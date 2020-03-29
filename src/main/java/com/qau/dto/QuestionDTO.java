package com.qau.dto;

import com.qau.model.Question;
import com.qau.model.User;
import lombok.Data;

@Data
public class QuestionDTO extends Question {
    private User user;
}
