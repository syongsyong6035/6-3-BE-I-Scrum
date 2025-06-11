package com.grepp.datenow.app.model.like.dto;

public record FavoriteCourseResponse(
    Long favoriteCourseId,
    String nickname,
    Long recommendCourseId,
    Long editorCourseId,
    String title
) {}
