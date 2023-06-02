package com.rest.basic.service;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.basic.client.SpotifyApi;
import com.rest.basic.model.ElasticArtist;
import com.rest.basic.model.MongoDBArtist;
import com.rest.basic.repository.ElasticArtistRepository;
import com.rest.basic.repository.MongoDBArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {
    @Autowired
    private ElasticArtistRepository elasticArtistRepository;

    @Autowired
    private MongoDBArtistRepository mongoDBArtistRepository;
    @Value("${spotify.bearer_token}")
    private String bearer_token;
    @Value("${spotify.base_url}")
    private String base_url;
    @Autowired
    private ElasticsearchClient elasticsearchClient;
    public ArtistService(ElasticArtistRepository elasticArtistRepository) {
        this.elasticArtistRepository = elasticArtistRepository;
    }
    // Using Repository functions to findSimilarByGenre and getArtist
    public List<ElasticArtist> findSimilarArtistsByGenre(String genre) {
        System.out.println(genre);
        System.out.println(elasticArtistRepository.findByGenresContaining(genre));
        return elasticArtistRepository.findByGenresContaining(genre);
    }
    public List<MongoDBArtist> findSimilarArtistsByGenreMondoDB(String genre) {
        System.out.println(genre);
        System.out.println(mongoDBArtistRepository.findByGenresContaining(genre));
        return mongoDBArtistRepository.findByGenresContaining(genre);
    }
    public Optional<ElasticArtist> getArtist(String id) {
        return elasticArtistRepository.findById(id);
    }
    public Optional<MongoDBArtist> getArtistMongoDB(String id) {
        return mongoDBArtistRepository.findById(id);
    }
    public ElasticArtist addArtist(ElasticArtist artist) {
        return elasticArtistRepository.save(artist);
    }

    // Tried to build my own method for getArtistById but not required
    public ResponseEntity<ElasticArtist> getArtistById(String id) throws IOException {
        ElasticArtist artistNode = null;
        // Using core elasticsearch function to Query and read the document
        GetResponse<ElasticArtist> response = elasticsearchClient.get(g -> g
                        .index("artists")
                        .id(id),
                ElasticArtist.class
        );

        if (response.found()) {
            artistNode = response.source();
            assert artistNode != null;
            System.out.println("ElasticArtist name " + artistNode.getName() + artistNode.getId());
        } else {
            System.out.println("ElasticArtist not found");
        }
        // this is to convert the artistNode into json format properly
        return ResponseEntity.ok(artistNode);
    }
    public ResponseEntity<String> processAndStoreSpotifyResponse(String id) throws IOException, InterruptedException {
        String responseBody = SpotifyApi.runSpotifyApiClient(base_url,bearer_token,id);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ElasticArtist artistData = objectMapper.readValue(responseBody, ElasticArtist.class);
            MongoDBArtist mongoDBArtistData = objectMapper.readValue(responseBody,MongoDBArtist.class);
            mongoDBArtistData.setId(id);
            artistData.setId(id);
            elasticArtistRepository.save(artistData);
            mongoDBArtistRepository.save(mongoDBArtistData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(responseBody);
    }

}
