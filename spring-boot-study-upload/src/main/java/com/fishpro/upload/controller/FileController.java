package com.fishpro.upload.controller;

import com.fishpro.upload.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class FileController {

    @Value("${fishpro.uploadPath}")
    private String uploadPath;

    @GetMapping("/uploadfile")
    public String uploadfile(){
        return "uploadfile";
    }


    /**
     * 单文件
     * */
    @PostMapping("/upload")
    @ResponseBody
    Object upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Map<String,Object> map=new HashMap();
        map.put("status",0);
        String fileName = file.getOriginalFilename();
        fileName = UUID.randomUUID().toString(); //对文件名称重命名

        try {
            FileUtil.uploadFile(file.getBytes(), uploadPath, fileName);
            map.put("filename",fileName);
        } catch (Exception e) {
           map.put("status",-1);
           map.put("message",e.getMessage());

        }


        return map;
    }

    /**
     * 多文件
     * */
    @PostMapping("/uploads")
    @ResponseBody
    Object uploads(@RequestParam("files") MultipartFile [] files, HttpServletRequest request) {
        Map<String,Object> map=new HashMap();
        map.put("status",0);
        List<String> filenames=new ArrayList<>();
        for (MultipartFile file: files
             ) {
            String ext = file.getOriginalFilename().split("\\.")[1];
            String fileName = file.getOriginalFilename();
            //fileName = UUID.randomUUID().toString()+"."+ext; //对文件名称重命名

            try {
                FileUtil.uploadFile(file.getBytes(), uploadPath, fileName);
                filenames.add(fileName);
            } catch (Exception e) {
                map.put("status",-1);
                map.put("message",e.getMessage());
                return  map;

            }
        }

        map.put("filename",filenames);
        return map;
    }
}
