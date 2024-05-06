package aiss.videominer.controller;

import aiss.videominer.model.Caption;
import aiss.videominer.repository.CaptionRepository;
import aiss.videominer.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/videominer")
public class CaptionController {

    @Autowired
    CaptionRepository repository;
    @Autowired
    VideoRepository videoRepository;

    // GET http://localhost:8080/videominer/captions
    @GetMapping("/captions")
    public List<Caption> findAll() { return repository.findAll(); }

    // GET http://localhost:8080/videominer/captions/{id}
    @GetMapping("/captions/{id}")
    public Caption findOne(@PathVariable String id) {
        return repository.findById(id).orElseThrow();
    }

    // GET http://localhost:8080/videominer/videos/{id}/captions
    @GetMapping("/videos/{id}/captions")
    public List<Caption> findByVideo(@PathVariable String id) {
        return videoRepository.findById(id).orElseThrow().getCaptions();
    }
}
