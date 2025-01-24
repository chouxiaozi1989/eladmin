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
package me.zhengjie.service;

import me.zhengjie.domain.CosConfig;
import me.zhengjie.domain.CosContent;
import me.zhengjie.service.dto.CosQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author caiyi
 */
public interface CosService {

    /**
     * 查配置
     * @return CosConfig
     */
    CosConfig find();

    /**
     * 修改配置
     * @param cosConfig 配置
     * @return CosConfig
     */
    CosConfig config(CosConfig cosConfig);

    /**
     * 分页查询
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    PageResult<CosContent> queryAll(CosQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部
     * @param criteria 条件
     * @return /
     */
    List<CosContent> queryAll(CosQueryCriteria criteria);

    /**
     * 上传文件
     * @param file 文件
     * @param cosConfig 配置
     * @return CosContent
     */
    CosContent upload(MultipartFile file, CosConfig cosConfig) throws Exception;

    /**
     * 查询文件
     * @param id 文件ID
     * @return CosContent
     */
    CosContent findByContentId(String id);

    /**
     * 下载文件
     * @param content 文件信息
     * @param config 配置
     * @return String
     */
    String download(CosContent content, CosConfig config);

    /**
     * 删除文件
     * @param content 文件
     * @param config 配置
     */
    void delete(CosContent content, CosConfig config);

    /**
     * 同步数据
     * @param config 配置
     */
    void synchronize(CosConfig config);

    /**
     * 删除文件
     * @param ids 文件ID数组
     * @param config 配置
     */
    void deleteAll(String[] ids, CosConfig config);

    /**
     * 更新数据
     * @param type 类型
     */
    void update(String type);

    /**
     * 导出数据
     * @param queryAll /
     * @param response /
     * @throws IOException /
     */
    void downloadList(List<CosContent> queryAll, HttpServletResponse response) throws IOException;
}
