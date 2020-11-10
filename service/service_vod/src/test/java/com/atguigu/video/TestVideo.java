package com.atguigu.video;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;

import java.util.List;

public class TestVideo {

    public static void main(String[] args) {
        try {
            getVideoPlayAuth();
//            getVideoPlayURL();
//            uploadVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //上传视频
    public static void uploadVideo() {

        String accessKeyId = "LTAI4GCi5wcLTsAAtprP6nNb";
        String accessKeySecret = "t5ZDHHR7809TJPw04fW2hRDCzO6Tb8\n";

        String title = "6 - What If I Want to Move Faster - upload by sdk";   //上传之后文件名称
        String fileName = "D:/1.mp4";  //本地文件路径和名称

        //上传视频的方法
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
        request.setPartSize(2 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);

        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        }
    }

    //获取视频播放地址
    //获取视频播放凭证
    public static void getVideoPlayURL() throws ClientException{
        //第一步 获取初始化对象
        DefaultAcsClient client = VideoUtilsDemo.
                initVodClient("LTAI4GCi5wcLTsAAtprP6nNb","t5ZDHHR7809TJPw04fW2hRDCzO6Tb8");
        //第二步 创建获取视频地址的request和response对象
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();
        //第三步 向request设置视频id
        request.setVideoId("842e6ca9983a4eef890c718438a3731b");
        //第四步 调用初始化对象的方法获取内容，通过response接收
        response = client.getAcsResponse(request);
        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        //播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
        }
    }

    //获取视频播放凭证
    public static void getVideoPlayAuth() throws ClientException{
        //第一步 获取初始化对象
        DefaultAcsClient client = VideoUtilsDemo.
                initVodClient("LTAI4GCi5wcLTsAAtprP6nNb","t5ZDHHR7809TJPw04fW2hRDCzO6Tb8");

        //第二步 创建获取视频凭证的request和response对象
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();

        //第三步 向request对象设置视频id
        request.setVideoId("2b97c39a034a47be8e3e4a450c67cae3");

        //第四步 调用初始化对象的方法获取内容，通过response接收
        response = client.getAcsResponse(request);

        String playAuth = response.getPlayAuth();
        System.out.println("playauth:: "+playAuth);
    }
}
