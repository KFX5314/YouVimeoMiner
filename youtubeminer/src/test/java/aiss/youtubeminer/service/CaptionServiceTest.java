package aiss.youtubeminer.service;

import aiss.youtubeminer.model.youtube.caption.Caption;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CaptionServiceTest {
    @Autowired
    CaptionService service;

    @Test
    @DisplayName("Get captions from a given video")
    void getVideosFromChannel() {
        // video 275VkeAlu90
        List<Caption> Captions = service.getCaptionsForVideo("275VkeAlu90", "API_KEY");
        assertFalse(Captions.isEmpty(), "The list of Captions is empty");
        System.out.println(Captions.stream().map(c -> c.getSnippet().getLanguage()).toList());
    }
}