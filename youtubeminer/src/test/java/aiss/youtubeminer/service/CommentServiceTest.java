package aiss.youtubeminer.service;

import aiss.youtubeminer.model.youtube.comment.Comment;
import aiss.youtubeminer.model.youtube.videoSnippet.VideoSnippet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {
    @Autowired
    CommentService service;

    @Test
    @DisplayName("Get M comments from a given channel")
    void getVideosFromChannel() {
        // 10 comentarios del video 275VkeAlu90
        List<Comment> comments = service.getCommentsForVideo("275VkeAlu90", 10);
        assertFalse(comments.isEmpty(), "The list of comments is empty");
        System.out.println(comments.stream().map(v -> v.getCommentSnippet().getTopLevelComment().getSnippet().getTextOriginal()).collect(Collectors.joining("\n")));
    }
}