package com.grepp.datenow.app.model.like.repository;

import com.grepp.datenow.app.model.course.entity.EditorCourse;
import com.grepp.datenow.app.model.course.entity.RecommendCourse;
import com.grepp.datenow.app.model.like.entity.FavoriteCourse;
import com.grepp.datenow.app.model.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteCourse,Long> {

  Optional<FavoriteCourse> findByMemberAndRecommendCourse(Member member, RecommendCourse recommendCourse);

  Optional<FavoriteCourse> findByMemberAndEditorCourse(Member member, EditorCourse editorCourse);

  boolean existsByMemberAndRecommendCourseAndActivatedTrue(Member member, RecommendCourse recommendCourse);

  boolean existsByMemberAndEditorCourseAndActivatedTrue(Member member, EditorCourse editorCourse);

  int countByEditorCourse(EditorCourse course);

  int countByEditorCourseAndActivatedTrue(EditorCourse course);

  int countByRecommendCourseAndActivatedTrue(RecommendCourse course);

    @Query("SELECT f FROM FavoriteCourse f " +
        "LEFT JOIN FETCH f.recommendCourse rc " +
        "LEFT JOIN FETCH rc.course c " +
        "LEFT JOIN FETCH f.editorCourse ec " +
        "WHERE f.member.id = :memberId AND f.activated = true")
    List<FavoriteCourse> findByMemberId(Integer memberId);
}
