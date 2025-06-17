package com.grepp.datenow.app.model.course.entity;

import com.grepp.datenow.app.model.member.entity.Member;
import com.grepp.datenow.infra.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Course extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coursesId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Member id;
    private String title;
    private String description;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CourseHashtag> courseHashtags = new ArrayList<>();

    // 양방향 연관관계 주입 메서드
    public void addCourseHashtag(Hashtag hashtag) {
        CourseHashtag courseHashtag = new CourseHashtag(this, hashtag);
        // Course 객체의 courseHashtags 리스트 컬렉션에 연결 테이블 객체를 추가
        this.courseHashtags.add(courseHashtag);
        // Hashtag 객체에도 연결 테이블 객체 추가해줌
        hashtag.getCourseHashtags().add(courseHashtag);
    }
}
