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
package me.caiyi.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author caiyi
* @date 2025-01-21
**/
@Entity
@Data
@Table(name="t_card")
public class TCard implements Serializable {

    @Id
    @Column(name = "`card_id`")
    @ApiModelProperty(value = "cardId")
    private String cardId;

    @Column(name = "`user_id`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "userId")
    private String userId;

    @Column(name = "`real_name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "realName")
    private String realName;

    @Column(name = "`mobile`")
    @ApiModelProperty(value = "mobile")
    private String mobile;

    @Column(name = "`phone_number`")
    @ApiModelProperty(value = "phoneNumber")
    private String phoneNumber;

    @Column(name = "`email`")
    @ApiModelProperty(value = "email")
    private String email;

    @Column(name = "`address`")
    @ApiModelProperty(value = "address")
    private String address;

    @Column(name = "`postcode`")
    @ApiModelProperty(value = "postcode")
    private String postcode;

    @Column(name = "`organization`")
    @ApiModelProperty(value = "organization")
    private String organization;

    @Column(name = "`department`")
    @ApiModelProperty(value = "department")
    private String department;

    @Column(name = "`position`")
    @ApiModelProperty(value = "position")
    private String position;

    @Column(name = "`valid`")
    @ApiModelProperty(value = "valid")
    private String valid;

    @Column(name = "`background`")
    @ApiModelProperty(value = "background")
    private String background;

    public void copy(TCard source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
