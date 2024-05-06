package aiss.youtubeminer.service;


import aiss.youtubeminer.model.youtube.channel.Channel;
import aiss.youtubeminer.model.youtube.videoSnippet.VideoSnippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service

public class VideoSnippetService
{
    private String APIKEY = "API_KEY";
    private String baseURI = "https://www.googleapis.com/youtube/v3/";

    @Autowired
    RestTemplate restTemplate;

    public Channel getChannel(Integer id)
    {
        String uri = baseURI + "channels/" + id;

        //Create request
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-goog-api-key",APIKEY );
        HttpEntity<Channel> request = new HttpEntity<>(null, headers);

        //Send request
        ResponseEntity<Channel> channel = restTemplate.exchange(uri, HttpMethod.GET, request, Channel.class);
        return channel.getBody();
    }
    public List<VideoSnippet> getVideosFromChannel(Integer id, Integer maxVideos, Integer maxComments)
    {
        List<VideoSnippet> videos = new ArrayList<>();
        //Sacar canal
        videos = getChannel(id).getVideos().subList(0,maxVideos);//Limitar videos
        //Limitar comentarios
        videos.forEach(v -> v.getComments().subList(0, maxComments));

        return videos;
    }
}