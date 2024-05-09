package aiss.videominer.controller;

import aiss.videominer.exception.ChannelNotFoundException;
import aiss.videominer.exception.VideoNotFoundException;
import aiss.videominer.model.Channel;
import aiss.videominer.model.Video;
import aiss.videominer.repository.ChannelRepository;
import aiss.videominer.repository.VideoRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/videos")
    public List<Video> findAll() { return repository.findAll(); }

    // GET http://localhost:8080/videominer/videos/{id}
    @GetMapping("/videos/{id}")
    public Video findOne(@PathVariable String id) throws VideoNotFoundException {
        Optional<Video> video = repository.findById(id);
        if(video.isEmpty()) {
            throw new VideoNotFoundException();
        }
        return video.get();
    }

    // GET http://localhost:8080/videominer/channels/{id}/videos
    @GetMapping("/channels/{id}/videos")
    public List<Video> findByChannel(@PathVariable String id) throws ChannelNotFoundException {
        Optional<Channel> channel = channelRepository.findById(id);
        if(channel.isEmpty()) {
            throw new ChannelNotFoundException();
        }
        return channel.get().getVideos();
    }
}
