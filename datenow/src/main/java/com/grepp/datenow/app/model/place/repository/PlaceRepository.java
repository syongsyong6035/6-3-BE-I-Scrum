package com.grepp.datenow.app.model.place.repository;

import com.grepp.datenow.app.model.course.entity.Course;
import com.grepp.datenow.app.model.course.entity.EditorCourse;
import com.grepp.datenow.app.model.place.entity.Place;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place,Long> {



  List<Place> findAllByCourseId(Course course);

  List<Place> findAllByEditorCourseId(EditorCourse editorCourse);

  List<Place> findByCourseId(Course course);
}
