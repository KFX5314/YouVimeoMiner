package aiss.videominer.controller;

import aiss.videominer.exception.ChannelNotFoundException;
import aiss.videominer.exception.VideoNotFoundException;
import aiss.videominer.model.Channel;
import aiss.videominer.model.Video;
import aiss.videominer.repository.ChannelRepository;
import aiss.videominer.repository.VideoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Video", description = "Video management API")
@RestController
@RequestMapping("/videominer")
public class VideoController {

    @Autowired
    VideoRepository repository;
    @Autowired
    ChannelRepository channelRepository;

    // GET http://localhost:8080/videominer/videos
    @Operation(
            summary = "Retrieve all videos",
            description = "Get all videos present on the database. Filtering by name, sorting and pagination is possible",
            tags = {"videos", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of videos",
                    content = {
                            @Content(schema = @Schema(implementation = Video.class), mediaType = "application/json")
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
    @GetMapping("/videos")
    public List<Video> findAll(
            @Parameter(description = "page number for pagination. Default is 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "number of items per page. Default is 10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "name of the video to be searched") @RequestParam(required = false) String name,
            @Parameter(description = "property to sort by. Prefix with '-' for descending order") @RequestParam(required = false) String order
    ) {
        Pageable paging;

        if (order != null) {
            Sort.Direction direction = order.startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC;
            String property = order.startsWith("-") ? order.substring(1) : order;
            paging = PageRequest.of(page, size, Sort.by(direction, property));
        } else {
            paging = PageRequest.of(page, size);
        }

        Page<Video> pageVideos;
        if (name != null) {
            pageVideos = repository.findByName(name, paging);
        } else {
            pageVideos = repository.findAll(paging);
        }

        return pageVideos.getContent();
    }

    // GET http://localhost:8080/videominer/videos/{id}
    @Operation(
            summary = "Retrieve a Video by Id",
            description = "Get a Video object by specifying its id",
            tags = {"videos", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Retrieved video",
                    content = {
                            @Content(schema = @Schema(implementation = Video.class), mediaType = "application/json")
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
                    description = "Bad Request",
                    content = {
                            @Content(schema = @Schema())
                    }
            )
    })
    @GetMapping("/videos/{id}")
    public Video findOne(
            @Parameter(description = "id of the video to be searched") @PathVariable String id
    ) throws VideoNotFoundException {
        Optional<Video> video = repository.findById(id);

        if (video.isEmpty()) {
            throw new VideoNotFoundException();
        }

        return video.get();
    }

    // GET http://localhost:8080/videominer/channels/{id}/videos
    @Operation(
            summary = "Retrieve all videos of a Channel by its Id",
            description = "Get a all videos of a channel by specifying the id of the Channel",
            tags = {"videos", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Retrieved video",
                    content = {
                            @Content(schema = @Schema(implementation = Video.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Channel not found",
                    content = {
                            @Content(schema = @Schema())
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request"
            )
    })
    @GetMapping("/channels/{id}/videos")
    public List<Video> findByChannel(
            @Parameter(description = "id of the channel to retrieve videos from") @PathVariable String id
    ) throws ChannelNotFoundException {
        Optional<Channel> channel = channelRepository.findById(id);

        if (channel.isEmpty()) {
            throw new ChannelNotFoundException();
        }

        return channel.get().getVideos();
    }

    // PUT http://localhost:8080/videominer/videos/{id}
    @Operation(
            summary = "Update a video",
            description = "Updates the content of the video whose id is given",
            tags = {"videos", "put"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Video updated",
                    content = {
                            @Content(schema = @Schema())
                    }),
            @ApiResponse(
                    responseCode = "404",
                    description = "Video not found",
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/videos/{id}")
    public void update(
            @Parameter(description = "updated video") @Valid @RequestBody Video newVideo,
            @Parameter(description = "id of video to be updated") @PathVariable String id
    ) throws VideoNotFoundException {
        Optional<Video> oldVideo = repository.findById(id);

        if (oldVideo.isEmpty()) {
            throw new VideoNotFoundException();
        }

        Video video = oldVideo.get();
        video.setName(newVideo.getName());
        video.setDescription(newVideo.getDescription());
        video.setComments(newVideo.getComments());
        video.setCaptions(newVideo.getCaptions());

        repository.save(video);
    }

    // DELETE http://localhost:8080/videominer/videos/{id}
    @Operation(
            summary = "Delete a video",
            description = "Deletes the video whose id is given",
            tags = {"videos", "delete"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Video deleted",
                    content = {
                            @Content(schema = @Schema())
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
                    description = "Bad Request",
                    content = {
                            @Content(schema = @Schema())
                    }
            )
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/videos/{id}")
    public void delete(
            @Parameter(description = "id of video to be deleted") @PathVariable String id
    ) throws VideoNotFoundException {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new VideoNotFoundException();
        }
    }
}
