package aiss.vimeominer.service;

import aiss.vimeominer.model.Video;
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
public class VideoService {

    @Autowired
    RestTemplate restTemplate;

    String url = "https://api.vimeo.com/videos";
    //https://api.vimeo.com/users/sundanceshorts/videos
    //https://api.vimeo.com/channels/sundanceshorts/videos
    //https://api.vimeo.com/channels/sundanceshorts/videos/898953374

    //Este es el token que he generado yo
    String token = "3f762ed847d6f0ab92886c953e5481de";

    public List<Video> findAllVideosFromUser(String userId) {
        String uri = "https://api.vimeo.com/users/" + userId + "/videos";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<VideoList> request = new HttpEntity<>(null, headers);
        ResponseEntity<VideoList> response = restTemplate.exchange(uri, HttpMethod.GET, request, VideoList.class);

        return Objects.requireNonNull(response.getBody()).getData();
    }

    public List<Video> findAllVideosFromChannel(String channelId, Integer numVideos) {
        String uri = "https://api.vimeo.com/channels/" + channelId + "/videos?page=1&per_page="+numVideos;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<VideoList> request = new HttpEntity<>(null, headers);
        ResponseEntity<VideoList> response = restTemplate.exchange(uri, HttpMethod.GET, request, VideoList.class);

        return Objects.requireNonNull(response.getBody()).getData();
    }

    public Video findVideoFromChannel(String channelId, String idVideo) {
        String uri = "https://api.vimeo.com/channels/" + channelId + "/videos/" + idVideo;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Video> request = new HttpEntity<>(null, headers);
        ResponseEntity<Video> response = restTemplate.exchange(uri, HttpMethod.GET, request, Video.class);

        return response.getBody();
    }

    public Video findVideo(String id) {
        String uri = url + "/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Video> request = new HttpEntity<>(null, headers);
        ResponseEntity<Video> response = restTemplate.exchange(uri, HttpMethod.GET, request, Video.class);

        return response.getBody();
    }

}
