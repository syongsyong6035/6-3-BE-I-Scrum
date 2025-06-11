package com.grepp.datenow.app.controller.web.recommend;

import com.grepp.datenow.app.model.recommend.code.Mood;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RecommendController {

    @GetMapping("/recommend-course")
    public String recommend(Model model) {
        model.addAttribute("moods", Mood.values());
        return "select_category_user_input";
    }
}
