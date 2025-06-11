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

    // ⭐ addCourseHashtag 편의 메서드 ⭐
    public void addCourseHashtag(Hashtag hashtag) {
        CourseHashtag courseHashtag = new CourseHashtag(this, hashtag);
        this.courseHashtags.add(courseHashtag);
        hashtag.getCourseHashtags().add(courseHashtag); // Hashtag 엔티티에도 해당 컬렉션 추가 필요
    }
}
