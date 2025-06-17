package com.grepp.datenow.app.model.course.entity;

import com.grepp.datenow.infra.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Hashtag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId; // 해시태그 고유 ID

    @Column(unique = true, nullable = false, length = 100)
    private String tagName;
    @OneToMany(mappedBy = "hashtag")
    private List<CourseHashtag> courseHashtags = new ArrayList<>();
}
