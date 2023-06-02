package com.rest.basic.repository;

import com.rest.basic.model.MongoDBArtist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MongoDBArtistRepository extends MongoRepository<MongoDBArtist, String> {
    // Additional custom query methods can be defined here if needed
    List<MongoDBArtist> findByName(@Param("name") String name);
    List<MongoDBArtist> findByGenresContaining(String genre);

    Optional<MongoDBArtist> findById(@Param("id") String id);

    MongoDBArtist save(MongoDBArtist artist);
}
