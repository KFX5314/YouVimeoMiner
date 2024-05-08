package aiss.youtubeminer.service;

import aiss.youtubeminer.model.Channel;
import aiss.youtubeminer.model.Video;
import aiss.youtubeminer.model.youtube.videoSnippet.VideoSnippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class VideominerService {

    private final String BASE_URI = "http://localhost:8080/videominer/channels";

    @Autowired
    private RestTemplate restTemplate;

    public void saveVideos(String channelId, List<VideoSnippet> videos) {
        List<Video> channelVideos = videos.stream().map(v -> {
            var vid = new Video();

            vid.setId(v.getId().getVideoId());
            vid.setName(v.getSnippet().getTitle());
            vid.setDescription(v.getSnippet().getDescription());
            vid.setReleaseTime(v.getSnippet().getPublishedAt());

            return vid;
        }).toList();

        Channel channel = new Channel();
        channel.setId(channelId);
        channel.setName("");
        channel.setVideos(channelVideos);

        restTemplate.postForObject(BASE_URI, channel, Channel.class);
        // TODO: IN PROGRESS
    }

}
