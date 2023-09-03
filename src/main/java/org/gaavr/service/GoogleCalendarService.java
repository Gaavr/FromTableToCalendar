package org.gaavr.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import lombok.RequiredArgsConstructor;
import org.gaavr.config.GoogleConfig;
import org.gaavr.model.EventDTO;
import org.gaavr.util.GoogleAuthorizeUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class GoogleCalendarService {

    private final GoogleAuthorizeUtil googleAuthorizeUtil;
    private final GoogleConfig googleConfig;
    private final GoogleSheetsReaderService googleSheetsReaderService;


//    public List<Event> getEvents() {
//        try {
//            Calendar calendarService = googleAuthorizeUtil.getCalendarService();
//
//            Events events = calendarService.events().list(googleConfig.getCalendarId())
//                    .setOrderBy("startTime")
//                    .setSingleEvents(true)
//                    .execute();
//
//            System.out.println(events.size());
//            System.out.println(events.getAccessRole());
//            return events.getItems();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Collections.emptyList();
//        }
//    }

    public List<Event> getEvents() {
        try {
            String calendarId = googleConfig.getCalendarId();
            Calendar calendarService = googleAuthorizeUtil.getCalendarService();

            List<Event> allEvents = new ArrayList<>();
            String nextPageToken = null;

            do {
                Events events = calendarService.events().list(calendarId)
                        .setPageToken(nextPageToken)
                        .execute();

                List<Event> pageEvents = events.getItems();
                if (pageEvents != null) {
                    allEvents.addAll(pageEvents);
                }

                nextPageToken = events.getNextPageToken();
            } while (nextPageToken != null);

            return allEvents;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Event createEvent(Event event) {
        try {
            Calendar calendarService = googleAuthorizeUtil.getCalendarService();

            String calendarId = "primary";
            return calendarService.events().insert(calendarId, event).execute();
        } catch (IOException e) {
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

                String dateStr = eventDTO.getDate().replace("г.", "").trim();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllEvents() {
        try {
            String calendarId = googleConfig.getCalendarId();
            Calendar calendarService = googleAuthorizeUtil.getCalendarService();

            List<Event> eventsToDelete = getEvents();

            for (Event event : eventsToDelete) {
                System.out.println("Попытка удалить событие " + event.getSummary());
                calendarService.events().delete(calendarId, event.getId()).execute();
                System.out.println("Событие " + event.getSummary() + " удалено!");
            }

            System.out.println("Все события удалены из календаря с calendarId: " + calendarId);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось удалить события из календаря" );
        }
    }

    public void createEventsFromDTO() throws GeneralSecurityException, IOException, InterruptedException {
        List<EventDTO> eventDTOList = googleSheetsReaderService.getListOfEvents();
        Pattern datePattern = Pattern.compile("^\\d{2}\\.\\d{2}\\.\\d{4}г? \\d{2}\\.\\d{2}-\\d{2}\\.\\d{2}$");

        Calendar calendarService = googleAuthorizeUtil.getCalendarService();
        String previousDate = null;
        int eventsCreated = 0;
        List<String> notCreatedEvents = new ArrayList<>();

        for (EventDTO eventDTO : eventDTOList) {
            String dateStr = eventDTO.getDate().replace("г.", "").trim();

            if (!datePattern.matcher(dateStr).matches()) {
                System.out.println("Неверный формат даты: " + dateStr);
                notCreatedEvents.add(eventDTO.getSubject());  // Добавляем событие в список не созданных
                continue;
            }

            Event event = new Event()
                    .setSummary(eventDTO.getSubject())
                    .setDescription("Преподаватель: " + eventDTO.getTeacher());

            String[] dateParts = dateStr.split(" ");
            String startDateStr = dateParts[0];
            String startTimeStr = dateParts[1].split("-")[0].replace('.', ':');
            String endTimeStr = dateParts[1].split("-")[1].replace('.', ':');
            //                String startTimeStr = "19:00";
//                String endTimeStr = "22:00";

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

            System.out.println("Создано событие: " + eventDTO.getSubject());
            eventsCreated++;
        }

        if (eventsCreated > 0) {
            System.out.println("Успешно создано событий: " + eventsCreated);
        } else {
            System.out.println("События не были созданы.");
        }

        // Выводим названия не созданных событий
        if (!notCreatedEvents.isEmpty()) {
            System.out.println("Не созданные события:");
            for (String eventName : notCreatedEvents) {
                System.out.println(eventName);
            }
        }
    }
}
