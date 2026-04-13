package edu.stanstate.degreeplanner.controllers;

import edu.stanstate.degreeplanner.dao.PlanDao;
import edu.stanstate.degreeplanner.models.Plan;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/parent/dashboard")
public class ParentDashboardServlet extends HttpServlet {
  private final PlanDao planDao = new PlanDao();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      Long linkedStudentId = (Long) req.getSession().getAttribute("linkedStudentUserId");
      if (linkedStudentId == null) {
        req.setAttribute("error", "No student linked to this parent account.");
        req.getRequestDispatcher("/jsp/parent-dashboard.jsp").forward(req, resp);
        return;
      }
      Plan plan = planDao.getPlanForStudent(linkedStudentId);
      req.setAttribute("plan", plan);
      req.setAttribute("planCourses", planDao.listPlanCourses(plan.id()));
      req.getRequestDispatcher("/jsp/parent-dashboard.jsp").forward(req, resp);
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }
}