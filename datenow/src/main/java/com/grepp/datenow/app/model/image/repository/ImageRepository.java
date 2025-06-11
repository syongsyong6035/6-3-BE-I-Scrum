package com.grepp.datenow.app.model.image.repository;

import com.grepp.datenow.app.model.course.entity.Course;
import com.grepp.datenow.app.model.course.entity.EditorCourse;
import com.grepp.datenow.app.model.course.entity.RecommendCourse;
import com.grepp.datenow.app.model.image.entity.Image;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {

  Optional<Image> findFirstByEditorCourseId(EditorCourse course);

  Optional<Image> findFirstByRecommendCourseId(RecommendCourse recommendCourseId);

  List<Image> findAllByRecommendCourseId(RecommendCourse recommendCourse);

  List<Image> findAllByEditorCourseId(EditorCourse places);

  List<Image> findByRecommendCourseId_CourseId(Course course);
}
