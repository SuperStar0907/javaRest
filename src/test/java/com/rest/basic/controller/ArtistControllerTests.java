package com.rest.basic.controller;

import com.rest.basic.model.ElasticArtist;
import com.rest.basic.service.ArtistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ArtistControllerTests {

    private MockMvc mockMvc;

    @Mock
    private ArtistService artistService;
    @InjectMocks
    private ArtistController artistController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(artistController).build();
    }

    @Test
    public void testFindSimilarArtistsByGenre() throws Exception {
        List<ElasticArtist> artists = Arrays.asList(
                new ElasticArtist("1", "Artist 1", Arrays.asList("Genre 1", "Genre 2"), 90),
                new ElasticArtist("2", "Artist 2", Arrays.asList("Genre 3", "Genre 4"), 85)
        );
        when(artistService.findSimilarArtistsByGenre(anyString())).thenReturn(artists);

        mockMvc.perform(get("/artist/similar/rock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Artist 1"))
                .andExpect(jsonPath("$[0].genres").isArray())
                .andExpect(jsonPath("$[0].genres[0]").value("Genre 1"))
                .andExpect(jsonPath("$[0].popularity").value(90))
                .andExpect(jsonPath("$[1].name").value("Artist 2"))
                .andExpect(jsonPath("$[1].genres").isArray())
                .andExpect(jsonPath("$[1].genres[1]").value("Genre 4"))
                .andExpect(jsonPath("$[1].popularity").value(85));

        verify(artistService, times(1)).findSimilarArtistsByGenre("rock");
    }

    @Test
    public void testStoreArtist_Success() throws IOException, InterruptedException {
        // Arrange
        String id = "123";
        String response = "Response body";

        when(artistService.processAndStoreSpotifyResponse(id)).thenReturn(ResponseEntity.ok(response));

        // Act
        ResponseEntity<String> result = artistController.storeArtist(id);

        // Assert
        verify(artistService, times(1)).processAndStoreSpotifyResponse(id);
        assert result != null;
        assert ((ResponseEntity<?>) result).getStatusCode() == HttpStatus.OK;
        assert Objects.equals(result.getBody(), response);
    }

    @Test
    public void testGetArtist() throws Exception {
        String id = "123";
        ElasticArtist artist = new ElasticArtist(id, "Artist 1", Arrays.asList("Genre 1", "Genre 2"), 90);
        when(artistService.getArtist(id)).thenReturn(Optional.of(artist));

        mockMvc.perform(get("/artist/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"123\",\"name\":\"Artist 1\",\"genres\":[\"Genre 1\",\"Genre 2\"],\"popularity\":90}"));

        verify(artistService, times(1)).getArtist(id);
    }

    @Test
    public void testGetArtist_NotFound() throws Exception {
        String id = "123";
        Optional<ElasticArtist> artist = Optional.empty();
        when(artistService.getArtist(id)).thenReturn(artist);

        mockMvc.perform(get("/artist/{id}", id))
                .andExpect(status().isInternalServerError());

        ElasticArtist artist2 = artistService.getArtist(id).orElse(null);
        assertNull(artist2);
    }

    @Test
    public void testGetArtistsByGenre() throws Exception {
        String genre = "rock";
        List<ElasticArtist> artists = Arrays.asList(
                new ElasticArtist("1", "Artist 1", Arrays.asList("Genre 1", "Genre 2"), 90),
                new ElasticArtist("2", "Artist 2", Arrays.asList("Genre 3", "Genre 4"), 85)
        );
        when(artistService.findSimilarArtistsByGenre(genre)).thenReturn(artists);

        mockMvc.perform(get("/artist/genre/{genre}", genre))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Artist 1"))
                .andExpect(jsonPath("$[0].genres").isArray())
                .andExpect(jsonPath("$[0].genres[0]").value("Genre 1"))
                .andExpect(jsonPath("$[0].popularity").value(90))
                .andExpect(jsonPath("$[1].name").value("Artist 2"))
                .andExpect(jsonPath("$[1].genres").isArray())
                .andExpect(jsonPath("$[1].genres[1]").value("Genre 4"))
                .andExpect(jsonPath("$[1].popularity").value(85));

        verify(artistService, times(1)).findSimilarArtistsByGenre(genre);
    }
}
