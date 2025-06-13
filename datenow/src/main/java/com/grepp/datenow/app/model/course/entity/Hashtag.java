package com.grepp.datenow.app.model.course.entity;

import com.grepp.datenow.infra.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Hashtag extends BaseEntity { // BaseEntity는 생성/수정 시간을 관리한다고 가정
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId; // 해시태그 고유 ID

    // 해시태그 이름은 중복되면 안 되고, 필수 값입니다.
    @Column(unique = true, nullable = false, length = 100)
    private String tagName; // 해시태그 이름 (예: '강남데이트', '혼밥추천')

    @OneToMany(mappedBy = "hashtag")
    private List<CourseHashtag> courseHashtags = new ArrayList<>();
}
