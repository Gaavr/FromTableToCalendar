package org.gaavr.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import lombok.RequiredArgsConstructor;
import org.gaavr.config.GoogleConfig;
import org.gaavr.util.GoogleAuthorizeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleCalendarService {

    private final GoogleAuthorizeUtil googleAuthorizeUtil;
    private final GoogleConfig googleConfig;


    public List<Event> getEvents() {
        try {
            Calendar calendarService = googleAuthorizeUtil.getCalendarService();

            DateTime now = new DateTime(System.currentTimeMillis());
            Events events = calendarService.events().list(googleConfig.getCalendarId())
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            return events.getItems();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Event createEvent(Event event) {
        try {
            Calendar calendarService = googleAuthorizeUtil.getCalendarService();

            String calendarId = "primary";
            return calendarService.events().insert(calendarId, event).execute();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            return null;
        }
    }
}
