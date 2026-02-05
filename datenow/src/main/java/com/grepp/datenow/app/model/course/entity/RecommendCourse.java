package com.grepp.datenow.app.model.course.entity;

import com.grepp.datenow.app.model.image.entity.Image;
import com.grepp.datenow.app.model.like.entity.FavoriteCourse;
import com.grepp.datenow.app.model.member.entity.Member;
import com.grepp.datenow.app.model.review.entity.Review;
import com.grepp.datenow.infra.entity.BaseEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendCourse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendCourseId;

    @OneToMany(mappedBy = "recommendCourse", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Image> courseImages = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courses_id")
    private Course course;

    @OneToMany(mappedBy = "recommendCourse", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CourseHashtag> courseHashtags = new ArrayList<>();

    // RecommendCourse.java
    @Column(columnDefinition = "integer default 0")
    private int favoriteCount;

    @Column(columnDefinition = "integer default 0")
    private int reviewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member id;

    // 양방향 연관관계 주입 메서드
    public void addCourseHashtag(Hashtag hashtag) {
        CourseHashtag courseHashtag = new CourseHashtag(this, hashtag);
        // Course 객체의 courseHashtags 리스트 컬렉션에 연결 테이블 객체를 추가
        this.courseHashtags.add(courseHashtag);
        // Hashtag 객체에도 연결 테이블 객체 추가해줌
        hashtag.getCourseHashtags().add(courseHashtag);
    }

}






