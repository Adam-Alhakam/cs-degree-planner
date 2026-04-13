package edu.stanstate.degreeplanner.controllers;

import edu.stanstate.degreeplanner.dao.PlanDao;
import edu.stanstate.degreeplanner.models.Plan;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/student/dashboard")
public class StudentDashboardServlet extends HttpServlet {
  private final PlanDao planDao = new PlanDao();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      long userId = (long) req.getSession().getAttribute("userId");
      Plan plan = planDao.getPlanForStudent(userId);
      req.setAttribute("plan", plan);
      req.getRequestDispatcher("/jsp/student-dashboard.jsp").forward(req, resp);
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }
}