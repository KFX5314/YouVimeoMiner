package aiss.youtubeminer.controller;

import aiss.youtubeminer.exception.ChannelNotFoundException;
import aiss.youtubeminer.model.youtube.caption.Caption;
import aiss.youtubeminer.model.youtube.caption.CaptionSearch;
import aiss.youtubeminer.model.youtube.channel.Channel;
import aiss.youtubeminer.model.youtube.comment.Comment;
import aiss.youtubeminer.model.youtube.videoSnippet.VideoSnippet;
import aiss.youtubeminer.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Channel", description = "Channel management API")
@RestController
@RequestMapping("/youtubeminer")
public class ChannelControler {
    @Autowired
    ChannelService channelService;
    @Autowired
    VideoService videoService;
    @Autowired
    CommentService commentService;
    @Autowired
    CaptionService captionService;

    @Autowired
    VideominerService videominerService;

    // GET http://localhost:8082/youtubeminer/channels/{channelId}
    // EJEMPLO DE GET: http://localhost:8082/youtubeminer/channels/UC_slDWTPHflhuZqbGc8u4yA?maxVideos=10&maxComments=10
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
            @PathVariable(value = "channelId") String channelId,
                              @Parameter(description = "limit of videos to add to the channel")
            @RequestParam(defaultValue = "10") Integer maxVideos,
                              @Parameter(description = "limit of comments to add to the channel's videos")
            @RequestParam(defaultValue = "10") Integer maxComments,
                              @Parameter(description = "YouTube API KEY, it must passed through the \'X-goog-api-key\' field of the header")
            @RequestHeader("X-goog-api-key") String API_KEY
    ) throws ChannelNotFoundException {
        List<VideoSnippet> videos = videoService.getVideosFromChannel(channelId, maxVideos, API_KEY);

        for (VideoSnippet video : videos) {
            List<Comment> comments = commentService.getCommentsForVideo(video.getId().getVideoId(), maxComments, API_KEY);
            video.setComments(comments);
            List<Caption> captions = captionService.getCaptionsForVideo(video.getId().getVideoId(), API_KEY);
            video.setCaptions(captions);
        }

        Channel channel = channelService.getChannel(channelId, API_KEY);
        channel.setVideos(videos);
        return channel;
    }

    // POST http://localhost:8082/youtubeminer/channels/{channelId}
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Channel created",
                    content = {@Content(schema = @Schema(implementation = aiss.youtubeminer.model.Channel.class),
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
    public aiss.youtubeminer.model.Channel downloadVideos(
                @Parameter(description = "id of channel to be searched")
            @PathVariable(value = "channelId") String channelId,
                @Parameter(description = "limit of videos to add to the channel")
            @RequestParam(defaultValue = "10") Integer maxVideos,
                @Parameter(description = "limit of comments to add to the channel's videos")
            @RequestParam(defaultValue = "10") Integer maxComments,
                @Parameter(description = "YouTube API KEY, it must passed through the X-goog-api-key field of the header")
            @RequestHeader("X-goog-api-key") String apiKey
    ) throws ChannelNotFoundException {
        var channel = getChannel(channelId, maxVideos, maxComments, apiKey);

        return videominerService.saveChannel(channel);
    }
}
