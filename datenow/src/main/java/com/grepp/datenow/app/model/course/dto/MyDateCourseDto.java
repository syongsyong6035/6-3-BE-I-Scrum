package com.grepp.datenow.app.model.course.dto;

import com.grepp.datenow.app.model.place.dto.PlaceSaveDto;
import java.util.List;

public record MyDateCourseDto(
    String title,
    String description,
    String date,
    List<PlaceSaveDto> places,
    List<String> hashtagNames
) {


}
