package com.grepp.datenow.app.model.course.repository;

import com.grepp.datenow.app.model.course.entity.Course;
import com.grepp.datenow.app.model.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MyCourseRepository extends JpaRepository<Course, Long> {

    /**
     * 현재 구조에서 '내 코스'는 RecommendCourse 에 연결된 Course 를 의미하므로,
     * RecommendCourse.id(Member) 를 기준으로 조회한다.
     */
    @Query("""
        SELECT c
        FROM RecommendCourse rc
        JOIN rc.course c
        WHERE rc.id = :member
        ORDER BY c.createdAt DESC
        """)
    List<Course> findAllByMemberOrderByCreatedAtDesc(@Param("member") Member member);
}
