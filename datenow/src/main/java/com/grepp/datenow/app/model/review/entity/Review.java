package com.grepp.datenow.app.model.review.entity;

import com.grepp.datenow.app.model.course.entity.RecommendCourse;
import com.grepp.datenow.app.model.member.entity.Member;
import com.grepp.datenow.infra.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recommend_course_id", nullable = false)
    private RecommendCourse recommendCourseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Member id;

    private String content;
    private int star;

    public Review(RecommendCourse recommendCourseId, Member id,String content,
        int star) {
        this.content = content;
        this.id = id;
        this.recommendCourseId = recommendCourseId;
        this.star = star;
    }
}
