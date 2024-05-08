package aiss.vimeominer.controller;

import aiss.vimeominer.model.Channel;
import aiss.vimeominer.service.ChannelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChannelControllerTest {

    @Autowired
    ChannelController channelController;

    @Test
    @DisplayName("Find all channels")
    void getChannel() {
        Channel channel = channelController.getChannel("sundanceshorts",10,10);
        assertNotNull(channel, "The channel is null");
        System.out.println(channel);
    }
}