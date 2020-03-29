package com.qau.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qau.dto.CommentDTO;
import com.qau.dto.QuestionDTO;
import com.qau.enums.CommentTypeEnum;
import com.qau.exception.CustomizeErrorCode;
import com.qau.exception.CustomizeException;
import com.qau.mapper.NotificationExtMapper;
import com.qau.mapper.QuestionExtMapper;
import com.qau.mapper.UserMapper;
import com.qau.model.Question;
import com.qau.service.CommentService;
import com.qau.service.QuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private NotificationExtMapper notificationExtMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id, Model model) {
        QuestionDTO questionDTO = questionService.getById(id);
        if (questionDTO == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        List<QuestionDTO> relateQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        model.addAttribute("question", questionDTO);
        //累加阅读数
        questionService.incView(id);
        model.addAttribute("relateQuestions", relateQuestions);
        model.addAttribute("comments", comments);
        return "question";
    }

    @GetMapping("/question/{id}/{notificationid}")
    public String questionread(@PathVariable(name = "id") Long id, @PathVariable("notificationid") Long notificationid, Model model) {
        QuestionDTO questionDTO = questionService.getById(id);
        if (questionDTO == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        List<QuestionDTO> relateQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        model.addAttribute("question", questionDTO);
        //累加阅读数
        questionService.incView(id);
        notificationExtMapper.updateStatus(notificationid);
        model.addAttribute("relateQuestions", relateQuestions);
        model.addAttribute("comments", comments);
        return "question";
    }

    @GetMapping("/question/search")
    public String questionSearch(Model model,
                                @RequestParam(name = "search", required = false) String search,
                                 @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum) {
        if(search.trim()==null){
            return "index";
        }
        PageHelper.startPage(pageNum, 5);
        List<Question> questionList = questionExtMapper.searchQuestion(search.trim());
        PageInfo pageinfo = new PageInfo(questionList);
        if (questionList == null) {
            return "search";
        } else {
            List<QuestionDTO> questionDTOList = new ArrayList<>();
            for (Question question : questionList) {
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question, questionDTO);
                questionDTO.setUser(userMapper.selectByPrimaryKey(question.getCreator()));
                questionDTOList.add(questionDTO);
            }
            PageInfo pageInfo = new PageInfo();
            BeanUtils.copyProperties(pageinfo,pageInfo);
            pageInfo.setList(questionDTOList);
            model.addAttribute("pageInfo", pageInfo);
            model.addAttribute("searchwords",search);
            return "search";
        }
    }
}
