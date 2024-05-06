package aiss.videominer.controller;

import aiss.videominer.model.User;
import aiss.videominer.repository.CommentRepository;
import aiss.videominer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public User findOne(@PathVariable String id) {
        return repository.findById(id).orElseThrow();
    }

    // GET http://localhost:8080/videominer/comments/{id}/user
    @GetMapping("/comments/{id}/user")
    public User findAuthor(@PathVariable String id) {
        return commentRepository.findById(id).orElseThrow().getAuthor();
    }
}
