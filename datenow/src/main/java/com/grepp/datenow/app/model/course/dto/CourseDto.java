package com.grepp.datenow.app.model.course.dto;

import com.grepp.datenow.app.model.course.entity.RecommendCourse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CourseDto {
  private Long courseId;
  private String title;
  private String description;
  private String creatorNickname;
  private String imageUrl;
  private int favoriteCnt;
  private int reviewCnt;
  private List<String> hashtagNames;

  public CourseDto(RecommendCourse course,String imageUrl,int likeCnt, int reviewCnt) {
    this.courseId = course.getRecommendCourseId();
    this.title = course.getCourseId().getTitle();
    this.description = course.getCourseId().getDescription();
    this.creatorNickname = course.getCourseId().getId().getNickname();
    this.imageUrl = imageUrl;
    this.favoriteCnt = likeCnt;
    this.reviewCnt = reviewCnt;
  }

  public CourseDto(RecommendCourse course) {
    this.courseId = course.getRecommendCourseId();
    this.title = course.getCourseId().getTitle();
    this.description = course.getCourseId().getDescription();
    this.creatorNickname = course.getCourseId().getId().getNickname();
  }



}

