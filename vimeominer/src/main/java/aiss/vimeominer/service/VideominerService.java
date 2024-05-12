package aiss.vimeominer.service;

import aiss.vimeominer.videominer_models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VideominerService {

    private final String BASE_URI = "http://localhost:8080/videominer/channels";

    @Autowired
    private RestTemplate restTemplate;

    public Channel saveChannel(aiss.vimeominer.model.Channel channel) {
        Channel newChannel = new Channel();

        newChannel.setId(channel.getId());
        newChannel.setName(channel.getName());
        newChannel.setDescription(channel.getDescription());
        newChannel.setCreatedTime(channel.getCreatedTime());

        newChannel.setVideos(channel.getVideos().stream().map(v -> {
            Video vid = new Video();

            vid.setId(v.getId());
            vid.setName(v.getName());
            vid.setDescription(v.getDescription());
            vid.setReleaseTime(v.getReleaseTime());

            vid.setComments(v.getComments().stream().map(c -> {
                Comment comment = new Comment();

                comment.setId(c.getId());
                comment.setText(c.getText());
                comment.setCreatedOn(c.getCreatedOn());

                User user = new User();
                user.setName(c.getUser().getName());
                user.setUser_link(c.getUser().getUser_link());
                user.setPicture_link(c.getUser().getPicture_link());

                comment.setAuthor(user);

                return comment;
            }).toList());

            vid.setCaptions(v.getCaptions().stream().map(c -> {
                var caption = new Caption();

                caption.setId(c.getId());
                caption.setName(c.getName());
                caption.setLanguage(c.getLanguage());

                return caption;
            }).toList());

            return vid;
        }).toList());
        restTemplate.postForObject(BASE_URI, newChannel, Channel.class);

        return newChannel;
    }

}
