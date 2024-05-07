package aiss.vimeominer.service;

import aiss.vimeominer.model.Video;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VideoServiceTest {

    @Autowired
    VideoService videoService;

    @Test
    @DisplayName("Find all videos")
    void findAllVideos() {
        List<Video> videoList = videoService.findAllVideos("paultrillo");
        assertNotNull(videoList, "The videoList is null");
        System.out.println(videoList);
    }

    @Test
    @DisplayName("Find video by ID")
    void findVideo() {
        Video video = videoService.findVideo("941713443");
        assertNotNull(video, "The video is null");
        System.out.println(video);
    }
}