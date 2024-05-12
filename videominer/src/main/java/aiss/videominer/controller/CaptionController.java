package aiss.videominer.controller;

import aiss.videominer.exception.CaptionNotFoundException;
import aiss.videominer.exception.VideoNotFoundException;
import aiss.videominer.model.Caption;
import aiss.videominer.model.Video;
import aiss.videominer.repository.CaptionRepository;
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

@Tag(name = "Caption", description = "Caption management API")
@RestController
@RequestMapping("/videominer")
public class CaptionController {

    @Autowired
    CaptionRepository repository;
    @Autowired
    VideoRepository videoRepository;

    // GET http://localhost:8080/videominer/captions
    @Operation(
            summary = "Retrieve all captions",
            description = "Get all captions present on the database",
            tags = {"captions", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of captions",
                    content = {
                            @Content(schema = @Schema(implementation = Caption.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request"
            )
    })
    @GetMapping("/captions")
    public List<Caption> findAll() {
        return repository.findAll();
    }

    // GET http://localhost:8080/videominer/captions/{id}
    @Operation(
            summary = "Retrieve a Caption by Id",
            description = "Get a Caption object by specifying its id",
            tags = {"captions", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Retrieved caption",
                    content = {
                            @Content(schema = @Schema(implementation = Caption.class), mediaType = "application/json")
                    }),
            @ApiResponse(
                    responseCode = "404",
                    description = "Caption not found",
                    content = {
                            @Content(schema = @Schema())
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = {
                            @Content(schema = @Schema())
                    }
            )
    })
    @GetMapping("/captions/{id}")
    public Caption findOne(
            @Parameter(description = "id of the caption to be searched") @PathVariable String id
    ) throws CaptionNotFoundException {
        Optional<Caption> caption = repository.findById(id);

        if (caption.isEmpty()) {
            throw new CaptionNotFoundException();
        }

        return caption.get();
    }

    // GET http://localhost:8080/videominer/videos/{id}/captions
    @Operation(
            summary = "Retrieve all captions of a video by its Id",
            description = "Get a all captions of a video by specifying the id of the Video",
            tags = {"captions", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Retrieved caption",
                    content = {
                            @Content(schema = @Schema(implementation = Caption.class), mediaType = "application/json")
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
    @GetMapping("/videos/{id}/captions")
    public List<Caption> findByVideo(
            @Parameter(description = "id of the video to retrieve captions from") @PathVariable String id
    ) throws VideoNotFoundException {
        Optional<Video> video = videoRepository.findById(id);

        if (video.isEmpty()) {
            throw new VideoNotFoundException();
        }

        return video.get().getCaptions();
    }
}
