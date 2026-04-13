package edu.stanstate.degreeplanner.models;

public record PlanCourse(long id, long planId, String term, int year, Course course, String status) {}