package aiss.vimeominer.service;

import aiss.vimeominer.model.Caption;
import aiss.vimeominer.model.CaptionList;
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
public class CaptionService {

    @Autowired
    RestTemplate restTemplate;

    String url = "https://api.vimeo.com/videos";
    //https://api.vimeo.com/videos/{video_id}/texttracks

    //Este es el token que he generado yo
    String token = "3f762ed847d6f0ab92886c953e5481de";

    public List<Caption> findAllCaptions(String id) {
        String uri = url + "/" + id + "/texttracks";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<CaptionList> request = new HttpEntity<>(null, headers);
        ResponseEntity<CaptionList> response = restTemplate.exchange(uri, HttpMethod.GET, request, CaptionList.class);

        return Objects.requireNonNull(response.getBody()).getData();
    }

    public Caption findCaption(String id, String id2) {
        String uri = url + "/" + id + "/texttracks/" + id2;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Caption> request = new HttpEntity<>(null, headers);
        ResponseEntity<Caption> response = restTemplate.exchange(uri, HttpMethod.GET, request, Caption.class);

        return response.getBody();
    }

}
