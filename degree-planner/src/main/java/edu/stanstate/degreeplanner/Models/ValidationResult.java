package edu.stanstate.degreeplanner.models;

import java.util.List;

public record ValidationResult(boolean okToSubmit, List<ValidationIssue> issues, int completedUnits) {}