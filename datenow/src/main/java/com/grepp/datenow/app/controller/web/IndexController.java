package com.grepp.datenow.app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    // 메인 페이지
    // 메서드명은 다른 걸 쓰자. > mainPage ?
    @GetMapping("/")
    public String main() {
        return "/mainPage";
    }
    
}
