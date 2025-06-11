package com.grepp.datenow.app.model.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteRecommendRequestDto {

  private Long recommendId;
  private String userId;


}
