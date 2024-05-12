package aiss.videominer.controller;

import aiss.videominer.exception.ChannelNotFoundException;
import aiss.videominer.exception.VideoNotFoundException;
import aiss.videominer.model.Channel;
import aiss.videominer.model.Video;
import aiss.videominer.repository.ChannelRepository;
import aiss.videominer.repository.VideoRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

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
    public List<Video> findAll(@RequestParam(defaultValue = "0")int page,
                               @RequestParam(defaultValue = "10")int size,
                               @RequestParam(required = false) String order) {
        Pageable paging;
        if(order != null) {
            Sort.Direction direction = order.startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC;
            String property = order.startsWith("-") ? order.substring(1) : order;
            paging = PageRequest.of(page, size, Sort.by(direction, property));
        } else {
            paging = PageRequest.of(page, size);
        }
        Page<Video> pageVideos;
        pageVideos = repository.findAll(paging);
        return pageVideos.getContent();
    }

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
