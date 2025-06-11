package com.grepp.datenow.app.model.place.entity;

import com.grepp.datenow.app.model.course.entity.Course;
import com.grepp.datenow.app.model.course.entity.EditorCourse;
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
@Getter @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Place extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "course_id", nullable = true)
    private Course courseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editor_course_id",nullable = true)
    private EditorCourse editorCourseId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "region_id")
//    private Region regionId;

    @Column(nullable = false, length = 255)
    private String placeName;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = true, length = 255)
    private String placeUrl;

    private double latitude;
    private double longitude;
}
