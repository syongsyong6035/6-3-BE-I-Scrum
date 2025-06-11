package com.grepp.datenow.app.model.course.repository;

import com.grepp.datenow.app.model.course.entity.EditorCourse;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminCourseRepository extends JpaRepository<EditorCourse,Long> {

  @Query("""
    SELECT e FROM EditorCourse e
    JOIN FETCH e.member
    WHERE e.activated = true
    ORDER BY e.createdAt DESC
""")
  List<EditorCourse> findTop4ByOrderByCreatedAtDesc(Pageable pageable);

  @Query("SELECT e FROM EditorCourse e JOIN FETCH e.member WHERE e.activated = true ORDER BY e.createdAt DESC")
  List<EditorCourse> findAllByActivatedTrue();

  @Query("SELECT e FROM EditorCourse e JOIN FETCH e.member WHERE e.editorCourseId = :id")
  Optional<EditorCourse> findWithMemberById(@Param("id") Long editorId);
}
