package aiss.vimeominer.service;

import aiss.vimeominer.model.Comment;
import aiss.vimeominer.model.CommentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
public class CommentService {

    @Autowired
    RestTemplate restTemplate;

    String url = "https://api.vimeo.com/videos";
    //https://api.vimeo.com/videos/941713443/comments

    //Este es el token que he generado yo
    String token = "3f762ed847d6f0ab92886c953e5481de";

    public List<Comment> findAllComments(String id, Integer numComments) {
        String uri = url + "/" + id + "/comments?page=1&per_page="+numComments;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<CommentList> request = new HttpEntity<>(null, headers);
        ResponseEntity<CommentList> response = restTemplate.exchange(uri, HttpMethod.GET, request, CommentList.class);

        return Objects.requireNonNull(response.getBody()).getData();
    }

    public Comment findComment(String id, String id2) {
        String uri = url + "/" + id + "/comments/" + id2;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Comment> request = new HttpEntity<>(null, headers);
        ResponseEntity<Comment> response = restTemplate.exchange(uri, HttpMethod.GET, request, Comment.class);

        return response.getBody();
    }
}
