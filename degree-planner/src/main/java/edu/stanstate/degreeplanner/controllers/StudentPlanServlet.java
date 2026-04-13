package edu.stanstate.degreeplanner.controllers;

import edu.stanstate.degreeplanner.dao.CourseDao;
import edu.stanstate.degreeplanner.dao.PlanDao;
import edu.stanstate.degreeplanner.models.Course;
import edu.stanstate.degreeplanner.models.Plan;
import edu.stanstate.degreeplanner.models.PlanCourse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

@WebServlet("/student/plan")
public class StudentPlanServlet extends HttpServlet {
    private final PlanDao planDao = new PlanDao();
    private final CourseDao courseDao = new CourseDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long userId = (long) req.getSession().getAttribute("userId");
            Plan plan = planDao.ensurePlanForStudent(userId);
            List<PlanCourse> pcs = planDao.listPlanCourses(plan.id());

            recalculatePlannedUnits(plan.id(), pcs);

            req.setAttribute("plan", planDao.getPlanById(plan.id()));
            req.setAttribute("planCourses", pcs);
            req.setAttribute("allCourses", courseDao.listAll());
            req.getRequestDispatcher("/jsp/student-plan.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long userId = (long) req.getSession().getAttribute("userId");
            Plan plan = planDao.ensurePlanForStudent(userId);

            String action = req.getParameter("action");

            if ("add".equals(action)) {
                String term = req.getParameter("term");
                int year = Integer.parseInt(req.getParameter("year"));
                long courseId = Long.parseLong(req.getParameter("courseId"));

                List<PlanCourse> existingCourses = planDao.listPlanCourses(plan.id());
                Course targetCourse = courseDao.getById(courseId);

                if (targetCourse == null) {
                    forwardWithError(req, resp, plan, existingCourses, "Selected course was not found.");
                    return;
                }

                if (alreadyInPlan(existingCourses, courseId)) {
                    forwardWithError(req, resp, plan, existingCourses,
                            "Course " + targetCourse.code() + " is already in your plan.");
                    return;
                }

                String prereqError = validatePrerequisites(courseId, term, year, existingCourses);
                if (prereqError != null) {
                    forwardWithError(req, resp, plan, existingCourses, prereqError);
                    return;
                }

                String conflictError = validateTimeConflicts(targetCourse, term, year, existingCourses);
                if (conflictError != null) {
                    forwardWithError(req, resp, plan, existingCourses, conflictError);
                    return;
                }

                planDao.addCourse(plan.id(), term, year, courseId);

            } else if ("remove".equals(action)) {
                long planCourseId = Long.parseLong(req.getParameter("planCourseId"));
                planDao.removeCourse(planCourseId);
            }

            List<PlanCourse> updatedCourses = planDao.listPlanCourses(plan.id());
            recalculatePlannedUnits(plan.id(), updatedCourses);

            resp.sendRedirect(req.getContextPath() + "/student/plan");
        } catch (SQLException e) {
            throw new ServletException("Database error while updating student plan.", e);
        }
    }

    private void forwardWithError(HttpServletRequest req, HttpServletResponse resp, Plan plan,
                                  List<PlanCourse> existingCourses, String errorMessage)
            throws ServletException, IOException, SQLException {
        recalculatePlannedUnits(plan.id(), existingCourses);
        req.setAttribute("error", errorMessage);
        req.setAttribute("plan", planDao.getPlanById(plan.id()));
        req.setAttribute("planCourses", existingCourses);
        req.setAttribute("allCourses", courseDao.listAll());
        req.getRequestDispatcher("/jsp/student-plan.jsp").forward(req, resp);
    }

    private boolean alreadyInPlan(List<PlanCourse> existingCourses, long courseId) {
        for (PlanCourse pc : existingCourses) {
            if (pc.course().id() == courseId) {
                return true;
            }
        }
        return false;
    }

    private String validatePrerequisites(long courseId, String targetTerm, int targetYear,
                                         List<PlanCourse> existingCourses) throws SQLException {
        List<Long> prereqIds = courseDao.getPrerequisiteIds(courseId);
        if (prereqIds.isEmpty()) {
            return null;
        }

        Course targetCourse = courseDao.getById(courseId);

        for (Long prereqId : prereqIds) {
            Course prereqCourse = courseDao.getById(prereqId);
            boolean foundEarlier = false;

            for (PlanCourse pc : existingCourses) {
                if (pc.course().id() == prereqId && isEarlier(pc.term(), pc.year(), targetTerm, targetYear)) {
                    foundEarlier = true;
                    break;
                }
            }

            if (!foundEarlier) {
                String prereqCode = prereqCourse != null ? prereqCourse.code() : ("ID " + prereqId);
                String courseCode = targetCourse != null ? targetCourse.code() : ("ID " + courseId);
                return "Cannot add " + courseCode + ". Prerequisite " + prereqCode
                        + " must be planned in an earlier semester.";
            }
        }

        return null;
    }

    private String validateTimeConflicts(Course targetCourse, String targetTerm, int targetYear,
                                         List<PlanCourse> existingCourses) {
        for (PlanCourse pc : existingCourses) {
            if (pc.year() != targetYear) continue;
            if (!pc.term().equalsIgnoreCase(targetTerm)) continue;

            Course existing = pc.course();
            if (daysOverlap(targetCourse.days(), existing.days())
                    && timesOverlap(targetCourse.startTime(), targetCourse.endTime(),
                    existing.startTime(), existing.endTime())) {
                return "Cannot add " + targetCourse.code() + ". It conflicts with "
                        + existing.code() + " in the same semester.";
            }
        }
        return null;
    }

    private boolean daysOverlap(String d1, String d2) {
        if (d1 == null || d2 == null) return false;
        for (char ch : d1.toCharArray()) {
            if (d2.indexOf(ch) >= 0) {
                return true;
            }
        }
        return false;
    }

    private boolean timesOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private boolean isEarlier(String term1, int year1, String term2, int year2) {
        if (year1 < year2) return true;
        if (year1 > year2) return false;
        return termOrder(term1) < termOrder(term2);
    }

    private int termOrder(String term) {
        return switch (term.toUpperCase()) {
            case "SPRING" -> 1;
            case "SUMMER" -> 2;
            case "FALL" -> 3;
            case "WINTER" -> 4;
            default -> 0;
        };
    }

    private void recalculatePlannedUnits(long planId, List<PlanCourse> planCourses) throws SQLException {
        int totalUnits = 0;
        for (PlanCourse pc : planCourses) {
            totalUnits += pc.course().units();
        }
        planDao.updateCompletedUnits(planId, totalUnits);
    }
}