package com.qau.controller;

import com.qau.dto.AccessTokenDTO;
import com.qau.dto.GithubUser;
import com.qau.model.User;
import com.qau.provider.GitHubProvider;
import com.qau.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GitHubProvider gitHubProvider;

    @Value("${github.client.id}") //从application.properties读配置信息 这些信息都是在GitHub创建OAuth Apps的得到的
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUrl;


    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    //通过AccessTokenDTO数据传输对象的数据拿到AccessToken
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO(); //快捷键Ctrl+Alt+V 在外部创建对象
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUrl);
        accessTokenDTO.setState(state);
        String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = gitHubProvider.getUser(accessToken);
        //System.out.println(githubUser.getName());
        if(githubUser!=null && githubUser.getId()!=null){
            //不为空  说明获取到了user信息 将信息插入数据库 并写cookie和session
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatar_url());
            userService.createOrUpdata(user);
            response.addCookie(new Cookie("token",token));
            return "redirect:/";  //重定向到index redirect返回的是路径 所以要加/
        }else {
            //登陆失败 重新登陆
            return "redirect:/";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}

