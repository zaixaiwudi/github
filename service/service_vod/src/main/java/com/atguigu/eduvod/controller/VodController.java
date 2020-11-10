package com.atguigu.eduvod.controller;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.Result;
import com.atguigu.common.servicebase.exception.GuliException;

import com.atguigu.eduvod.utils.VideoUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Path;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/eduvod/vod")
//@CrossOrigin
public class VodController {

    @PostMapping("uploadVideo")
    public Result uploadVideoAliyun(MultipartFile file){

        try {
            String fileName = file.getOriginalFilename(); //文件实际名称
            // abcd.qw.cc.mp4
            //一般经常传递文件名称不带后缀名
            String title = fileName.substring(0, fileName.lastIndexOf("."));
            InputStream inputStream = file.getInputStream();
            UploadStreamRequest request = new UploadStreamRequest("LTAI4GCi5wcLTsAAtprP6nNb", "t5ZDHHR7809TJPw04fW2hRDCzO6Tb8",
                    title, fileName, inputStream);

            // request.setEcsRegionId("cn-shanghai");
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            String videoId = "";
            if (response.isSuccess()) {
                videoId = response.getVideoId();
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                videoId = response.getVideoId();
            }
            return Result.ok().data("videoId",videoId);
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(20001,"上传视频失败");
        }
    }

    //删除阿里云视频
    @DeleteMapping("deleteVideo/{videoId}")
    public Result deleteVideo(@PathVariable String videoId){

        try {
            DefaultAcsClient client = VideoUtils.initVodClient("LTAI4GKxXc3pEavyXMt62Yog", "M51uiQEodSWLfn1bJzHEbjsFOCr42H");

            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(videoId);
            client.getAcsResponse(request);
            return Result.ok();
        } catch (ClientException e) {
            e.printStackTrace();
            throw  new GuliException(20001,"删除视频失败");
        }
    }

    //删除多个视频
    @DeleteMapping("removeMoreVideo")
    public Result removeMoreVideo(@RequestParam("videoIdList") List<String> videoIdList){
        try {
            DefaultAcsClient client = VideoUtils.initVodClient("LTAI4GKxXc3pEavyXMt62Yog", "M51uiQEodSWLfn1bJzHEbjsFOCr42H");

            DeleteVideoRequest request = new DeleteVideoRequest();
            String videos = StringUtils.join(videoIdList.toArray(),",");
            request.setVideoIds(videos);
            client.getAcsResponse(request);
            return Result.ok();
        } catch (ClientException e) {
            e.printStackTrace();
            throw  new GuliException(20001,"删除视频失败");
        }

    }


    //根据视频id获取凭证
    @GetMapping("getPlayAuth/{videoId}")
    public Result getPlayAuth(@PathVariable String videoId){

        try {
            //创建初始化对象
            DefaultAcsClient client = VideoUtils.initVodClient("LTAI4GCi5wcLTsAAtprP6nNb", "t5ZDHHR7809TJPw04fW2hRDCzO6Tb8");
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
            request.setVideoId(videoId);
            response = client.getAcsResponse(request);

            String playAuth = response.getPlayAuth();

            return Result.ok().data("playAuth",playAuth);
        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001,"获取失败");
        }

    }



}
