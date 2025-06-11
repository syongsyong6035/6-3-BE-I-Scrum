package com.grepp.datenow.app.model.recommend.code;

import java.util.List;
import java.util.stream.Collectors;

public enum Mood {

    ROMANTIC("로맨틱한"),
    COZY("아늑한"),
    TRENDY("트렌디한"),
    COMFORTABLE("편안한"),
    QUIET("조용한"),
    LIVELY("활기찬"),
    LUXURIOUS("고급스러운"),
    UNIQUE("독특한");

    private final String description;

    Mood(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static String joinDescriptions(List<Mood> moods) {
        return moods.stream()
            .map(Mood::getDescription)
            .collect(Collectors.joining(", "));
    }
}
