package top.javahai.chatroom.controller;

import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.javahai.chatroom.entity.GroupMsgContent;
import top.javahai.chatroom.utils.FastDFSUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @author Hai
 * @date 2020/6/21 - 16:47
 */
@RestController
public class FileController {
//
//  @Value("${fastdfs.nginx.host}")
//  String nginxHost;
private static final String IMAGE_PREFIX = "/upload/";  //服务器储存上传图片地址的前缀

//  @PostMapping("/file")
//  public String uploadFlie(MultipartFile file) throws IOException, MyException {
//    String fileId= FastDFSUtil.upload(file);
//    String url=nginxHost+fileId;
//    return url;
//  }


  @PostMapping("/file")
  public String uploadFlie(HttpServletRequest request,MultipartFile file) throws IOException, MyException {
    String url="";
    if (!file.isEmpty()){
      String imageName = UUID.randomUUID().toString() + ".jpg";
      File imageFile = new File(request.getSession().getServletContext().getRealPath(IMAGE_PREFIX) + "/" + imageName);
      if (!imageFile.exists()){
        imageFile.mkdirs();
      }
      try {
        //上传图片到目录
        file.transferTo(imageFile);
        // 图片的src
        url=request.getContextPath() + IMAGE_PREFIX + imageName;
        String currUrl= ResourceUtils.getURL("classpath:").getPath();
        System.out.println("currUrl:"+currUrl);
      } catch (IOException e) {
        return "upload false";
      }
    }
    return url;
  }

}
