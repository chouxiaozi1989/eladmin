package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * cos对象存储配置类
 * @author caiyi
 */
@Data
@Entity
@Table(name = "tool_cos_config")
public class CosConfig implements Serializable {

    @Id
    @Column(name = "config_id")
    @ApiModelProperty(value = "ID")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @NotBlank
    @Column(name = "secret_id")
    @ApiModelProperty(value = "secret_id")
    private String secretId;

    @NotBlank
    @Column(name = "secret_key")
    @ApiModelProperty(value = "secret_key")
    private String secretKey;

    @NotBlank
    @Column(name = "bucket_name")
    @ApiModelProperty(value = "bucket_name")
    private String bucketName;

    @NotBlank
    @Column(name = "host")
    @ApiModelProperty(value = "host")
    private String host;

    @NotBlank
    @Column(name = "type")
    @ApiModelProperty(value = "type")
    private String type = "公开";

    @NotBlank
    @Column(name = "region")
    @ApiModelProperty(value = "region")
    private String region;
}
