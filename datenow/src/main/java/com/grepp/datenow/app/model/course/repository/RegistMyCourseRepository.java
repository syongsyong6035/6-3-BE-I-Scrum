package com.grepp.datenow.app.model.course.repository;

import com.grepp.datenow.app.model.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistMyCourseRepository extends JpaRepository<Course, Long> {
}