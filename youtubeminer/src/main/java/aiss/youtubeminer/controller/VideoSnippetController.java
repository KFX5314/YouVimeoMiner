package aiss.youtubeminer.controller;

import aiss.youtubeminer.exception.ChannelNotFoundException;
import aiss.youtubeminer.model.youtube.comment.Comment;
import aiss.youtubeminer.model.youtube.videoSnippet.VideoSnippet;
import aiss.youtubeminer.service.CommentService;
import aiss.youtubeminer.service.VideoSnippetService;
import aiss.youtubeminer.service.VideominerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/youtubeminer")

public class VideoSnippetController {
    @Autowired
    VideoSnippetService videoSnippetService;
    @Autowired
    CommentService commentService;

    @Autowired
    VideominerService videominerService;

    // GET http://localhost:8082/youtubeminer/channels/{channelId}
    // EJEMPLO DE GET: http://localhost:8082/youtubeminer/channels/UC_slDWTPHflhuZqbGc8u4yA?maxVideos=10&maxComments=10
    @GetMapping("/channels/{channelId}")
    public List<VideoSnippet> getVideosFromChannel(
            @PathVariable(value = "channelId") String channelId,
            @RequestParam(defaultValue = "10") int maxVideos,
            @RequestParam(defaultValue = "10") int maxComments
    ) throws ChannelNotFoundException {
        List<VideoSnippet> videos = videoSnippetService.getVideosFromChannel(channelId, maxVideos);

        for (VideoSnippet video : videos) {
            List<Comment> comments = commentService.getCommentsForVideo(video.getId().getVideoId(), maxComments);
            video.setComments(comments);
        }

        return videos;
    }

    // POST http://localhost:8082/youtubeminer/{id}
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}")
    public List<VideoSnippet> downloadVideos(
            @PathVariable(value = "id") String channelId,
            @RequestParam(defaultValue = "10") Integer maxVideos,
            @RequestParam(defaultValue = "10") Integer maxComments
    ) throws ChannelNotFoundException {
        var videos = getVideosFromChannel(channelId, maxVideos, maxComments);

        videominerService.saveVideos(channelId, videos);

        return videos;
    }
}
