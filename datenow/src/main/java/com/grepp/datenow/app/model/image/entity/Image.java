package com.grepp.datenow.app.model.image.entity;

import com.grepp.datenow.app.model.course.entity.EditorCourse;
import com.grepp.datenow.app.model.course.entity.RecommendCourse;
import com.grepp.datenow.infra.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommend_course_id",nullable = true)
    private RecommendCourse recommendCourse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editor_course_id",nullable = true)
    private EditorCourse editorCourseId;

    @Column(nullable = false, length = 255)
    private String originFileName;

    @Column(nullable = false, length = 255)
    private String renameFileName;

    @Column(nullable = false, length = 255)
    private String savePath;

    @Column(length = 255)
    private String type;

}
