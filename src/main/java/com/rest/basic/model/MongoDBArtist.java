package com.rest.basic.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "artists")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MongoDBArtist {
    @Id
    private String id;
    private String name;
    private int popularity;
    private List<String> genres;

    // Constructors, getters, and setters
    public MongoDBArtist() {
    }

    public MongoDBArtist(String id, String name, int popularity, List<String> genres) {
        this.id = id;
        this.name = name;
        this.popularity = popularity;
        this.genres = genres;
    }

    public MongoDBArtist(MongoDBArtist artistData) {
        this.id=artistData.getId();
        this.name=artistData.getName();
        this.popularity=artistData.getPopularity();
        this.genres=artistData.getGenres();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }
}
