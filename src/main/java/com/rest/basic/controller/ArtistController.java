package com.rest.basic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.basic.model.ElasticArtist;
import com.rest.basic.model.MongoDBArtist;
import com.rest.basic.service.ArtistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("")
public class ArtistController {

    private final ArtistService artistService;
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping("/findArtistByGenre/elastic/{genre}")
    public ResponseEntity<List<ElasticArtist>> findSimilarArtistsByGenre(@PathVariable String genre) {
        try {
            List<ElasticArtist> similarArtists = artistService.findSimilarArtistsByGenre(genre);
            return ResponseEntity.ok(similarArtists);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/findArtistByGenre/mongodb/{genre}")
    public ResponseEntity<List<MongoDBArtist>> findSimilarArtistsByGenreMondoDB(@PathVariable String genre) {
        try{
            List<MongoDBArtist> similarArtists = artistService.findSimilarArtistsByGenreMondoDB(genre);
            return ResponseEntity.ok(similarArtists);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("storeArtist/{id}")
    public ResponseEntity<String> storeArtist(@PathVariable String id) throws IOException, InterruptedException {
        try {
            return artistService.processAndStoreSpotifyResponse(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/elastic/{id}")
    public ResponseEntity<Object> getArtist(@PathVariable String id) {
        try {
            Optional<ElasticArtist> artistOptional = artistService.getArtist(id);
            ElasticArtist artist = artistOptional.get();
            return ResponseEntity.ok(artist);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/mongodb/{id}")
    public ResponseEntity<Object> getArtistMongoDB(@PathVariable String id) {
        try {
            Optional<MongoDBArtist> artistOptional = artistService.getArtistMongoDB(id);
            MongoDBArtist artist = artistOptional.get();
            return ResponseEntity.ok(artist);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}