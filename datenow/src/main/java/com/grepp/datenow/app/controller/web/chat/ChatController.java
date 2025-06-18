package com.grepp.datenow.app.controller.web.chat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChatController {
  @GetMapping("/chatList")
  public String chatList(){
    return "fragments/chatList";
  }

  @GetMapping("/chatList/{id}")
  public String chatProcess(@PathVariable Long id, Model model){

    model.addAttribute("roomId",id);
    return "chat";
  }

  @GetMapping("/newchat")
  public String chatNew(){
    return "chat_start";
  }

}
