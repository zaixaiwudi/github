package com.atguigu.test;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class OssTest {
    //域节点
    String endpoint = "oss-cn-beijing.aliyuncs.com";
    //秘钥id
    String accessKeyId = "LTAI4GKxXc3pEavyXMt62Yog";
    //秘钥名称
    String accessKeyName = "M51uiQEodSWLfn1bJzHEbjsFOCr42H";
    //bucket名称
    String backetName = "0621-onlineteacher11";
    @Test
    public void ossTest(MultipartFile file){

        try {
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId,accessKeyName);
            InputStream inputStream = file.getInputStream();
            String fileName = file.getName();
            //把文件放到packet中
            ossClient.putObject(backetName, fileName, inputStream);
            //关闭
            ossClient.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
