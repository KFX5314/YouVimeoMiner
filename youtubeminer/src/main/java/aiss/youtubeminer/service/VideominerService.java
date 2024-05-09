package aiss.youtubeminer.service;

import aiss.youtubeminer.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VideominerService {

    private final String BASE_URI = "http://localhost:8080/videominer/channels";

    @Autowired
    private RestTemplate restTemplate;

    // TODO: NO PROBADO
    public Channel saveChannel(aiss.youtubeminer.model.youtube.channel.Channel ytChannel) {
        Channel videoChannel = new Channel();
        videoChannel.setId(ytChannel.getId());
        videoChannel.setName(ytChannel.getSnippet().getTitle());
        videoChannel.setDescription(ytChannel.getSnippet().getDescription());
        videoChannel.setCreatedTime(ytChannel.getSnippet().getPublishedAt());

        videoChannel.setVideos(ytChannel.getVideos().stream().map(v -> {
            Video vid = new Video();

            vid.setId(v.getId().getVideoId());
            vid.setName(v.getSnippet().getTitle());
            vid.setDescription(v.getSnippet().getDescription());
            vid.setReleaseTime(v.getSnippet().getPublishedAt());

            vid.setComments(v.getComments().stream().map(c -> {
                Comment comment = new Comment();
                var topLevelComment = c.getCommentSnippet().getTopLevelComment();

                comment.setId(topLevelComment.getId());
                comment.setText(topLevelComment.getSnippet().getTextOriginal());
                comment.setCreatedOn(topLevelComment.getSnippet().getPublishedAt());

                User user = new User();
                user.setName(topLevelComment.getSnippet().getAuthorDisplayName());
                user.setUser_link(topLevelComment.getSnippet().getAuthorChannelUrl());
                user.setPicture_link(topLevelComment.getSnippet().getAuthorProfileImageUrl());

                comment.setAuthor(user);

                return comment;
            }).toList());

            vid.setCaptions(v.getCaptions().stream().map(c -> {
                var caption = new Caption();

                caption.setId(c.getId());
                caption.setName(c.getSnippet().getName());
                caption.setLanguage(c.getSnippet().getLanguage());

                return caption;
            }).toList());

            return vid;
        }).toList());

        restTemplate.postForObject(BASE_URI, videoChannel, Channel.class);

        return videoChannel;
    }

}
