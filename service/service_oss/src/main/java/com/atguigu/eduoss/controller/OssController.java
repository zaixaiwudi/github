package com.atguigu.eduoss.controller;

import com.atguigu.Result;
import com.atguigu.eduoss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
//@CrossOrigin
@RequestMapping("/eduoss/oss")
public class OssController {
    @Autowired
    private OssService ossService;

    //上传讲师头像
    @PostMapping("upload")
    public Result uploadFile(MultipartFile file){
        //获取上传过来的文件MultipartFile,file要和<input name="file>值一样
        //调用service
        //返回上传之后阿里云图片地址
       String url =  ossService.uploadFileOss(file);
        return Result.ok().data("url",url);
    }
}
