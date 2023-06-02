package com.rest.basic.repository;

import com.rest.basic.model.ElasticArtist;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ElasticArtistRepository extends ElasticsearchRepository<ElasticArtist,String> {

    List<ElasticArtist> findByName(@Param("name") String name);
    List<ElasticArtist> findByGenresContaining(String genre);

    Optional<ElasticArtist> findById(@Param("id") String id);

    ElasticArtist save(ElasticArtist artist);
}

