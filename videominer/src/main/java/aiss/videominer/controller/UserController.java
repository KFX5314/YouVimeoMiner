package aiss.videominer.controller;

import aiss.videominer.exception.CommentNotFoundException;
import aiss.videominer.exception.UserNotFoundException;
import aiss.videominer.exception.VideoNotFoundException;
import aiss.videominer.model.Comment;
import aiss.videominer.model.User;
import aiss.videominer.model.Video;
import aiss.videominer.repository.CommentRepository;
import aiss.videominer.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Tag(name = "User", description = "User management API")
@RestController
@RequestMapping("/videominer")
public class UserController {

    @Autowired
    UserRepository repository;
    @Autowired
    CommentRepository commentRepository;

    // GET http://localhost:8080/videominer/users
    @Operation( summary = "Retrieve all users on the database",
            description = "Get all users present on the database",
            tags = { "users", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of users",
                    content = { @Content(schema = @Schema(implementation = User.class),
                            mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/users")
    public List<User> findAll() { return repository.findAll(); }

    // GET http://localhost:8080/videominer/users/{id}
    @Operation( summary = "Retrieve a User by Id",
            description = "Get a User object by specifying its id",
            tags = { "users", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retrieved user",
                    content = { @Content(schema = @Schema(implementation = User.class),
                            mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/users/{id}")
    public User findOne(@Parameter(description = "id of the user to be searched")
                            @PathVariable String id) throws UserNotFoundException {
        Optional<User> user = repository.findById(id);
        if(user.isEmpty()) {
            throw new UserNotFoundException();
        }
        return user.get();
    }

    // GET http://localhost:8080/videominer/comments/{id}/user
    @Operation( summary = "Retrieve the User of a Comment by its Id",
            description = "Get a User object by specifying the id of its Comment",
            tags = { "users", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retrieved user",
                    content = { @Content(schema = @Schema(implementation = User.class),
                            mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Comment not found",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/comments/{id}/user")
    public User findAuthor(@Parameter(description = "id of the comment to retrieve the user from")
                               @PathVariable String id) throws CommentNotFoundException {
        Optional<Comment> comment = commentRepository.findById(id);
        if(comment.isEmpty()) {
            throw new CommentNotFoundException();
        }
        return comment.get().getAuthor();
    }
}
