package aiss.youtubeminer.service;

import aiss.youtubeminer.exception.ChannelNotFoundException;
import aiss.youtubeminer.model.youtube.channel.Channel;
import aiss.youtubeminer.model.youtube.channel.ChannelSearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@Service
public class ChannelService {
    private final String BASE_URI = "https://www.googleapis.com/youtube/v3/channels";

    @Autowired
    private RestTemplate restTemplate;

    public Channel getChannel(String id, String API_KEY) throws ChannelNotFoundException {
        URI uri = UriComponentsBuilder.fromUriString(BASE_URI)
                .queryParam("part", "snippet")
                .queryParam("id", id)
                .build()
                .toUri();

        // Create request
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-goog-api-key", API_KEY);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Send request and deserialize response into ChannelSearch
        ResponseEntity<ChannelSearch> responseEntity;

        try {
            responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, ChannelSearch.class);
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                throw new ChannelNotFoundException();
            } else {
                throw exception;
            }
        }

        ChannelSearch channelSearch = responseEntity.getBody();

        // Double check the Channel is found just in case.
        if (channelSearch == null || channelSearch.getItems().isEmpty()) {
            throw new ChannelNotFoundException();
        }
        return channelSearch.getItems().get(0);
    }
}