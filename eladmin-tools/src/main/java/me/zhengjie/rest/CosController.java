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
package me.zhengjie.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.CosConfig;
import me.zhengjie.domain.CosContent;
import me.zhengjie.service.CosService;
import me.zhengjie.service.dto.CosQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Cos对象存储管理
 * @author caiyi
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cosContent")
@Api(tags = "工具：Cos对象存储管理")
public class CosController {

    private final CosService cosService;

    @GetMapping(value = "/config")
    public ResponseEntity<CosConfig> queryConfig(){
        return new ResponseEntity<>(cosService.find(), HttpStatus.OK);
    }

    @Log("配置七牛云存储")
    @ApiOperation("配置七牛云存储")
    @PutMapping(value = "/config")
    public ResponseEntity<Object> updateConfig(@Validated @RequestBody CosConfig qiniuConfig){
        cosService.config(qiniuConfig);
        cosService.update(qiniuConfig.getType());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public void export(HttpServletResponse response, CosQueryCriteria criteria) throws IOException {
        cosService.downloadList(cosService.queryAll(criteria), response);
    }

    @ApiOperation("查询文件")
    @GetMapping
    public ResponseEntity<PageResult<CosContent>> query(CosQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(cosService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @ApiOperation("上传文件")
    @PostMapping
    public ResponseEntity<Object> upload(@RequestParam MultipartFile file) throws Exception {
        CosConfig config = cosService.find();
        CosContent content = cosService.upload(file,config);
        Map<String,Object> map = new HashMap<>(3);
        map.put("id",content.getId());
        map.put("errno",0);
        map.put("data",new String[]{cosService.download(content, config)});
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @Log("同步cos数据")
    @ApiOperation("同步cos数据")
    @PostMapping(value = "/synchronize")
    public ResponseEntity<Object> synchronize(){
        cosService.synchronize(cosService.find());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("下载文件")
    @ApiOperation("下载文件")
    @GetMapping(value = "/download/{id}")
    public ResponseEntity<Object> download(@PathVariable String id){
        Map<String,Object> map = new HashMap<>(1);
        map.put("url", cosService.download(cosService.findByContentId(id),cosService.find()));
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @Log("删除文件")
    @ApiOperation("删除文件")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id){
        cosService.delete(cosService.findByContentId(id),cosService.find());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("删除多张图片")
    @ApiOperation("删除多张图片")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody String[] ids) {
        cosService.deleteAll(ids, cosService.find());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
