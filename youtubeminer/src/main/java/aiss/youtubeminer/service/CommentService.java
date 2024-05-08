package aiss.youtubeminer.service;


import aiss.youtubeminer.model.youtube.comment.Comment;
import aiss.youtubeminer.model.youtube.comment.CommentSearch;
import aiss.youtubeminer.model.youtube.videoSnippet.VideoSnippet;
import aiss.youtubeminer.model.youtube.videoSnippet.VideoSnippetSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    private final String API_KEY = "API_KEY";

    private final String BASE_URI = "https://www.googleapis.com/youtube/v3/commentThreads";

    @Autowired
    private RestTemplate restTemplate;

    public List<Comment> getCommentsForVideo(String videoId, Integer maxComments) {
        URI uri = UriComponentsBuilder.fromUriString(BASE_URI)
                .queryParam("part", "snippet")
                .queryParam("videoId", videoId)
                .queryParam("maxResults", maxComments.toString())
                .build()
                .toUri();

        // Create request
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-goog-api-key", API_KEY);
        HttpEntity<CommentSearch> request = new HttpEntity<>(headers);

        // Initialize empty Comment List
        List<Comment> comments = new ArrayList<>();

        try {
            // Send request and deserialize
            ResponseEntity<CommentSearch> response = restTemplate.exchange(uri, HttpMethod.GET, request, CommentSearch.class);
            CommentSearch commentSearch = response.getBody();

            // Extract Comment items from CommentSearch
            if (commentSearch != null && commentSearch.getItems() != null) {
                comments.addAll(commentSearch.getItems());
            }
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode().isSameCodeAs(HttpStatus.FORBIDDEN)) {
                // The author has disabled comments
                System.out.println("The author has disabled comments!");
            } else {
                throw exception;
            }
        }

        return comments;
    }

    public List<Comment> getCommentsForVideo(String videoId) {
        return getCommentsForVideo(videoId, 10);
    }
}
