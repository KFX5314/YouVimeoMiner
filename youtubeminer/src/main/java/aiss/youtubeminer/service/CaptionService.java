package aiss.youtubeminer.service;

import aiss.youtubeminer.model.youtube.caption.Caption;
import aiss.youtubeminer.model.youtube.caption.CaptionSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class CaptionService {// TODO

    private final String BASE_URI = "https://www.googleapis.com/youtube/v3/captions";

    @Autowired
    private RestTemplate restTemplate;

    public List<Caption> getCaptionsForVideo(String videoId, String API_KEY) {
        URI uri = UriComponentsBuilder.fromUriString(BASE_URI)
                .queryParam("part", "snippet")
                .queryParam("videoId", videoId)
                .build()
                .toUri();

        // Create request
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-goog-api-key", API_KEY);
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Initialize empty Caption List
        List<Caption> captions = new ArrayList<>();

        try {
            // Send request and deserialize
            ResponseEntity<CaptionSearch> response = restTemplate.exchange(uri, HttpMethod.GET, request, CaptionSearch.class);
            CaptionSearch CaptionSearch = response.getBody();

            // Extract Caption items from CaptionSearch
            if (CaptionSearch != null && CaptionSearch.getItems() != null) {
                captions.addAll(CaptionSearch.getItems());
            }
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode().is4xxClientError()) {
                System.out.println("Error fetching captions: " + exception.getMessage());
            } else {
                throw exception;
            }
        }

        return captions;
    }
}
