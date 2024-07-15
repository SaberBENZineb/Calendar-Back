package com.example.db_calendar.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue
    private Integer id;
    private String start;
    private String end;
    private String title;
    private String primaryColor;
    private String secondaryColor;
    private String textColor;

    @ManyToMany(mappedBy = "assignedEvents",fetch = FetchType.EAGER)
    @JsonBackReference
    private List<User> users= new ArrayList<>();
}