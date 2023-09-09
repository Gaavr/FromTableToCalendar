package org.gaavr.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventDTO {
    private String Date;
    private String subject;
    private String teacher;
}
