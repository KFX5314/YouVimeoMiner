package aiss.videominer.controller;

import aiss.videominer.exception.CommentNotFoundException;
import aiss.videominer.exception.VideoNotFoundException;
import aiss.videominer.model.Comment;
import aiss.videominer.model.Video;
import aiss.videominer.repository.CommentRepository;
import aiss.videominer.repository.VideoRepository;
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

@Tag(name = "Comment", description = "Comment management API")
@RestController
@RequestMapping("/videominer")
public class CommentController {

    @Autowired
    CommentRepository repository;
    @Autowired
    VideoRepository videoRepository;

    // GET http://localhost:8080/videominer/comments
    @Operation(
            summary = "Retrieve all comments",
            description = "Get all comments present on the database",
            tags = {"comments", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of comments",
                    content = {
                            @Content(schema = @Schema(implementation = Comment.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request"
            )
    })
    @GetMapping("/comments")
    public List<Comment> findAll() {
        return repository.findAll();
    }

    // GET http://localhost:8080/videominer/comments/{id}
    @Operation(
            summary = "Retrieve a Comment by Id",
            description = "Get a Comment object by specifying its id",
            tags = {"comments", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Retrieved comment",
                    content = {
                            @Content(schema = @Schema(implementation = Comment.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Comment not found",
                    content = {
                            @Content(schema = @Schema())
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request"
            )
    })
    @GetMapping("/comments/{id}")
    public Comment findOne(
            @Parameter(description = "id of the comment to be searched") @PathVariable String id
    ) throws CommentNotFoundException {
        Optional<Comment> comment = repository.findById(id);

        if (comment.isEmpty()) {
            throw new CommentNotFoundException();
        }

        return comment.get();
    }

    // GET http://localhost:8080/videominer/videos/{id}/comments
    @Operation(
            summary = "Retrieve all comments of a video by its Id",
            description = "Get a all comments of a video by specifying the id of the Video",
            tags = {"comments", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Retrieved comment",
                    content = {
                            @Content(schema = @Schema(implementation = Comment.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Video not found",
                    content = {
                            @Content(schema = @Schema())
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request"
            )
    })
    @GetMapping("/videos/{id}/comments")
    public List<Comment> findByVideo(
            @Parameter(description = "id of the video to retrieve comments from") @PathVariable String id
    ) throws VideoNotFoundException {
        Optional<Video> video = videoRepository.findById(id);

        if (video.isEmpty()) {
            throw new VideoNotFoundException();
        }

        return video.get().getComments();
    }
}
