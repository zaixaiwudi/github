package com.atguigu.eduoss.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


public interface OssService {
    //上传
    String uploadFileOss(MultipartFile file);
}
