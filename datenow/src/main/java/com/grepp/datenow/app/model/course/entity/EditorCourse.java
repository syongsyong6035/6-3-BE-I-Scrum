package com.grepp.datenow.app.model.course.entity;

import com.grepp.datenow.app.model.image.entity.Image;
import com.grepp.datenow.app.model.member.entity.Member;
import com.grepp.datenow.infra.entity.BaseEntity;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class EditorCourse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long editorCourseId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Member member;
    private String title;

    @Lob
    private String description;

    @OneToMany(mappedBy = "editorCourseId", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Image> courseImages = new ArrayList<>();


}
