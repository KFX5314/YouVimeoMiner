package aiss.videominer.controller;

import aiss.videominer.exception.CommentNotFoundException;
import aiss.videominer.exception.UserNotFoundException;
import aiss.videominer.exception.VideoNotFoundException;
import aiss.videominer.model.Comment;
import aiss.videominer.model.User;
import aiss.videominer.model.Video;
import aiss.videominer.repository.CommentRepository;
import aiss.videominer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videominer")
public class UserController {

    @Autowired
    UserRepository repository;
    @Autowired
    CommentRepository commentRepository;

    // GET http://localhost:8080/videominer/users
    @GetMapping("/users")
    public List<User> findAll() { return repository.findAll(); }

    // GET http://localhost:8080/videominer/users/{id}
    @GetMapping("/users/{id}")
    public User findOne(@PathVariable String id) throws UserNotFoundException {
        Optional<User> user = repository.findById(id);
        if(user.isEmpty()) {
            throw new UserNotFoundException();
        }
        return user.get();
    }

    // GET http://localhost:8080/videominer/comments/{id}/author
    @GetMapping("/comments/{id}/user")
    public User findAuthor(@PathVariable String id) throws CommentNotFoundException {
        Optional<Comment> comment = commentRepository.findById(id);
        if(comment.isEmpty()) {
            throw new CommentNotFoundException();
        }
        return comment.get().getAuthor();
    }
}
