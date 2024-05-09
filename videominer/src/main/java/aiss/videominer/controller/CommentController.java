package aiss.videominer.controller;

import aiss.videominer.exception.CommentNotFoundException;
import aiss.videominer.exception.VideoNotFoundException;
import aiss.videominer.model.Comment;
import aiss.videominer.model.Video;
import aiss.videominer.repository.CommentRepository;
import aiss.videominer.repository.VideoRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
@Tag(name = "Comment", description = "Comment management API")
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
    public Comment findOne(@PathVariable String id) throws CommentNotFoundException {
        Optional<Comment> comment = repository.findById(id);
        if(comment.isEmpty()) {
            throw new CommentNotFoundException();
        }
        return comment.get();
    }

    // GET http://localhost:8080/videominer/videos/{id}/comments
    @GetMapping("/videos/{id}/comments")
    public List<Comment> findByVideo(@PathVariable String id) throws VideoNotFoundException {
        Optional<Video> video = videoRepository.findById(id);
        if(video.isEmpty()) {
            throw new VideoNotFoundException();
        }
        return video.get().getComments();
    }
}
