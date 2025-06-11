package com.grepp.datenow.app.model.course.dto;

import com.grepp.datenow.app.model.course.entity.EditorCourse;
import lombok.Getter;

@Getter

public class EditorCourseDto {
  private Long courseId;
  private String title;
  private String description;
  private String editorNickname;
  private String imageurl;
  private int favoriteCnt;

  public EditorCourseDto(EditorCourse course,String imageurl,int favoriteCount) {
    this.courseId = course.getEditorCourseId();
    this.title = course.getTitle();
    this.description = course.getDescription();
    this.editorNickname = course.getMember().getNickname();
    this.imageurl = imageurl;
    this.favoriteCnt = favoriteCount;
  }




}
