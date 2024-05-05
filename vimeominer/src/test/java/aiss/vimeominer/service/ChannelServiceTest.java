package aiss.vimeominer.service;

import aiss.vimeominer.model.Channel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChannelServiceTest {

    @Autowired
    ChannelService channelService;

    @Test
    @DisplayName("Find channel by ID")
    void findChannel() {
        Channel channel = channelService.findChannel(1001);
        assertNotNull(channel, "The user is null");
        System.out.println(channel);
    }
    /*@Test
    @DisplayName("Create a new User")
    void postUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe4@gmail.com");
        service.postUser(user);
        System.out.println(user);
    }
     */
}