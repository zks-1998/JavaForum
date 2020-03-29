package com.qau.dto;

import lombok.Data;

//需要这五个属性获取access_token
@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
