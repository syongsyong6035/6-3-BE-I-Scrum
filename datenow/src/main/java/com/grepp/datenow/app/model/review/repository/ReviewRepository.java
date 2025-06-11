package com.grepp.datenow.app.model.review.repository;

import com.grepp.datenow.app.model.course.entity.RecommendCourse;
import com.grepp.datenow.app.model.review.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

  int countByRecommendCourseIdAndActivatedTrue(RecommendCourse course);

  List<Review> findAllByRecommendCourseIdAndActivatedTrue(RecommendCourse recommendCourse);
}
