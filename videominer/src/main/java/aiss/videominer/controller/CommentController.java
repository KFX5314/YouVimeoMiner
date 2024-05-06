package aiss.videominer.controller;

import aiss.videominer.model.Comment;
import aiss.videominer.model.Video;
import aiss.videominer.repository.CommentRepository;
import aiss.videominer.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videominer")
public class CommentController {

    @Autowired
    CommentRepository repository;
    @Autowired
    VideoRepository videoRepository;

    // GET http://localhost:8080/videominer/comments
    @GetMapping("/comments")
    public List<Comment> findAll() { return repository.findAll(); }

    // GET http://localhost:8080/videominer/comments/{id}
    @GetMapping("/comments/{id}")
    public Comment findOne(@PathVariable String id) {
        return repository.findById(id).orElseThrow();
    }

    // GET http://localhost:8080/videominer/videos/{id}/comments
    @GetMapping("/videos/{id}/comments")
    public List<Comment> findByVideo(@PathVariable String id) {
        return videoRepository.findById(id).orElseThrow().getComments();
    }
}
