package com.grepp.datenow.app.model.course.repository.custom;

import com.grepp.datenow.app.model.course.entity.Course;
import com.grepp.datenow.app.model.course.entity.RecommendCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecommendCourseRepository extends JpaRepository<RecommendCourse, Long>,
    RecommendCourseRepositoryCustom {
    boolean existsByCourse(Course course);
    Optional<RecommendCourse> findByCourse(Course course);

    @Query("""
    SELECT fc.recommendCourse
    FROM FavoriteCourse fc
    WHERE fc.activated = TRUE 
    GROUP BY fc.recommendCourse
    ORDER BY COUNT(fc.id) DESC
    LIMIT 1
    """)
    RecommendCourse findTopLikedRecommendCourse();
}
