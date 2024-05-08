package aiss.videominer.controller;

import aiss.videominer.exception.ChannelNotFoundException;
import aiss.videominer.model.Channel;
import aiss.videominer.repository.ChannelRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videominer")
public class ChannelController {

    @Autowired
    ChannelRepository repository;

    // GET http://localhost:8080/videominer/channels
    @GetMapping("/channels")
    public List<Channel> findAll() {
        return repository.findAll();
    }

    // GET http://localhost:8080/videominer/channels/{id}
    @GetMapping("/channels/{id}")
    public Channel findOne(@PathVariable String id) throws ChannelNotFoundException {
        Optional<Channel> channel = repository.findById(id);
        if(channel.isEmpty()) {
            throw new ChannelNotFoundException();
        }
        return channel.get();
    }

    // POST http://localhost:8080/videominer/channels
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/channels")
    public Channel create(@Valid @RequestBody Channel channel) {
        return repository.save(channel);
    }
}
