package com.grepp.datenow.app.controller.api.recommend;

import com.grepp.datenow.app.model.recommend.service.RecommendAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
public class RecommendApiController {

    private final RecommendAiService recommendAiService;

    @PostMapping("/course")
    public ResponseEntity<String> recommendDateCourse(@RequestParam String mood) {
        String result = recommendAiService.recommend(mood).text();
        return ResponseEntity.ok(result);
    }
}
