package com.rest.basic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName="artists")
// there was one error with _class saying unknownProperty which didn't let the jackson deseriealize the object
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElasticArtist {

    // Id and id should be named appropriately
    @Id private String id;
    @Field(type = FieldType.Text,name="name")
    private String name;

    @Field(type = FieldType.Integer,name = "popularity")
    private Integer popularity;

    @Field(type = FieldType.Keyword,name="genres")
    private List<String> genres;

    //empty constructor is needed for the Jackson to deserialize properly
    public ElasticArtist(){

    }
    public ElasticArtist(String id,String name, List<String> genres, int popularity) {
        this.id=id;
        this.name=name;
        this.popularity=popularity;
        this.genres=genres;
    }

    public ElasticArtist(ElasticArtist artistData) {
        this.id=artistData.getId();
        this.name=artistData.getName();
        this.popularity=artistData.getPopularity();
        this.genres=artistData.getGenres();
    }

    public String getId(){ return this.id;}

    public void setId(String id){
        this.id=id;
    }
    public String getName(){
        return this.name;
    }
    public Integer getPopularity(){
        return this.popularity;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setPopularity(Integer popularity){
        this.popularity=popularity;
    }

    public List<String> getGenres(){
        return this.genres;
    }
    public void setGenres(List<String> genres){
        this.genres=genres;
    }

}
