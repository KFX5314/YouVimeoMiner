package aiss.vimeominer.service;

import aiss.vimeominer.model.Channel;
import aiss.vimeominer.model.VideoList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChannelServiceTest {

    @Autowired
    ChannelService channelService;

    @Test
    @DisplayName("Find all channels")
    void findAllChannels() {
        List<Channel> channelList = channelService.findAllChannels();
        assertNotNull(channelList, "The channelList is null");
        System.out.println(channelList);
    }

    @Test
    @DisplayName("Find channel by ID")
    void findChannel() {
        Channel channel = channelService.findChannel("newyorker");
        assertNotNull(channel, "The channel is null");
        System.out.println(channel);
    }

    @Test
    @DisplayName("Get videos from a channel")
    void getVideosFromChannel() {
        VideoList videoList = channelService.getVideosFromChannel("newyorker");
        assertNotNull(videoList, "The videoList is null");
        System.out.println(videoList);
    }
}