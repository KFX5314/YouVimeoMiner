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
    void findAllVideosFromUser() {
        List<Video> videoList = videoService.findAllVideosFromUser("luizdemedia");
        assertNotNull(videoList, "The videoList is null");
        System.out.println(videoList);
    }
    @Test
    @DisplayName("Find all videos")
    void findAllVideosFromChannel() {
        List<Video> videoList = videoService.findAllVideosFromChannel("sundanceshorts",10);
        assertNotNull(videoList, "The videoList is null");
        System.out.println(videoList);
    }
    @Test
    @DisplayName("Find all videos")
    void findVideoFromChannel() {
        Video video = videoService.findVideoFromChannel("sundanceshorts","898953374");
        assertNotNull(video, "The video is null");
        System.out.println(video);
    }
    @Test
    @DisplayName("Find video by ID")
    void findVideo() {
        Video video = videoService.findVideo("898953374");
        assertNotNull(video, "The video is null");
        System.out.println(video);
    }
}