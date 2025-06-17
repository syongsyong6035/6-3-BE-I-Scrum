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
    @JoinColumn(name = "course_id") // Course 엔티티의 기본 키(coursesId)를 참조
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id") // Hashtag 엔티티의 기본 키(tagId)를 참조
    private Hashtag hashtag;

    // 생성자 (양방향 연관관계 주입 시에 사용할 거임)
    public CourseHashtag(Course course, Hashtag hashtag) {
        this.course = course;
        this.hashtag = hashtag;
    }

    // 필수: equals()와 hashCode() override
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseHashtag that = (CourseHashtag) o;
        return Objects.equals(course, that.course) &&
            Objects.equals(hashtag, that.hashtag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, hashtag);
    }
}