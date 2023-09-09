package org.gaavr.controller;

import com.google.api.services.calendar.model.Event;
import lombok.RequiredArgsConstructor;
import org.gaavr.model.EventDTO;
import org.gaavr.service.GoogleCalendarService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
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

    @PostMapping("/create-events")
    public String createEvents(@RequestBody List<EventDTO> eventDTOList) throws GeneralSecurityException, IOException, InterruptedException {
        googleCalendarService.createEventsFromDTO(eventDTOList);
        return "Events have been created";
    }

    @PostMapping("/create-events-from-sheet")
    public String createEvents() {
        try {
            googleCalendarService.createEventsFromDTO();
            return "Events have been created";
        } catch (GeneralSecurityException | IOException | InterruptedException e) {
            return e.getMessage();
        }


    }

    @DeleteMapping("/delete-all-events")
    public String deleteAllEvents() {
        try {
            googleCalendarService.deleteAllEvents();
            return "Events have been deleted";
        } catch (Exception e) {
            return "Error deleting events: " + e.getMessage();
        }
    }
}
