package edu.stanstate.degreeplanner.controllers;

import edu.stanstate.degreeplanner.dao.PlanDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/advisor/dashboard")
public class AdvisorDashboardServlet extends HttpServlet {
  private final PlanDao planDao = new PlanDao();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      req.setAttribute("submittedPlans", planDao.listSubmittedPlans());
      req.getRequestDispatcher("/jsp/advisor-dashboard.jsp").forward(req, resp);
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }
}