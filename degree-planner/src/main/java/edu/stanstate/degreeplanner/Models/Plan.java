package edu.stanstate.degreeplanner.models;

import java.sql.Timestamp;

public record Plan(long id, long studentUserId, String status, String advisorComment,
                   int totalCompletedUnits, int totalRequiredUnits, Timestamp updatedAt) {}