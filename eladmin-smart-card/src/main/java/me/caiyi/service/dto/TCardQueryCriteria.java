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
package me.caiyi.service.dto;

import lombok.Data;
import java.util.List;
import me.zhengjie.annotation.Query;
import io.swagger.annotations.ApiModelProperty;

/**
* @website https://eladmin.vip
* @author caiyi
* @date 2025-01-21
**/
@Data
public class TCardQueryCriteria{

    /** 精确 */
    @Query
    @ApiModelProperty(value = "cardId")
    private String cardId;

    /** 精确 */
    @Query
    @ApiModelProperty(value = "userId")
    private String userId;

    /** 精确 */
    @Query
    @ApiModelProperty(value = "realName")
    private String realName;

    /** 精确 */
    @Query
    @ApiModelProperty(value = "mobile")
    private String mobile;

    /** 精确 */
    @Query
    @ApiModelProperty(value = "phoneNumber")
    private String phoneNumber;

    /** 精确 */
    @Query
    @ApiModelProperty(value = "email")
    private String email;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    @ApiModelProperty(value = "address")
    private String address;

    /** 精确 */
    @Query
    @ApiModelProperty(value = "postcode")
    private String postcode;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    @ApiModelProperty(value = "organization")
    private String organization;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    @ApiModelProperty(value = "department")
    private String department;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    @ApiModelProperty(value = "position")
    private String position;

    /** 精确 */
    @Query
    @ApiModelProperty(value = "valid")
    private String valid;

    /** 精确 */
    @Query
    @ApiModelProperty(value = "background")
    private String background;
}