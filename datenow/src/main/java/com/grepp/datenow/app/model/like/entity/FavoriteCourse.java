package com.grepp.datenow.app.model.like.entity;

import com.grepp.datenow.app.model.course.entity.EditorCourse;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteCourse extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteCourseId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "recommend_course_id", nullable = true)
    private RecommendCourse recommendCourse;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "editor_course_id", nullable = true)
    private EditorCourse editorCourse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Member member;


}
