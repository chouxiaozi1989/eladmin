package me.caiyi.service.dto;

import lombok.Data;

@Data
public class MiniProgramLoginDto {
    String user_name;
    String user_id;
    String session_key;
    String openid;
}
