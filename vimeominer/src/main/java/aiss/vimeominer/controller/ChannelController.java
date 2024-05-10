package aiss.vimeominer.controller;

import aiss.vimeominer.exception.ChannelNotFoundException;
import aiss.vimeominer.model.*;
import aiss.vimeominer.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/channels/{channelId}")
    public Channel getChannel(@PathVariable(value="channelId") String channelId,
                                          @RequestParam(defaultValue = "10") int maxVideos,
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
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/channels/{channelId}")
    public aiss.vimeominer.videominer_models.Channel downloadChannel(
            @PathVariable(value = "channelId") String channelId,
            @RequestParam(defaultValue = "10") Integer maxVideos,
            @RequestParam(defaultValue = "10") Integer maxComments
    ) throws ChannelNotFoundException {
        Channel channel = getChannel(channelId, maxVideos, maxComments);

        return videominerService.saveChannel(channel);
    }

}
