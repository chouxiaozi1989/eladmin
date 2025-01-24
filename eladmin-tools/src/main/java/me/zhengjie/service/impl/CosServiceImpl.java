/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.service.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.CosConfig;
import me.zhengjie.domain.CosContent;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.repository.CosConfigRepository;
import me.zhengjie.repository.CosContentRepository;
import me.zhengjie.service.CosService;
import me.zhengjie.service.dto.CosQueryCriteria;
import me.zhengjie.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author caiyi
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "cos", keyGenerator = "keyGenerator")
public class CosServiceImpl implements CosService {

    private final CosConfigRepository cosConfigRepository;
    private final CosContentRepository cosContentRepository;

    private final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    @Value("${cos.max-size}")
    private Long maxSize;

    @Override
    @Cacheable(key = "'config'")
    public CosConfig find() {
        Optional<CosConfig> cosConfig = cosConfigRepository.findAll().stream().findFirst();
        return cosConfig.orElseGet(CosConfig::new);
    }

    @Override
    @CachePut(key = "'config'")
    @Transactional(rollbackFor = Exception.class)
    public CosConfig config(CosConfig cosConfig) {
        String http = "http://", https = "https://";
        if (!(cosConfig.getHost().toLowerCase().startsWith(http)||cosConfig.getHost().toLowerCase().startsWith(https))) {
            throw new BadRequestException("外链域名必须以http://或者https://开头");
        }
        return cosConfigRepository.save(cosConfig);
    }

    @Override
    public PageResult<CosContent> queryAll(CosQueryCriteria criteria, Pageable pageable){
        return PageUtil.toPage(cosContentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable));
    }

    @Override
    public List<CosContent> queryAll(CosQueryCriteria criteria) {
        return cosContentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CosContent upload(MultipartFile file, CosConfig cosConfig) throws Exception {
        FileUtil.checkSize(maxSize, file.getSize());
        if(cosConfig.getId() == null){
            throw new BadRequestException("请先添加相应配置，再操作");
        }

        TransferManager transferManager = createTransferManager(cosConfig);

        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new Exception("文件名为空，请检查！");
        }
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        File localFile = File.createTempFile(fileName, prefix);
        file.transferTo(localFile);
        String key = authentication.getName().concat("/").concat(fileName);
        CosContent content;
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosConfig.getBucketName(), key, localFile);
        try {
            Upload upload = transferManager.upload(putObjectRequest);
            UploadResult uploadResult = upload.waitForUploadResult();
            content = new CosContent();
            content.setFileName(fileName);
            content.setFileSize((double) file.getSize());
            content.setKey(uploadResult.getKey());
            content.setBucketName(uploadResult.getBucketName());
            content.setSuffix(FileUtil.getSuffix(fileName));
            content.setUpdateTime(new Timestamp(DateUtil.parseLocalDateTimeFormat(uploadResult.getDateStr()).toInstant(ZoneOffset.of("+8")).toEpochMilli()));
            content.setRequestId(uploadResult.getRequestId());
            content.setVersionId(uploadResult.getVersionId());
            content.setCrc64Ecma(uploadResult.getCrc64Ecma());
            cosContentRepository.save(content);
        } catch (CosServiceException e) {
            throw new BadRequestException(e.getMessage());
        }
        transferManager.shutdownNow();
        return content;
    }

    @Override
    public CosContent findByContentId(String id) {
        CosContent cosContent = cosContentRepository.findById(id).orElseGet(CosContent::new);
        ValidationUtil.isNull(cosContent.getId(),"CosContent", "id",id);
        return cosContent;
    }

    @Override
    public String download(CosContent content,CosConfig config){
        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(content.getBucketName(), content.getKey(), HttpMethodName.GET);
        ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
        String responseContentType = "image/".concat(content.getSuffix());
        String responseContentLanguage = "zh-CN";
        String responseContentDispositon = "filename=".concat(content.getFileName());
        String responseCacheControl = "no-cache";
        String cacheExpireStr = DateUtil.formatRFC822Date(new Date(System.currentTimeMillis() + 24 * 3600 * 1000));
        responseHeaders.setContentType(responseContentType);
        responseHeaders.setContentLanguage(responseContentLanguage);
        responseHeaders.setContentDisposition(responseContentDispositon);
        responseHeaders.setCacheControl(responseCacheControl);
        responseHeaders.setExpires(cacheExpireStr);
        req.setResponseHeaders(responseHeaders);
        Date expirationDate = new Date(System.currentTimeMillis() + 60 * 1000);
        req.setExpiration(expirationDate);
        return createCli(config).generatePresignedUrl(req).toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(CosContent content, CosConfig config) {
        createCli(config).deleteObject(content.getBucketName(), content.getKey());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void synchronize(CosConfig config) {
        if(config.getId() == null){
            throw new BadRequestException("请先添加相应配置，再操作");
        }
        String bucketName = config.getBucketName();
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        // 设置bucket名称
        listObjectsRequest.setBucketName(bucketName);
        // prefix表示列出的object的key以prefix开始
        listObjectsRequest.setPrefix("");
        // deliter表示分隔符, 设置为/表示列出当前目录下的object, 设置为空表示列出所有的object
        listObjectsRequest.setDelimiter("");
        // 设置最大遍历出多少个对象, 一次listobject最大支持1000
        listObjectsRequest.setMaxKeys(1000);
        ObjectListing objectListing = null;
        COSClient cosClient = createCli(config);
        do {
            try {
                objectListing = cosClient.listObjects(listObjectsRequest);
            } catch (CosServiceException e) {
                throw new BadRequestException(e.getMessage());
            }
            // common prefix表示表示被delimiter截断的路径, 如delimter设置为/, common prefix则表示所有子目录的路径
            List<String> commonPrefixes = objectListing.getCommonPrefixes();

            // object summary表示所有列出的object列表
            List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
            for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
                if (cosContentRepository.findByKey(FileUtil.getFileNameNoEx(cosObjectSummary.getKey())) == null){
                    ObjectMetadata objectMetadata = cosClient.getObjectMetadata(bucketName, cosObjectSummary.getKey());
                    CosContent content = new CosContent();
                    String fileName = FileUtil.getFileNameNoPath(cosObjectSummary.getKey());
                    content.setFileName(fileName);
                    content.setFileSize((double) cosObjectSummary.getSize());
                    content.setKey(cosObjectSummary.getKey());
                    content.setBucketName(cosObjectSummary.getBucketName());
                    content.setSuffix(FileUtil.getSuffix(fileName));
                    content.setUpdateTime(new Timestamp(DateUtil.parseLocalDateTimeFormat(cosObjectSummary.getLastModified().toString()).toInstant(ZoneOffset.of("+8")).toEpochMilli()));
                    content.setRequestId(objectMetadata.getRequestId());
                    content.setVersionId(objectMetadata.getVersionId());
                    content.setCrc64Ecma(objectMetadata.getCrc64Ecma());
                    cosContentRepository.save(content);
                }
            }

            String nextMarker = objectListing.getNextMarker();
            listObjectsRequest.setMarker(nextMarker);
        } while (objectListing.isTruncated());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(String[] ids, CosConfig config) {
        for (String id : ids) {
            delete(findByContentId(id), config);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(String type) {
        cosConfigRepository.update(type);
    }

    @Override
    public void downloadList(List<CosContent> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CosContent content : queryAll) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("id", content.getId());
            map.put("文件名", content.getFileName());
            map.put("文件类型", content.getSuffix());
            map.put("空间名称", content.getBucketName());
            map.put("文件大小", content.getFileSize());
            map.put("空间类型", content.getType());
            map.put("请求id", content.getRequestId());
            map.put("版本号", content.getVersionId());
            map.put("Crc64Ecma", content.getCrc64Ecma());
            map.put("创建日期", content.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    private static TransferManager createTransferManager(CosConfig config) {
        COSCredentials cred = new BasicCOSCredentials(config.getSecretId(), config.getSecretKey());
        ClientConfig clientConfig = new ClientConfig(new Region(config.getRegion()));
        clientConfig.setHttpProtocol(HttpProtocol.https);
        COSClient cosclient = new COSClient(cred, clientConfig);
        ExecutorService threadPool = Executors.newFixedThreadPool(16);
        return new TransferManager(cosclient, threadPool);
    }

    private static COSClient createCli(CosConfig config) {
        COSCredentials cred = new BasicCOSCredentials(config.getSecretId(), config.getSecretKey());
        ClientConfig clientConfig = new ClientConfig(new Region(config.getRegion()));
        return new COSClient(cred, clientConfig);
    }
}
