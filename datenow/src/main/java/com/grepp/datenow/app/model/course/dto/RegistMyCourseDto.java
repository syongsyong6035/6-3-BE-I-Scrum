package com.grepp.datenow.app.model.course.dto;

import com.grepp.datenow.app.model.member.entity.Member;
import com.grepp.datenow.app.model.place.dto.PlaceDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class RegistMyCourseDto {
    private Long courseId;
    private Boolean activated;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String description;
    private String title;
    private Member id;
    private List<PlaceDto> places;

}