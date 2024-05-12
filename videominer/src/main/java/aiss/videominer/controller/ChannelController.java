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
import org.springframework.data.domain.Sort;
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
    @Operation( summary = "Retrieve all channels",
            description = "Get all channels present on the database. Filtering by name, sorting and pagination is possible",
            tags = { "channels", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of channels",
                    content = { @Content(schema = @Schema(implementation = Channel.class),
                            mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/channels")
    public List<Channel> findAll(@Parameter(description = "page number for pagination. Default is 0")
                                     @RequestParam(defaultValue = "0") int page,
                                 @Parameter(description = "number of items per page. Default is 10")
                                     @RequestParam(defaultValue = "10") int size,
                                 @Parameter(description = "name of the channel to be searched")
                                     @RequestParam(required= false) String name,
                                 @Parameter(description = "property to sort by. Prefix with '-' for descending order")
                                     @RequestParam(required = false) String order) {
        Pageable paging;
        if(order != null) {
            Sort.Direction direction = order.startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC;
            String property = order.startsWith("-") ? order.substring(1) : order;
            paging = PageRequest.of(page, size, Sort.by(direction, property));
        } else {
            paging = PageRequest.of(page, size);
        }
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
    @Operation( summary = "Create a channel",
            description = "Create a new channel",
            tags = { "channels", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Channel created",
                    content = {@Content(schema = @Schema(implementation = Channel.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/channels")
    public Channel create(@Parameter(description = "channel to be created")
                          @Valid @RequestBody Channel channel) {
        return repository.save(channel);
    }

    // PUT http://localhost:8080/videominer/channels/{id}
    @Operation( summary = "Update a channel",
            description = "Updates the content of the channel whose id is given",
            tags = { "channels", "put" })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Channel updated",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Channel not found",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = { @Content(schema = @Schema()) })
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/channels/{id}")
    public void update(@Parameter(description = "updated channel")
                           @Valid @RequestBody Channel newChannel,
                       @Parameter(description = "id of channel to be updated")
                           @PathVariable String id) throws ChannelNotFoundException {
        Optional<Channel> oldChannel = repository.findById(id);
        if(oldChannel.isEmpty()) {
            throw new ChannelNotFoundException();
        }
        Channel channel = oldChannel.get();
        channel.setName(newChannel.getName());
        channel.setDescription(newChannel.getDescription());
        channel.setVideos(newChannel.getVideos());
        repository.save(channel);
    }

    // DELETE http://localhost:8080/videominer/channels/{id}
    @Operation( summary = "Delete a channel",
            description = "Deletes the channel whose id is given",
            tags = { "channels", "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Channel deleted",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Channel not found",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = { @Content(schema = @Schema()) })
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/channels/{id}")
    public void delete(@Parameter(description = "id of channel to be deleted")
                           @PathVariable String id) throws ChannelNotFoundException {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new ChannelNotFoundException();
        }
    }

}
