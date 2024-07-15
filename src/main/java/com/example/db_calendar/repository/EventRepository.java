package com.example.db_calendar.repository;

import com.example.db_calendar.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component

public interface EventRepository extends JpaRepository<Event,Integer> {}
