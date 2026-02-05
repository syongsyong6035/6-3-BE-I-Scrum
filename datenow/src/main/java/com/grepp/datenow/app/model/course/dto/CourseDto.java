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
  private List<String> imageUrl;
  private int favoriteCnt;
  private int reviewCnt;
  private List<String> hashtagNames;

  public CourseDto(RecommendCourse course) {
    this.courseId = course.getRecommendCourseId();
    this.title = course.getCourse().getTitle();
    this.description = course.getCourse().getDescription();
    this.creatorNickname = course.getId().getNickname();
    this.imageUrl = course.getCourseImages().stream().map(ci-> ci.getSavePath()).toList();
    this.favoriteCnt = course.getFavoriteCount();
    this.reviewCnt = course.getReviewCount();
    this.hashtagNames = course.getCourseHashtags().stream().map(ch -> ch.getHashtag().getTagName()).toList();
  }


  public CourseDto(RecommendCourse course, String imageUrl, int count, int reviewCnt) {

    this.courseId = course.getRecommendCourseId();
    this.title = course.getCourse().getTitle();
    this.description = course.getCourse().getDescription();
    this.creatorNickname = course.getId().getNickname();
    this.favoriteCnt = count;
    this.reviewCnt = reviewCnt;

  }
}

