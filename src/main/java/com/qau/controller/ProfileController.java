package com.qau.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qau.dto.NotificationDTO;
import com.qau.dto.QuestionDTO;
import com.qau.enums.NotificationEnum;
import com.qau.mapper.*;
import com.qau.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;
import javax.swing.undo.CannotUndoException;
import java.util.ArrayList;
import java.util.List;


@Controller
public class ProfileController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private CommentMapper commentMapper;

    @GetMapping("/profile/{action}")
    public String Profile(@PathVariable(name = "action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            PageHelper.startPage(pageNum, 5);
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().
                    andCreatorEqualTo(user.getId());
            List<Question> questions = questionMapper.selectByExample(questionExample);
            PageInfo pageinfo = new PageInfo(questions);
            if (questions == null) {
                return "profile";
            } else {
                List<QuestionDTO> questionDTOList = new ArrayList<>();
                for(Question question:questions){
                    QuestionDTO questionDTO = new QuestionDTO();
                    BeanUtils.copyProperties(question,questionDTO);
                    questionDTO.setUser(userMapper.selectByPrimaryKey(question.getCreator()));
                    questionDTOList.add(questionDTO);
                }
                PageInfo pageInfo = new PageInfo();
                BeanUtils.copyProperties(pageinfo,pageInfo);
                pageInfo.setList(questionDTOList);
                model.addAttribute("pageInfo",pageInfo);
            }
        } else if ("replies".equals(action)) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
            PageHelper.startPage(pageNum, 5);
            NotificationExample example = new NotificationExample();
            example.createCriteria().andReceiverEqualTo(user.getId());
            example.setOrderByClause("gmt_create desc");
            List<Notification> notificationList = notificationMapper.selectByExample(example);
            PageInfo pageinfo= new PageInfo(notificationList);
            if(notificationList == null){
                return "profile";
            }else{
                List<NotificationDTO> notificationDTOS = new ArrayList<>();
                for (Notification notification : notificationList){
                    NotificationDTO notificationDTO = new NotificationDTO();
                    notificationDTO.setName(userMapper.selectByPrimaryKey(notification.getNotifier()).getName());
                    if(notification.getType() == 1){
                        notificationDTO.setId(notification.getId());
                        notificationDTO.setType(NotificationEnum.REPLY_QUESTION.getName());
                        Question question = questionMapper.selectByPrimaryKey(notification.getOuterid());
                        notificationDTO.setTitle(question.getTitle());
                        notificationDTO.setQuestionid(question.getId());
                    }else{
                        notificationDTO.setId(notification.getId());
                        notificationDTO.setType(NotificationEnum.REPLY_COMMENT.getName());
                        notificationDTO.setTitle(commentMapper.selectByPrimaryKey(notification.getOuterid()).getContent());
                        notificationDTO.setQuestionid(questionMapper.selectByPrimaryKey(commentMapper.selectByPrimaryKey(notification.getOuterid()).getParentId()).getId());
                    }
                    notificationDTO.setStatus(notification.getStatus());
                    notificationDTOS.add(notificationDTO);
                }
                PageInfo pageInfo = new PageInfo();
                BeanUtils.copyProperties(pageinfo,pageInfo);
                pageInfo.setList(notificationDTOS);
                model.addAttribute("pageInfo",pageInfo);
            }
        }
        return "profile";
    }
}
