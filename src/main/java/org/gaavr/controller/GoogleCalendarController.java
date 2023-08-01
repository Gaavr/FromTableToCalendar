package org.gaavr.controller;

import com.google.api.services.calendar.model.Event;
import lombok.RequiredArgsConstructor;
import org.gaavr.service.GoogleCalendarService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class GoogleCalendarController {

    private final GoogleCalendarService googleCalendarService;

    @GetMapping("/events")
    public List<Event> getEvents() {
        return googleCalendarService.getEvents();
    }

    @PostMapping("/events")
    public Event createEvent(@RequestBody Event event) {
        return googleCalendarService.createEvent(event);
    }
}
