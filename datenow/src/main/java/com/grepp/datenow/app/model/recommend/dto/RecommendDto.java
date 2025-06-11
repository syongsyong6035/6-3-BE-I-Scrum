package com.grepp.datenow.app.model.recommend.dto;

import com.grepp.datenow.app.model.recommend.code.Mood;
import java.util.List;

public record RecommendDto(
    double lat,
    double lon,
    List<Mood> moods
) {}
