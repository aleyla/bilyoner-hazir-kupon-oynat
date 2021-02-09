package com.bilyoner.assignment.couponapi.controller;

import com.bilyoner.assignment.couponapi.model.EventDTO;
import com.bilyoner.assignment.couponapi.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/event")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<EventDTO> getAllEvents() {
        return eventService.getAllEvents();
    }

    @PostMapping
    public EventDTO createEvent(@RequestBody @Valid EventDTO eventRequest) {
        return eventService.createEvent(eventRequest);
    }

}
