package org.gaavr.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import lombok.RequiredArgsConstructor;
import org.gaavr.config.GoogleConfig;
import org.gaavr.model.EventDTO;
import org.gaavr.util.GoogleAuthorizeUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class GoogleCalendarService {

    private final GoogleAuthorizeUtil googleAuthorizeUtil;
    private final GoogleConfig googleConfig;
    private final GoogleSheetsReaderService googleSheetsReaderService;


    public List<Event> getEvents() {
        try {
            Calendar calendarService = googleAuthorizeUtil.getCalendarService();

            Events events = calendarService.events().list(googleConfig.getCalendarId())
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

    public void createEventsFromDTO(List<EventDTO> eventDTOList) throws GeneralSecurityException, IOException, InterruptedException {
        if (eventDTOList == null) {
            eventDTOList = googleSheetsReaderService.getListOfEvents();
        }
        try {
            Calendar calendarService = googleAuthorizeUtil.getCalendarService();

            for (EventDTO eventDTO : eventDTOList) {
                Event event = new Event()
                        .setSummary(eventDTO.getSubject())
                        .setDescription("Преподаватель: " + eventDTO.getTeacher());

                String dateStr = eventDTO.getDate();
                String[] dateParts = dateStr.split(" ");
                String startDateStr = dateParts[0];
                String startTimeStr = dateParts[1].split("-")[0];
                String endTimeStr = dateParts[1].split("-")[1];

                DateTime startDateTime = new DateTime(startDateStr + "T" + startTimeStr + ":00+03:00"); // Время в вашем часовом поясе
                EventDateTime start = new EventDateTime()
                        .setDateTime(startDateTime)
                        .setTimeZone("Europe/Moscow"); // Установите ваш часовой пояс
                event.setStart(start);

                DateTime endDateTime = new DateTime(startDateStr + "T" + endTimeStr + ":00+03:00");
                EventDateTime end = new EventDateTime()
                        .setDateTime(endDateTime)
                        .setTimeZone("Europe/Moscow");
                event.setEnd(end);

                String calendarId = googleConfig.getCalendarId(); // ID календаря, в который добавляются события
                calendarService.events().insert(calendarId, event).execute();
            }

            System.out.println("События созданы успешно.");
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    public void createEventsFromDTO() throws GeneralSecurityException, IOException, InterruptedException {
        List<EventDTO> eventDTOList = googleSheetsReaderService.getListOfEvents();
        Pattern datePattern = Pattern.compile("^\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}\\.\\d{2}-\\d{2}\\.\\d{2}$");

        Calendar calendarService = googleAuthorizeUtil.getCalendarService();
        String previousDate = null;

        for (EventDTO eventDTO : eventDTOList) {
            System.out.println(eventDTO.getDate());
        }

        for (EventDTO eventDTO : eventDTOList) {
            String dateStr = eventDTO.getDate();

            if (!datePattern.matcher(dateStr).matches()) {
                System.out.println("Неверный формат даты: " + dateStr);
                continue;
            }

            Event event = new Event()
                    .setSummary(eventDTO.getSubject())
                    .setDescription("Преподаватель: " + eventDTO.getTeacher());

            String[] dateParts = dateStr.split(" ");
            String startDateStr = dateParts[0];
            String startTimeStr = dateParts[1].split("-")[0].replace('.', ':');
            String endTimeStr = dateParts[1].split("-")[1].replace('.', ':');

            if (!startDateStr.contains(".")) {
                startDateStr = previousDate;
            } else {
                previousDate = startDateStr;
                String[] dateElements = startDateStr.split("\\.");
                startDateStr = dateElements[2] + "-" + dateElements[1] + "-" + dateElements[0];
            }

            DateTime startDateTime = new DateTime(startDateStr + "T" + startTimeStr + ":00+03:00");
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone("Europe/Moscow");
            event.setStart(start);

            DateTime endDateTime = new DateTime(startDateStr + "T" + endTimeStr + ":00+03:00");
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime)
                    .setTimeZone("Europe/Moscow");
            event.setEnd(end);

            String calendarId = googleConfig.getCalendarId();
            calendarService.events().insert(calendarId, event).execute();
        }

        System.out.println("События созданы успешно.");
    }


}
