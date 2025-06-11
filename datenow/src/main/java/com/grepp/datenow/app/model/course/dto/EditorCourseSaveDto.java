package com.grepp.datenow.app.model.course.dto;

import com.grepp.datenow.app.model.place.dto.PlaceSaveDto;
import java.util.List;

public record EditorCourseSaveDto(
    String title,
    String description,
    List<PlaceSaveDto> places,
    List<String> imageUrls
) {



}
