package com.grepp.datenow.app.model.recommend.code;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@JsonFormat(shape = Shape.STRING)
public enum Dong {
    압구정동, 신사동, 청담동, 논현동,
    삼성동, 역삼동

}

//압구정동, 신사동, 청담동, 논현동,
//삼성동, 역삼동, 대치동, 도곡동,
//개포동, 일원동, 수서동, 자곡동,
//세곡동, 율현동