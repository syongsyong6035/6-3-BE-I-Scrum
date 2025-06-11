package com.grepp.datenow.app.model.course.repository;

import com.grepp.datenow.app.model.course.entity.RecommendCourse;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<RecommendCourse,Long> {

  @Query("""
    SELECT rc FROM RecommendCourse rc
    JOIN FETCH rc.courseId c
    JOIN FETCH c.id
    ORDER BY rc.createdAt DESC
""")
  List<RecommendCourse> findTop4ByOrderByCreatedAtDesc(Pageable pageable);
    // 코스 저장
    static void save(){}
  @Query("""
    SELECT rc FROM RecommendCourse rc
    JOIN FETCH rc.courseId c
    JOIN FETCH c.id
    ORDER BY c.createdAt DESC
""")
  List<RecommendCourse> findAllWithCourseAndMember();


  @Query("""
  SELECT rc FROM RecommendCourse rc
  JOIN FETCH rc.courseId c
  JOIN FETCH c.id
  WHERE rc.recommendCourseId = :id
""")
  Optional<RecommendCourse> findWithCourseAndMemberById(@Param("id") Long id);
}
