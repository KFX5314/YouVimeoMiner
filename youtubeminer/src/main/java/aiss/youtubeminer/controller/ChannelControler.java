package aiss.youtubeminer.controller;

import aiss.youtubeminer.exception.ChannelNotFoundException;
import aiss.youtubeminer.model.youtube.channel.Channel;
import aiss.youtubeminer.model.youtube.comment.Comment;
import aiss.youtubeminer.model.youtube.videoSnippet.VideoSnippet;
import aiss.youtubeminer.service.ChannelService;
import aiss.youtubeminer.service.CommentService;
import aiss.youtubeminer.service.VideoService;
import aiss.youtubeminer.service.VideominerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    VideominerService videominerService;

    // GET http://localhost:8082/youtubeminer/channels/{channelId}
    // EJEMPLO DE GET: http://localhost:8082/youtubeminer/channels/UC_slDWTPHflhuZqbGc8u4yA?maxVideos=10&maxComments=10
    @GetMapping("/channels/{channelId}")
    public Channel getChannel(
            @PathVariable(value = "channelId") String channelId,
            @RequestParam(defaultValue = "10") Integer maxVideos,
            @RequestParam(defaultValue = "10") Integer maxComments,
            @RequestHeader("X-goog-api-key") String API_KEY
    ) throws ChannelNotFoundException {
        List<VideoSnippet> videos = videoService.getVideosFromChannel(channelId, maxVideos, API_KEY);

        for (VideoSnippet video : videos) {
            List<Comment> comments = commentService.getCommentsForVideo(video.getId().getVideoId(), maxComments, API_KEY);
            video.setComments(comments);
        }

        Channel channel = channelService.getChannel(channelId, API_KEY);
        channel.setVideos(videos);
        return channel;
    }

    // POST http://localhost:8082/youtubeminer/channels/{channelId}
    /*@ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/channels/{channelId}")
    public List<VideoSnippet> downloadVideos(
            @PathVariable(value = "id") String channelId,
            @RequestParam(defaultValue = "10") Integer maxVideos,
            @RequestParam(defaultValue = "10") Integer maxComments,
            @RequestHeader("X-goog-api-key") String API_KEY
    ) throws ChannelNotFoundException {
        var videos = getChannel(channelId, maxVideos, maxComments, API_KEY);

        videominerService.saveVideos(channelId, videos);

        return videos;
    }*/
}
