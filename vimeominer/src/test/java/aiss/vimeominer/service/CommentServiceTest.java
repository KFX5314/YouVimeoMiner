package aiss.vimeominer.service;

import aiss.vimeominer.model.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Test
    @DisplayName("Find all comments")
    void findAllChannels() {
        List<Comment> commentList = commentService.findAllComments("898953374",10);
        assertNotNull(commentList, "The channelList is null");
        System.out.println(commentList);
    }

    @Test
    @DisplayName("Find comment by ID")
    void findChannel() {
        Comment comment = commentService.findComment("898953374","20493363");
        assertNotNull(comment, "The channel is null");
        System.out.println(comment);
    }
}