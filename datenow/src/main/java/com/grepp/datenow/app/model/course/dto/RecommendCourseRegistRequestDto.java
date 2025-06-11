package com.grepp.datenow.app.model.course.dto;

import java.util.List;
import lombok.Data;

@Data
public class RecommendCourseRegistRequestDto {
    private Long courseId;
    private List<String> imageUrls;
}
