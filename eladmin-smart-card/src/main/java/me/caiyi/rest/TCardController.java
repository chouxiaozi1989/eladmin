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
package me.caiyi.rest;

import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import me.caiyi.service.dto.MiniProgramLoginDto;
import me.zhengjie.annotation.Log;
import me.caiyi.domain.TCard;
import me.caiyi.service.TCardService;
import me.caiyi.service.dto.TCardQueryCriteria;
import me.zhengjie.annotation.rest.AnonymousAccess;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

import me.zhengjie.utils.PageResult;
import me.caiyi.service.dto.TCardDto;

/**
* @website https://eladmin.vip
* @author caiyi
* @date 2025-01-21
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "SmartCard")
@RequestMapping("/api/tCard")
public class TCardController {

    private final TCardService tCardService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('tCard:list')")
    public void exportTCard(HttpServletResponse response, TCardQueryCriteria criteria) throws IOException {
        tCardService.download(tCardService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询SmartCard")
    @PreAuthorize("@el.check('tCard:list')")
    public ResponseEntity<PageResult<TCardDto>> queryTCard(TCardQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(tCardService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增SmartCard")
    @ApiOperation("新增SmartCard")
    @PreAuthorize("@el.check('tCard:add')")
    public ResponseEntity<Object> createTCard(@Validated @RequestBody TCard resources){
        tCardService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改SmartCard")
    @ApiOperation("修改SmartCard")
    @PreAuthorize("@el.check('tCard:edit')")
    public ResponseEntity<Object> updateTCard(@Validated @RequestBody TCard resources){
        tCardService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除SmartCard")
    @ApiOperation("删除SmartCard")
    @PreAuthorize("@el.check('tCard:del')")
    public ResponseEntity<Object> deleteTCard(@ApiParam(value = "传ID数组[]") @RequestBody String[] ids) {
        tCardService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @ApiOperation("小程序登录")
    @GetMapping(value = "/login/{code}")
    public ResponseEntity<MiniProgramLoginDto> login(@NotBlank @PathVariable String code) throws Exception {
        URL url = new URL("https://api.weixin.qq.com/sns/jscode2session");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(Method.GET.name());
        connection.setRequestProperty("Charset", "utf-8");
        if (connection.getResponseCode() != 200) {
            throw new Exception("服务端调用异常！");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String lines = reader.readLine();
        connection.disconnect();
        JSONObject jsonObject = JSON.parseObject(lines);
        MiniProgramLoginDto loginDto = new MiniProgramLoginDto();
        loginDto.setOpenid(jsonObject.get("openid").toString());
        loginDto.setSession_key(jsonObject.get("session_key").toString());
        return ResponseEntity.ok(loginDto);
    }
}