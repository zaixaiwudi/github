package com.atguigu.eduoss.service.impl;

import com.aliyun.oss.OSSClient;
import com.atguigu.Result;
import com.atguigu.eduoss.service.OssService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileOss(MultipartFile file) {
        OSSClient ossClient = null;
        try {
            //定义需要固定值。
            // Endpoint以杭州为例，其它Region请按实际情况填写。
            String endpoint = "oss-cn-beijing.aliyuncs.com";
            // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
            String accessKeyId = "LTAI4GCi5wcLTsAAtprP6nNb";
            String accessKeySecret = "t5ZDHHR7809TJPw04fW2hRDCzO6Tb8";
            String backetName = "zaixiawudi";

            ossClient = new OSSClient(endpoint, accessKeyId,accessKeySecret);
            //获取上传输入流
            InputStream in = file.getInputStream();
            String filename = file.getOriginalFilename();
            //在文件名称前添加uuid值保证每个名称不一样
            filename = UUID.randomUUID().toString().replaceAll("-", "")+filename;
            //根据当前日期创建文件夹,存储文件
            //2020/1/1/1.jpg
            //DateTime()的作用是附带上日期时间并创建日期时间格式的文件夹
            String filePath = new DateTime().toString("yyyy/MM/dd");

            filename = filePath+"/"+filename;

            //bucket名称，文件路径和名称，输入流
            //putObject的作用是把文件放在ducket中
            ossClient.putObject(backetName,filename,in);

            //返回上传文件在阿里云的地址
            String url = "https://" + backetName + "." + endpoint + "/" + filename;
            return url;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭上传对象
            ossClient.shutdown();
        }
    return null;
    }
}
