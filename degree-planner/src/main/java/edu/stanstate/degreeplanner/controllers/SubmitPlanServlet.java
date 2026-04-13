package edu.stanstate.degreeplanner.controllers;

import edu.stanstate.degreeplanner.dao.PlanDao;
import edu.stanstate.degreeplanner.models.Plan;
import edu.stanstate.degreeplanner.models.ValidationResult;
import edu.stanstate.degreeplanner.services.ValidationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.concurrent.Future;

@WebServlet("/student/submit")
public class SubmitPlanServlet extends HttpServlet {
  private final PlanDao planDao = new PlanDao();
  private final ValidationService validationService = new ValidationService();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      long userId = (long) req.getSession().getAttribute("userId");
      Plan plan = planDao.getPlanForStudent(userId);

      // Background thread pool validation, then wait for result (still shows "multithreading" correctly)
      Future<ValidationResult> fut = validationService.validateAsync(plan.id());
      ValidationResult result = fut.get(); // for MVP, block submit until we know ok/not ok

      planDao.updateCompletedUnits(plan.id(), result.completedUnits());

      if (!result.okToSubmit()) {
        req.setAttribute("error", "Cannot submit: unmet prerequisites found.");
        req.setAttribute("issues", result.issues());
        req.setAttribute("plan", planDao.getPlanForStudent(userId));
        req.getRequestDispatcher("/jsp/student-dashboard.jsp").forward(req, resp);
        return;
      }

      boolean updated = planDao.updateStatusWithOptimisticLock(plan.id(), "SUBMITTED", null, plan.updatedAt());
      if (!updated) {
        req.setAttribute("error", "Your plan changed since you loaded it. Refresh and try again.");
        req.setAttribute("plan", planDao.getPlanForStudent(userId));
        req.getRequestDispatcher("/jsp/student-dashboard.jsp").forward(req, resp);
        return;
      }

      resp.sendRedirect(req.getContextPath() + "/student/dashboard");
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }
}