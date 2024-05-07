package aiss.youtubeminer.service;


import aiss.youtubeminer.model.youtube.comment.Comment;
import aiss.youtubeminer.model.youtube.comment.CommentSearch;
import aiss.youtubeminer.model.youtube.videoSnippet.VideoSnippet;
import aiss.youtubeminer.model.youtube.videoSnippet.VideoSnippetSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService
{
    @Autowired
    private RestTemplate restTemplate;

    public List<Comment> getCommentsForVideo(String videoId, Integer maxComments, String APIKEY) {
        String baseURI = "https://www.googleapis.com/youtube/v3/commentThreads";
        String uri = baseURI + "?part=snippet&videoId=" + videoId + "&maxResults=" + maxComments;

        //System.out.println(uri);
        //Create request
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-goog-api-key", APIKEY);
        HttpEntity<CommentSearch> request = new HttpEntity<>(headers);

        //Initialize empty Comment List
        List<Comment> comments = new ArrayList<>();

        try {
            // Send request and deserialize
            ResponseEntity<CommentSearch> response = restTemplate.exchange(uri, HttpMethod.GET, request, CommentSearch.class);
            CommentSearch commentSearch = response.getBody();

            // Extract Comment items from CommentSearch
            //Assert no null
            if (commentSearch != null && commentSearch.getItems() != null)
            {
                comments.addAll(commentSearch.getItems());
            }
        }
        catch(Exception e)
        {
            System.out.println("The author has disabled comments!");
        }

        return comments;
    }
}
