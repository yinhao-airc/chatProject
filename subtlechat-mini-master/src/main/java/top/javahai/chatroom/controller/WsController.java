package top.javahai.chatroom.controller;

import com.github.binarywang.java.emoji.EmojiConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.javahai.chatroom.entity.GroupMsgContent;
import top.javahai.chatroom.entity.GroupMsgRepository;
import top.javahai.chatroom.entity.Message;
import top.javahai.chatroom.entity.User;
import top.javahai.chatroom.service.GroupMsgContentService;
import top.javahai.chatroom.utils.TuLingUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author Hai
 * @date 2020/6/16 - 23:34
 */
@Controller
public class WsController {
  @Autowired
  SimpMessagingTemplate simpMessagingTemplate;
  @Autowired
  GroupMsgRepository groupMsgRepository;

  /**
   * 单聊的消息的接受与转发
   * @param authentication
   * @param message
   */
  @MessageMapping("/ws/chat")
  public void handleMessage(Authentication authentication, Message message){
//    User user= ((User) authentication.getPrincipal());
//    message.setFromNickname(user.getNickname());
//    message.setFrom(user.getUsername());
    message.setCreateTime(new Date());
    simpMessagingTemplate.convertAndSendToUser(message.getTo(),"/queue/chat",message);
  }


  @Autowired
  GroupMsgContentService groupMsgContentService;
  EmojiConverter emojiConverter = EmojiConverter.getInstance();

  SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  /**
   * 群聊的消息接受与转发
   * @param authentication
   * @param groupMsgContent
   */
  @MessageMapping("/ws/groupChat")
  public void handleGroupMessage(Authentication authentication, GroupMsgContent groupMsgContent){
    User currentUser= (User) authentication.getPrincipal();
    //处理emoji内容,转换成unicode编码
    groupMsgContent.setContent(emojiConverter.toHtml(groupMsgContent.getContent()));
    //保证来源正确性，从Security中获取用户信息
    groupMsgContent.setFromId(currentUser.getId());
    groupMsgContent.setFromName(currentUser.getNickname());
    groupMsgContent.setFromProfile(currentUser.getUserProfile());
    groupMsgContent.setCreateTime(new Date());
    //保存该条群聊消息记录到数据库中
//    groupMsgContentService.insert(groupMsgContent);
    groupMsgRepository.addGroupMsgContent(groupMsgContent);
    //转发该条数据
    simpMessagingTemplate.convertAndSend("/topic/greetings",groupMsgContent);
  }


//  /**
//   * 接受前端发来的消息，获得图灵机器人回复并转发回给发送者
//   * @param authentication
//   * @param message
//   * @throws IOException
//   */
//  @MessageMapping("/ws/robotChat")
//  public void handleRobotChatMessage(Authentication authentication, Message message) throws IOException {
//    User user = ((User) authentication.getPrincipal());
//    //接收到的消息
//    message.setFrom(user.getUsername());
//    message.setCreateTime(new Date());
//    message.setFromNickname(user.getNickname());
//    message.setFromUserProfile(user.getUserProfile());
//    //发送消息内容给机器人，获得回复
//    String result = TuLingUtil.replyMessage(message.getContent());
//    //构建返回消息JSON字符串
//    Message resultMessage = new Message();
//    resultMessage.setFrom("瓦力");
//    resultMessage.setCreateTime(new Date());
//    resultMessage.setFromNickname("瓦力");
//    resultMessage.setContent(result);
//    //回送机器人回复的消息给发送者
//    simpMessagingTemplate.convertAndSendToUser(message.getFrom(),"/queue/robot",resultMessage);
//
//  }
}
