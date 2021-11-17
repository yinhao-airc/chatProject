package top.javahai.chatroom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.javahai.chatroom.config.VerificationCode;
import top.javahai.chatroom.entity.RespBean;
import top.javahai.chatroom.entity.User;
import top.javahai.chatroom.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Random;

/**
 * @author Hai
 * @date 2020/6/16 - 17:33
 */
@RestController
public class LoginController {

  @Autowired
  UserServiceImpl userService;
  @Autowired
  SimpMessagingTemplate simpMessagingTemplate;
  /**
   * 获取验证码图片写到响应的输出流中，保存验证码到session
   * @param response
   * @param session
   * @throws IOException
   */
  @GetMapping("/verifyCode")
  public void getVerifyCode(HttpServletResponse response, HttpSession session) throws IOException {
    VerificationCode code = new VerificationCode();
    BufferedImage image = code.getImage();
    String text = code.getText();
    session.setAttribute("verify_code",text);
    VerificationCode.output(image,response.getOutputStream());
  }

  /**
   * 获取验证码图片写到响应的输出流中，保存验证码到session
   * @param resp
   * @param authentication
   * @throws IOException
   */
  @GetMapping("/doLoginNew2")
  public RespBean doLoginNew(HttpServletResponse resp, Authentication authentication) throws IOException {

    resp.setContentType("application/json;charset=utf-8");
    PrintWriter out=resp.getWriter();
    User user=(User) authentication.getPrincipal();
    user.setNickname(user.getUsername());
    user.setPassword(null);
    //更新用户状态为在线
    userService.setUserStateToOn(user.getId());
    user.setUserStateId(1);
    //广播系统通知消息
    simpMessagingTemplate.convertAndSend("/topic/notification","系统消息：用户【"+user.getNickname()+"】进入了聊天室");
    RespBean ok = RespBean.ok("登录成功", user);
    String s = new ObjectMapper().writeValueAsString(ok);
    out.write(s);
    return ok;

  }



  @Autowired
  JavaMailSender javaMailSender;
  /**
   * 获取邮箱验证码，并保存到本次会话
   * @param session
   */
  @GetMapping("/admin/mailVerifyCode")
  public RespBean getMailVerifyCode(HttpSession session){
    //获取随机的四个数字
    StringBuilder code=new StringBuilder();
    for (int i = 0; i <4; i++) {
      code.append(new Random().nextInt(10));
    }
   //邮件内容
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setSubject("微言聊天室管理端验证码验证");
    msg.setText("本次登录的验证码："+code);
    msg.setFrom("1258398543@qq.com");
    msg.setSentDate(new Date());
    msg.setTo("jinhaihuang824@aliyun.com");
    //保存验证码到本次会话
    session.setAttribute("mail_verify_code",code.toString());
    //发送到邮箱
    try {
      javaMailSender.send(msg);
      return RespBean.ok("验证码已发送到邮箱，请注意查看！");
    }catch (Exception e){
      e.printStackTrace();
      return RespBean.error("服务器出错啦！请稍后重试！");
    }
  }
}
