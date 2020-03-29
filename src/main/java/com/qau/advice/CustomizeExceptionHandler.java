package com.qau.advice;

import com.alibaba.fastjson.JSON;
import com.qau.dto.ResultDTO;
import com.qau.exception.CustomizeErrorCode;
import com.qau.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//用@ControllerAdvice和@ExceptionHandler处理异常
@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class) //所有的exception都处理
    ModelAndView handle(Throwable e, Model model , HttpServletRequest request, HttpServletResponse response) throws IOException {

        String contentType = request.getContentType();
        if("application/json".equals(contentType)){
            ResultDTO resultDTO = null;
            //返回JSON
            if(e instanceof CustomizeException){
                resultDTO = ResultDTO.errorOf((CustomizeException)e);
            }else {
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try{
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            }catch (IOException ioe){
            }
            return null;

        }else{
            //返回错误页面
            if(e instanceof CustomizeException){
                model.addAttribute("message",e.getMessage());
            }else {
                model.addAttribute("message",e.getMessage());
            }
            return new ModelAndView("error");
        }
    }
}
