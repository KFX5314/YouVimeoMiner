package aiss.youtubeminer.service;

import aiss.youtubeminer.exception.ChannelNotFoundException;
import aiss.youtubeminer.model.youtube.videoSnippet.VideoSnippet;
import aiss.youtubeminer.model.youtube.videoSnippet.VideoSnippetSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class VideoSnippetService {

    private final String API_KEY = "API_KEY";

    private final String BASE_URI = "https://www.googleapis.com/youtube/v3/search";

    @Autowired
    private RestTemplate restTemplate;

    public List<VideoSnippet> getVideosFromChannel(String id, Integer maxVideos) throws ChannelNotFoundException {
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

        try {
            responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, VideoSnippetSearch.class);
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                throw new ChannelNotFoundException();
            } else {
                throw exception;
            }
        }

        VideoSnippetSearch videoSnippetSearch = responseEntity.getBody();

        // Extract VideoSnippet items from VideoSnippetSearch
        List<VideoSnippet> videoSnippets = new ArrayList<>();
        if (videoSnippetSearch != null && videoSnippetSearch.getItems() != null) {
            videoSnippets.addAll(videoSnippetSearch.getItems());
        }
        //TODO incluso si el canal existe devuelve esta excepcion para canales sin videos
        if (videoSnippets.isEmpty())
        {
            throw new ChannelNotFoundException();
        }
        return videoSnippets;
    }

    public List<VideoSnippet> getVideosFromChannel(String id) throws ChannelNotFoundException {
        return getVideosFromChannel(id, 10);
    }
}
