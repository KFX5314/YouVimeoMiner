package aiss.vimeominer.service;

import aiss.vimeominer.model.Channel;
import aiss.vimeominer.model.ChannelList;
import aiss.vimeominer.model.VideoList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Objects;

@Service
public class ChannelService {

    @Autowired
    RestTemplate restTemplate;

    String url = "https://api.vimeo.com/channels";

    //Este es el token que he generado yo
    String token = "3f762ed847d6f0ab92886c953e5481de";

    public List<Channel> findAllChannels() {
        String uri = url;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<ChannelList> request = new HttpEntity<>(null, headers);
        ResponseEntity<ChannelList> response = restTemplate.exchange(uri, HttpMethod.GET, request, ChannelList.class);

        return Objects.requireNonNull(response.getBody()).getData();
    }

    public Channel findChannel(String id) {
        String uri = url+"/"+id;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Channel> request = new HttpEntity<>(null, headers);
        ResponseEntity<Channel> response = restTemplate.exchange(uri, HttpMethod.GET, request, Channel.class);

        return response.getBody();
    }

    public VideoList getVideosFromChannel(String channelId) {
        String uri = "https://api.vimeo.com/channels/" + channelId + "/videos";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<VideoList> request = new HttpEntity<>(null, headers);
        ResponseEntity<VideoList> response = restTemplate.exchange(uri, HttpMethod.GET, request, VideoList.class);

        return response.getBody();
    }

}
