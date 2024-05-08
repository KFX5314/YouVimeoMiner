package aiss.youtubeminer.service;

import aiss.youtubeminer.exception.ChannelNotFoundException;
import aiss.youtubeminer.model.youtube.channel.Channel;
import aiss.youtubeminer.model.youtube.videoSnippet.VideoSnippet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChannelServiceTest {
    @Autowired
    ChannelService service;
    @Test
    @DisplayName("Get channel from a given id")
    void getChannel() throws ChannelNotFoundException {
        // Test con el canal de 42FundacionTelefonica
        Channel channel = service.getChannel("UCBfgelCcfPlHU_cy3enz02w", "API_KEY");
        System.out.println(channel.getSnippet());
    }
}