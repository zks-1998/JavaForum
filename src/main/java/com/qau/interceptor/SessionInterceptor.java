package com.qau.interceptor;

import com.qau.mapper.NotificationExtMapper;
import com.qau.mapper.UserMapper;
import com.qau.model.User;
import com.qau.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationExtMapper notificationExtMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    UserExample userExample = new UserExample();
                    userExample.createCriteria().andTokenEqualTo(token);
                    List<User> users =userMapper.selectByExample(userExample);
                    if(users.size()!=0){
                        request.getSession().setAttribute("user",users.get(0));
                        int countStatus = notificationExtMapper.countStatus(users.get(0).getId(), 0);
                        request.getSession().setAttribute("countStatus",countStatus);
                    }
                    break;
                }
            }
        }
        return true;
    }
}
