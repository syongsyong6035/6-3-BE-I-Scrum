package com.grepp.datenow.app.model.course.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.grepp.datenow.app.model.place.dto.PlaceDetailDto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class CourseDetailDto {
  Long coursesId;
  String title;
  String nickname;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  LocalDateTime createdAt;
  String description;
  List<String> imageUrl;
  List<PlaceDetailDto> places;
  List<String> hashtagNames;

}
