package com.qau.mapper;

import org.apache.ibatis.annotations.Param;

public interface NotificationExtMapper {
    int countStatus(@Param("receiver") Long receiver, @Param("status") Integer status);
    int updateStatus(@Param("id") Long id);
}
