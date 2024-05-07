package aiss.vimeominer.service;

import aiss.vimeominer.model.Caption;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CaptionServiceTest {

    @Autowired
    CaptionService captionService;

    @Test
    @DisplayName("Find all captions")
    void findAllCaptions() {
        List<Caption> captionList = captionService.findAllCaptions("941713443");
        assertNotNull(captionList, "The captionList is null");
        System.out.println(captionList);
    }

    @Test
    @DisplayName("Find caption by ID")
    void findCaptions() {
        Caption caption = captionService.findCaption("941713443","153358471");
        assertNotNull(caption, "The caption is null");
        System.out.println(caption);
    }
}