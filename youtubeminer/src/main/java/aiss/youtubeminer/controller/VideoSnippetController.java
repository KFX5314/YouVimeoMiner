package aiss.youtubeminer.controller;

import aiss.youtubeminer.model.youtube.comment.Comment;
import aiss.youtubeminer.model.youtube.videoSnippet.VideoSnippet;
import aiss.youtubeminer.service.CommentService;
import aiss.youtubeminer.service.VideoSnippetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/youtubeminer")

public class VideoSnippetController
{
    @Autowired
    VideoSnippetService videoSnippetService;
    @Autowired
    CommentService commentService;

    // GET http://localhost:8082/youtubeminer/channels/{channelId}
    // EJEMPLO DE GET: http://localhost:8082/youtubeminer/channels/UC_slDWTPHflhuZqbGc8u4yA?maxVideos=10&maxComments=10
    @GetMapping("/channels/{channelId}")
    public List<VideoSnippet> getVideosFromChannel(@PathVariable(value="channelId") String channelId,
                                                   @RequestParam(defaultValue = "10") int maxVideos,
                                                   @RequestParam(defaultValue = "10") int maxComments)
                                                    //,@RequestHeader("X-goog-api-key") String apiKey)
    {
        List<VideoSnippet> videos = videoSnippetService.getVideosFromChannel(channelId, maxVideos, "API_KEY");//TODO
        for (VideoSnippet video : videos)
        {
            List<Comment> comments = commentService.getCommentsForVideo(video.getId().getVideoId(), maxComments, "API_KEY");//TODO
            video.setComments(comments);
        }
        return videos;
    }
}
