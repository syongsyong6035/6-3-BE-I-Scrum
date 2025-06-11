package com.grepp.datenow.app.model.review.dto;

import com.grepp.datenow.app.model.review.entity.Review;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ResponseReviewDto {
  private Long reviewId;
  private String nickname;
  private String content;
  private int star;
  private LocalDateTime createdAt;

  public ResponseReviewDto(Review review){
    this.reviewId = review.getReviewId();
    this.nickname = review.getId().getNickname();
    this.content = review.getContent();
    this.star = review.getStar();
    this.createdAt = review.getCreatedAt();
  }

}
