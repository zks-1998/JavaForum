package com.qau.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long questionid;
    private String name;
    private String type;
    private String title;
    private Integer status;
}
