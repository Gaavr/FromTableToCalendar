package org.gaavr.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CalendarEventDTO {
    private String id;
    private String summary;
    private String description;
    private String location;
    private String startDateTime;
    private String endDateTime;
    private String timeZone;
    private List<String> attendees;
}

