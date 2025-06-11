package com.grepp.datenow.app.controller.web.member;

import com.grepp.datenow.app.controller.web.member.payload.FindPasswordRequest;
import com.grepp.datenow.app.controller.web.member.payload.SigninRequest;
import com.grepp.datenow.app.controller.web.member.payload.SignupRequest;
import com.grepp.datenow.app.model.auth.code.Role;
import com.grepp.datenow.app.model.member.service.MemberService;
import jakarta.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    // 전체적으로 view 를 제외하고 모두 API Contoller 로 ㄱㄱ
    @GetMapping("/signin")
    public String signin(Model model){
        model.addAttribute("signinRequest", new SigninRequest());
        return "signin";
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signupRequest", new SignupRequest());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(
        @Valid @ModelAttribute("signupRequest") SignupRequest form,
        BindingResult bindingResult,
        Model model) {

        if (bindingResult.hasErrors()) {
            return "signup";
        }

        memberService.signup(form.toDto(), Role.ROLE_USER);
        return "redirect:/";
    }

    @GetMapping("/find-password")
    public String findPassword(Model model){
        model.addAttribute("findPasswordRequest", new FindPasswordRequest());
        return "find_password";
    }

    @PostMapping("/find-password")
    public String findPassword(
        @Valid @ModelAttribute("findPasswordRequest") FindPasswordRequest form,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "find_password";
        }
        try {
            memberService.processFindPassword(form.getEmail());
            String msg = URLEncoder.encode("임시 비밀번호가 이메일로 발송되었습니다.", StandardCharsets.UTF_8);
            return "redirect:/member/signin?msg=" + msg;
        } catch (Exception e) {
            model.addAttribute("errorMsg", "등록되지 않은 메일입니다");
            return "find_password";
        }
    }

}
