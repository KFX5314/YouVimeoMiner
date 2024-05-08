package aiss.youtubeminer.service;

import aiss.youtubeminer.exception.ChannelNotFoundException;
import aiss.youtubeminer.model.youtube.videoSnippet.VideoSnippet;
import aiss.youtubeminer.model.youtube.videoSnippet.VideoSnippetSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class VideoService {

    private final String BASE_URI = "https://www.googleapis.com/youtube/v3/search";

    @Autowired
    private RestTemplate restTemplate;

    public List<VideoSnippet> getVideosFromChannel(String id, Integer maxVideos, String API_KEY) throws ChannelNotFoundException {
        URI uri = UriComponentsBuilder.fromUriString(BASE_URI)
                .queryParam("part", "snippet")
                .queryParam("channelId", id)
                .queryParam("maxResults", maxVideos.toString())
                .build()
                .toUri();

        // Create request
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-goog-api-key", API_KEY);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Send request and deserialize response into VideoSnippetSearch
        ResponseEntity<VideoSnippetSearch> responseEntity;


        responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, VideoSnippetSearch.class);


        VideoSnippetSearch videoSnippetSearch = responseEntity.getBody();

        // Extract VideoSnippet items from VideoSnippetSearch
        List<VideoSnippet> videoSnippets = new ArrayList<>();
        if (videoSnippetSearch != null && videoSnippetSearch.getItems() != null) {
            videoSnippets.addAll(videoSnippetSearch.getItems());
        }
        return videoSnippets;
    }
}
