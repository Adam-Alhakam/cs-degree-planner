package edu.stanstate.degreeplanner.controllers;

import edu.stanstate.degreeplanner.dao.AdvisorReviewDao;
import edu.stanstate.degreeplanner.dao.PlanDao;
import edu.stanstate.degreeplanner.models.Plan;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@WebServlet("/advisor/review")
public class AdvisorReviewServlet extends HttpServlet {
    private final PlanDao planDao = new PlanDao();
    private final AdvisorReviewDao reviewDao = new AdvisorReviewDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long planId = Long.parseLong(req.getParameter("planId"));
            Plan plan = planDao.getPlanById(planId);

            Object error = req.getSession().getAttribute("error");
            if (error != null) {
                req.setAttribute("error", error);
                req.getSession().removeAttribute("error");
            }

            req.setAttribute("plan", plan);
            req.setAttribute("planCourses", planDao.listPlanCourses(planId));
            req.getRequestDispatcher("/jsp/advisor-review.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long advisorUserId = (long) req.getSession().getAttribute("userId");
            long planId = Long.parseLong(req.getParameter("planId"));
            String decision = req.getParameter("decision");
            String comment = req.getParameter("comment");

            String updatedAtParam = req.getParameter("updatedAt");
            Timestamp expectedUpdatedAt = parseTimestamp(updatedAtParam);

            boolean ok = planDao.updateStatusWithOptimisticLock(planId, decision, comment, expectedUpdatedAt);
            if (!ok) {
                req.getSession().setAttribute("error", "Plan was updated by someone else. Refresh and try again.");
                resp.sendRedirect(req.getContextPath() + "/advisor/review?planId=" + planId);
                return;
            }

            reviewDao.create(planId, advisorUserId, decision, comment);
            resp.sendRedirect(req.getContextPath() + "/advisor/dashboard");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private Timestamp parseTimestamp(String value) throws ServletException {
        if (value == null || value.isBlank()) {
            throw new ServletException("Missing updatedAt value for optimistic locking.");
        }

        String v = value.trim();

        try {
            return Timestamp.valueOf(v);
        } catch (IllegalArgumentException ignored) {
        }

        try {
            return Timestamp.valueOf(LocalDateTime.parse(v.replace(" ", "T")));
        } catch (Exception ignored) {
        }

        try {
            return Timestamp.valueOf(LocalDate.parse(v).atStartOfDay());
        } catch (Exception ignored) {
        }

        throw new ServletException("Invalid updatedAt format: " + v);
    }
}