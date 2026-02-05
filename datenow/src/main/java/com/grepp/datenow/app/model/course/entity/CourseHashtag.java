package com.grepp.datenow.app.model.course.entity;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseHashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 단일 기본 키 추가

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommend_course_id") // Course 엔티티의 기본 키(coursesId)를 참조
    private RecommendCourse recommendCourse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id") // Hashtag 엔티티의 기본 키(tagId)를 참조
    private Hashtag hashtag;

    // 생성자 (양방향 연관관계 주입 시에 사용할 거임)
    public CourseHashtag(RecommendCourse recommendCourse, Hashtag hashtag) {
        this.recommendCourse = recommendCourse;
        this.hashtag = hashtag;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseHashtag that = (CourseHashtag) o;
        return Objects.equals(recommendCourse, that.recommendCourse) &&
            Objects.equals(hashtag, that.hashtag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recommendCourse, hashtag);
    }
}