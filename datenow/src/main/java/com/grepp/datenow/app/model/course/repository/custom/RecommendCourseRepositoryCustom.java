package com.grepp.datenow.app.model.course.repository.custom;

import com.grepp.datenow.app.model.course.entity.RecommendCourse;
import java.util.List;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * RecommendCourse 에 대한 커스텀 조회 메서드 정의용 인터페이스.
 * 구현 클래스 이름은 반드시 'RecommendCourseRepositoryImpl' 이어야
 * Spring Data JPA 가 자동으로 연결합니다.
 */
public interface RecommendCourseRepositoryCustom {

  Page<RecommendCourse> findAllCourseWithHashtags(List<String> hashtagNames,int page, int size);

  Page<RecommendCourse> findAllWithCourseAndMember(int page, int size);
}
