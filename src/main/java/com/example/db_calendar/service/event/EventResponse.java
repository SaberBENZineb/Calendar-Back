package com.example.db_calendar.service.event;

import com.example.db_calendar.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventResponse {
    private String message;
    private String status;
    private Event event;
}
