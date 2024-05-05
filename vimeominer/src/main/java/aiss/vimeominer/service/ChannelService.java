package aiss.vimeominer.service;


import aiss.vimeominer.model.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;

@Service
public class ChannelService {

    String url = "https://api.vimeo.com/channels";

    //Este es el token que he generado yo
    String token = "3f762ed847d6f0ab92886c953e5481de";
    String Authorization = "Authorization: Bearer " + token;

    @Autowired
    RestTemplate restTemplate;


    public Channel findChannel(Integer id) {
        String channelUrl = url + "/" + id + "/n" + Authorization;
        return restTemplate.getForObject(channelUrl, Channel.class);
    }

    /*
    public void findChannel(Integer id) {
        String channelUrl = url + "/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+token);
        ResponseEntity<Channel> response = restTemplate.exchange(channelUrl, HttpMethod.GET, request, Channel.class);
    }
     */

    /*
    public User findUserSearch(Integer id) {
        String userUrl = url + "/" + id;
        UserSearch userSearch = restTemplate.getForObject(userUrl, User.class);
        return userSearch;
    }
    */
}
