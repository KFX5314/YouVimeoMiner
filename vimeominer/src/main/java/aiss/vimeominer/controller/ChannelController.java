package aiss.vimeominer.controller;

import aiss.vimeominer.exception.ChannelNotFoundException;
import aiss.vimeominer.model.*;
import aiss.vimeominer.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Channel", description = "Channel management API")
@RestController
@RequestMapping("/vimeominer")

public class ChannelController
{
    @Autowired
    ChannelService channelService;
    @Autowired
    VideoService videoService;
    @Autowired
    CaptionService captionService;
    @Autowired
    CommentService commentService;
    @Autowired
    VideominerService videominerService;

    // GET http://localhost:8081/vimeominer/channels/{channelId}
    // EJEMPLO DE GET: http://localhost:8081/vimeominer/channels/sundanceshorts?maxVideos=10&maxComments=10

    @Operation( summary = "Retrieve a Channel by Id",
            description = "By default the channel comes with 10 videos (if possible), and each video with 10 comments and captions (if possible), the limit of comments and videos can be passed as parameters",
            tags = { "channels", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retrieved channel",
                    content = { @Content(schema = @Schema(implementation = Channel.class),
                            mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Channel not found",
                    content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/channels/{channelId}")
    public Channel getChannel(@Parameter(description = "id of channel to be searched")
                                  @PathVariable(value="channelId") String channelId,
                              @Parameter(description = "limit of videos to add to the channel")
                                          @RequestParam(defaultValue = "10") int maxVideos,
                              @Parameter(description = "limit of comments to add to the channel's videos")
                                          @RequestParam(defaultValue = "10") int maxComments

    ) throws ChannelNotFoundException {
        Channel channel = channelService.findChannel(channelId);
        List<Video> videos = videoService.findAllVideosFromChannel(channelId, maxVideos);

        for (Video video : videos) {
            if(video != null){
                List<Caption> captions = captionService.findAllCaptions(video.getId());
                video.setCaptions(captions);
                List<Comment> comments = commentService.findAllComments(video.getId(), maxComments);
                video.setComments(comments);
            }
        }
        channel.setVideos(videos);

        return channel;
    }

    // POST http://localhost:8082/vimeominer/channels/{channelId}
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Channel created",
                    content = {@Content(schema = @Schema(implementation = aiss.vimeominer.videominer_models.Channel.class),
                            mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Channel not found",
                    content = { @Content(schema = @Schema()) })
    })
    @Operation( summary = "Gets the channel information from YouTube's API using the Id passed as parameter and posts it into the database",
            description = "Copies a channel getting the information from YouTube's API",
            tags = { "channels", "post" })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/channels/{channelId}")
    public aiss.vimeominer.videominer_models.Channel downloadChannel(
            @Parameter(description = "id of channel to be searched")
                @PathVariable(value = "channelId") String channelId,
            @Parameter(description = "limit of videos to add to the channel")
                @RequestParam(defaultValue = "10") Integer maxVideos,
            @Parameter(description = "limit of comments to add to the channel's videos")
                @RequestParam(defaultValue = "10") Integer maxComments
    ) throws ChannelNotFoundException {
        Channel channel = getChannel(channelId, maxVideos, maxComments);

        return videominerService.saveChannel(channel);
    }

}
