package edu.stanstate.degreeplanner.models;

public record User(long id, String email, String role, Long linkedStudentUserId) {}