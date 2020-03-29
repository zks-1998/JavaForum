package com.qau.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND(2001, "你找到问题不在了，要不要换个试试？"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题进行评论或者回复"),
    NO_LOGIN(2003,"请先登录再重试"),
    SYS_ERROR(2004,"服务器压力太大啦!请起来运动运动稍后再来试试哦~"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"你操作的评论不存在"),
    CONTENT_IS_EMPTY(2007,"输入内容不能为空"),
    MESSAGE_NOT_FOUND(2008,"消息不翼而飞了,请去找别的小可爱吧~"),
    FILE_UPLOAD_FAIL(2009,"图片上传失败啦");


    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
