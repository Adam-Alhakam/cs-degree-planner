package edu.stanstate.degreeplanner.models;

import java.time.LocalTime;

public record Course(long id, String code, String title, int units, String days, LocalTime startTime, LocalTime endTime) {}