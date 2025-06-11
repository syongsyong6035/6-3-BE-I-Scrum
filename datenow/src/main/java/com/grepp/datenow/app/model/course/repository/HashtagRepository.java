package com.grepp.datenow.app.model.course.repository;

import com.grepp.datenow.app.model.course.entity.Hashtag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    Optional<Hashtag> findByTagName(String tagName);

}
