package aiss.youtubeminer.service;

import aiss.youtubeminer.exception.ChannelNotFoundException;
import aiss.youtubeminer.model.youtube.videoSnippet.VideoSnippet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VideoSnippetServiceTest {
    @Autowired
    VideoSnippetService service;

    @Test
    @DisplayName("Get N videos from a given channel")
    void getVideosFromChannel() throws ChannelNotFoundException {
        // 10 videos del canal RAINBOLT
        List<VideoSnippet> videos = service.getVideosFromChannel("UC_slDWTPHflhuZqbGc8u4yA", 10);
        assertFalse(videos.isEmpty(), "The list of videos is empty");
        System.out.println(videos.stream().map(v -> v.getSnippet().getTitle()).collect(Collectors.joining("\n")));
    }
}