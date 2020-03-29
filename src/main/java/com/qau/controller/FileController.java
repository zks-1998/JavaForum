package com.qau.controller;

import com.qau.dto.FileDTO;
import com.qau.exception.CustomizeErrorCode;
import com.qau.exception.CustomizeException;
import com.qau.provider.UcloudProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


@Controller
public class FileController {
    @Autowired
    private UcloudProvider ucloudProvider;

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(MultipartHttpServletRequest request) {
        MultipartFile file = request.getFile("editormd-image-file");
        try {
            String fileName = ucloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setUrl(fileName);
            return fileDTO;
        } catch (Exception e) {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
    }
}
