package com.qau.mapper;

import com.qau.model.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}