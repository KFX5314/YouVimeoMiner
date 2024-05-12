package aiss.videominer.controller;

import aiss.videominer.exception.ChannelNotFoundException;
import aiss.videominer.model.Channel;
import aiss.videominer.repository.ChannelRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Channel", description = "Channel management API")
@RestController
@RequestMapping("/videominer")
public class ChannelController {

    @Autowired
    ChannelRepository repository;

    // GET http://localhost:8080/videominer/channels
    @Operation(
            summary = "Retrieve all channels",
            description = "Get all channels present on the database. Filtering by name and pagination is possible",
            tags = {"channels", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of channels",
                    content = {
                            @Content(schema = @Schema(implementation = Channel.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request"
            )
    })
    @GetMapping("/channels")
    public List<Channel> findAll(
            @Parameter(description = "page number for pagination. Default is 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "number of items per page. Default is 10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "name of the channel to be searched") @RequestParam(required = false) String name
    ) {
        Pageable paging = PageRequest.of(page, size);
        Page<Channel> pageChannels;

        if (name != null) {
            pageChannels = repository.findByName(name, paging);
        } else {
            pageChannels = repository.findAll(paging);
        }

        return pageChannels.getContent();
    }

    // GET http://localhost:8080/videominer/channels/{id}
    @Operation(
            summary = "Retrieve a Channel by Id",
            description = "Get a Channel object by specifying its id",
            tags = {"channels", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Retrieved channel",
                    content = {
                            @Content(schema = @Schema(implementation = Channel.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Channel not found",
                    content = {
                            @Content(schema = @Schema())
                    }
            )
    })
    @GetMapping("/channels/{id}")
    public Channel findOne(
            @Parameter(description = "id of channel to be searched") @PathVariable String id
    ) throws ChannelNotFoundException {
        Optional<Channel> channel = repository.findById(id);

        if (channel.isEmpty()) {
            throw new ChannelNotFoundException();
        }

        return channel.get();
    }

    // POST http://localhost:8080/videominer/channels
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Channel created",
                    content = {@Content(schema = @Schema(implementation = Channel.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(schema = @Schema())})
    })
    @Operation(summary = "Create a channel",
            description = "Create a new channel",
            tags = {"channels", "post"})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/channels")
    public Channel create(@Parameter(description = "channel to be created")
                          @Valid @RequestBody Channel channel) {
        return repository.save(channel);
    }
}
