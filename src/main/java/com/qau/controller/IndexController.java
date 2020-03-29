package com.qau.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qau.dto.QuestionDTO;
import com.qau.mapper.QuestionMapper;
import com.qau.mapper.UserMapper;
import com.qau.model.Question;
import com.qau.model.QuestionExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum){
        PageHelper.startPage(pageNum,5);
        QuestionExample example = new QuestionExample();
        example.setOrderByClause("gmt_create desc");
        List<Question> questionList = questionMapper.selectByExample(example);
        PageInfo pageinfo = new PageInfo(questionList);
        if(questionList == null){
            return "index";
        }else{
            List<QuestionDTO> questionDTOList = new ArrayList<>();
            for(Question question:questionList){
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question,questionDTO);
                questionDTO.setUser(userMapper.selectByPrimaryKey(question.getCreator()));
                questionDTOList.add(questionDTO);
            }
            PageInfo pageInfo = new PageInfo();
            BeanUtils.copyProperties(pageinfo,pageInfo);
            pageInfo.setList(questionDTOList);
            model.addAttribute("pageInfo",pageInfo);
            return "index";
        }
    }
}
