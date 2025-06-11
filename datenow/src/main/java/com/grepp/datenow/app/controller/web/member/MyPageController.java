package com.grepp.datenow.app.controller.web.member;

import com.grepp.datenow.app.model.auth.domain.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class MyPageController {
    @GetMapping("/my-page")
    public String myPageMain(Model model, @AuthenticationPrincipal Principal principal) {
        model.addAttribute("userId", principal.getUsername());
        return "my_page";
    }

}
