package top.javahai.chatroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.javahai.chatroom.entity.ParticipantRepository;
import top.javahai.chatroom.entity.User;
import top.javahai.chatroom.service.UserService;
import top.javahai.chatroom.utils.UserUtil;

import java.util.List;

/**
 * @author Hai
 * @date 2020/6/16 - 21:32
 */
@RestController
@RequestMapping("/chat")
public class ChatController {
  @Autowired
  UserService userService;
  @Autowired
  ParticipantRepository participantRepository;

  @GetMapping("/users")
  public List<User> getUsersWithoutCurrentUser(){
    return participantRepository.getUsersWithoutCurrentUser();
  }
}
