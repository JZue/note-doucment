package com.jzue.controller;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author: junzexue
 * @Date: 2019/4/22 下午5:02
 * @Description:
 **/
@RestController
public class UploadController {

    @Value("${cmsUploadPath}")
    private String cmsUploadPath;

    @Value("${cmsUploadUrl}")
    private String cmsUploadUrl;

    @Value("${uploadDelayTime}")
    private int uploadDelayTime;

    /**
     *指定尺寸的主题包预览图上传
     * 规格 1920*720
     * 压缩规格 300*200
     */
    @PostMapping(value= "/uploadThemePic")
    @ResponseBody
    public Map<String, Object> uploadThemePic(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileUploadException {
        int width=200;
        int height=400;
        int smallWidth=100;
        int smallHeight=200;
        String savePath = cmsUploadPath;
        String saveUrl = cmsUploadUrl;
        Map<String , Object> uploadMap = ImageFileUtil.compressUploadTwCover(request,response,uploadDelayTime,savePath,saveUrl,
                width,height,
                smallWidth,smallHeight);
        return uploadMap;
    }


}
