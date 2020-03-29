package com.qau.dto;

import lombok.Data;

@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;//是评论问题本身 还是评论的别人的评论
}
