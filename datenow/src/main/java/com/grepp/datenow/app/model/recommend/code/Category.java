package com.grepp.datenow.app.model.recommend.code;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@JsonFormat(shape = Shape.STRING)
public enum Category {
    카페_디저트, 걷기, 놀거리, 호캉스, 맛집, 쇼핑, 영화_전시

}
