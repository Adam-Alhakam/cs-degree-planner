package edu.stanstate.degreeplanner.services;

import edu.stanstate.degreeplanner.dao.CourseDao;
import edu.stanstate.degreeplanner.dao.PlanDao;
import edu.stanstate.degreeplanner.models.Course;
import edu.stanstate.degreeplanner.models.PlanCourse;
import edu.stanstate.degreeplanner.models.ValidationIssue;
import edu.stanstate.degreeplanner.models.ValidationResult;
import edu.stanstate.degreeplanner.utils.TimeUtil;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

public class ValidationService {

  private static final ExecutorService POOL = Executors.newFixedThreadPool(8);

  private final PlanDao planDao = new PlanDao();
  private final CourseDao courseDao = new CourseDao();

  public Future<ValidationResult> validateAsync(long planId) {
    return POOL.submit(() -> validate(planId));
  }

  public ValidationResult validate(long planId) throws SQLException {
    List<PlanCourse> pcs = planDao.listPlanCourses(planId);

    int completedUnits = pcs.stream()
        .filter(pc -> "COMPLETED".equals(pc.status()))
        .mapToInt(pc -> pc.course().units())
        .sum();

    List<ValidationIssue> issues = new ArrayList<>();

    Set<Long> completedCourseIds = new HashSet<>();
    for (PlanCourse pc : pcs) {
      if ("COMPLETED".equals(pc.status())) {
        completedCourseIds.add(pc.course().id());
      }
    }

    pcs.sort(Comparator
        .comparingInt(PlanCourse::year)
        .thenComparingInt(pc -> termOrder(pc.term()))
        .thenComparing(pc -> pc.course().code()));

    Set<Long> takenOrEarlierPlanned = new HashSet<>(completedCourseIds);

    for (PlanCourse pc : pcs) {
      long courseId = pc.course().id();
      List<Long> prereqIds = courseDao.getPrerequisiteIds(courseId);

      for (Long pre : prereqIds) {
        if (!takenOrEarlierPlanned.contains(pre)) {
          issues.add(new ValidationIssue(
              "PREREQ",
              pc.course().code() + " missing prerequisite course (must be completed or planned earlier)."
          ));
        }
      }

      takenOrEarlierPlanned.add(courseId);
    }

    Map<String, List<PlanCourse>> bySemester = new HashMap<>();
    for (PlanCourse pc : pcs) {
      String key = pc.term() + "-" + pc.year();
      bySemester.computeIfAbsent(key, k -> new ArrayList<>()).add(pc);
    }

    for (Map.Entry<String, List<PlanCourse>> entry : bySemester.entrySet()) {
      List<PlanCourse> list = entry.getValue();
      for (int i = 0; i < list.size(); i++) {
        for (int j = i + 1; j < list.size(); j++) {
          Course a = list.get(i).course();
          Course b = list.get(j).course();
          if (TimeUtil.daysOverlap(a.days(), b.days()) &&
              TimeUtil.overlaps(a.startTime(), a.endTime(), b.startTime(), b.endTime())) {
            issues.add(new ValidationIssue(
                "CONFLICT",
                "Schedule conflict in " + entry.getKey() + ": " + a.code() + " overlaps " + b.code()
            ));
          }
        }
      }
    }

    boolean hasPrereqErrors = issues.stream().anyMatch(i -> "PREREQ".equals(i.type()));
    return new ValidationResult(!hasPrereqErrors, issues, completedUnits);
  }

  private int termOrder(String term) {
    return switch (term) {
      case "SPRING" -> 1;
      case "SUMMER" -> 2;
      case "FALL" -> 3;
      case "WINTER" -> 4;
      default -> 99;
    };
  }
}