package com.grepp.datenow.app.model.course.dto;

import com.grepp.datenow.app.model.course.entity.RecommendCourse;
import lombok.Getter;

@Getter

public class CourseDto {
  private Long courseId;
  private String title;
  private String description;
  private String creatorNickname;
  private String imageurl;
  private int favoriteCnt;
  private int reviewCnt;

  public CourseDto(RecommendCourse course,String imageurl,int likeCnt, int reviewCnt) {
    this.courseId = course.getRecommendCourseId();
    this.title = course.getCourseId().getTitle();
    this.description = course.getCourseId().getDescription();
    this.creatorNickname = course.getCourseId().getId().getNickname();
    this.imageurl = imageurl;
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

