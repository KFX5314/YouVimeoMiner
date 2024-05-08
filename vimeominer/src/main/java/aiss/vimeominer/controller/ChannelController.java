package aiss.vimeominer.controller;

import aiss.vimeominer.model.*;
import aiss.vimeominer.service.CaptionService;
import aiss.vimeominer.service.ChannelService;
import aiss.vimeominer.service.CommentService;
import aiss.vimeominer.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
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

    // GET http://localhost:8081/vimeominer/channels/{channelId}
    // EJEMPLO DE GET: http://localhost:8081/vimeominer/channels/sundanceshorts?maxVideos=10&maxComments=10

    @GetMapping("/channels/{channelId}")
    public Channel getChannel(@PathVariable(value="channelId") String channelId,
                                          @RequestParam(defaultValue = "10") int maxVideos,
                                          @RequestParam(defaultValue = "10") int maxComments)
                                            //,@RequestHeader("Authorization") String apiKey)
    {
        Channel channel = channelService.findChannel(channelId);
        List<Video> videos = videoService.findAllVideosFromChannel(channelId, maxVideos);//TODO

        for (Video video : videos)
        {
            List<Caption> captions = captionService.findAllCaptions(video.getId());
            List<Comment> comments = commentService.findAllComments(video.getId(), maxComments);//TODO
            video.setCaptions((CaptionList) captions);
            video.setComments((CommentList) comments);
        }
        channel.setVideos((VideoList) videos);
        return channel;
    }


}
