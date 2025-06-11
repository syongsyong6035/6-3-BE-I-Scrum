package com.grepp.datenow.app.model.place.repository;

import com.grepp.datenow.app.model.place.document.HotPlaceEmbedding;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HotPlaceEmbeddingRepository extends MongoRepository<HotPlaceEmbedding, String> {

}
