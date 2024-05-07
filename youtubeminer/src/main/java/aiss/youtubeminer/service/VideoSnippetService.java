package aiss.youtubeminer.service;

import aiss.youtubeminer.model.youtube.videoSnippet.VideoSnippet;
import aiss.youtubeminer.model.youtube.videoSnippet.VideoSnippetSearch;
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
public class VideoSnippetService {

    @Autowired
    private RestTemplate restTemplate;

    public List<VideoSnippet> getVideosFromChannel(String id, Integer maxVideos, String APIKEY)
    {
        String baseURI = "https://www.googleapis.com/youtube/v3/";
        String uri = baseURI + "search?part=snippet&channelId=" + id + "&maxResults=" + maxVideos;
        //System.out.println(uri);
        // Create request
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-goog-api-key", APIKEY);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Send request and deserialize response into VideoSnippetSearch
        ResponseEntity<VideoSnippetSearch> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, VideoSnippetSearch.class);
        VideoSnippetSearch videoSnippetSearch = responseEntity.getBody();

        // Extract VideoSnippet items from VideoSnippetSearch
        List<VideoSnippet> videoSnippets = new ArrayList<>();
        //Assert no null
        if (videoSnippetSearch != null && videoSnippetSearch.getItems() != null)
        {
            videoSnippets.addAll(videoSnippetSearch.getItems());
        }

        return videoSnippets;
    }
}
