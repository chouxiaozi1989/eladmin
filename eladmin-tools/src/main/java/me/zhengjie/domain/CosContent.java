package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "tool_cos_content")
public class CosContent {
    @Id
    @Column(name = "content_id")
    @ApiModelProperty(value = "ID")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @NotBlank
    @Column(name = "bucket_name")
    @ApiModelProperty(value = "bucket_name")
    private String bucketName;

    @NotBlank
    @Column(name = "file_name")
    @ApiModelProperty(value = "file_name")
    private String fileName;

    @NotBlank
    @Column(name = "file_size")
    @ApiModelProperty(value = "file_size")
    private Double fileSize;

    @NotBlank
    @Column(name = "type")
    @ApiModelProperty(value = "type")
    private String type = "公开";

    @NotBlank
    @Column(name = "content_key")
    @ApiModelProperty(value = "key")
    private String key;

    @NotBlank
    @Column(name = "suffix")
    @ApiModelProperty(value = "suffix")
    private String suffix;

    @NotBlank
    @Column(name = "update_time")
    @ApiModelProperty(value = "update_time")
    private Timestamp updateTime;

    @NotBlank
    @Column(name = "crc64Ecma")
    @ApiModelProperty(value = "crc64Ecma")
    private String crc64Ecma;

    @NotBlank
    @Column(name = "version_id")
    @ApiModelProperty(value = "version_id")
    private String versionId;

    @NotBlank
    @Column(name = "request_id")
    @ApiModelProperty(value = "request_id")
    private String requestId;
}
