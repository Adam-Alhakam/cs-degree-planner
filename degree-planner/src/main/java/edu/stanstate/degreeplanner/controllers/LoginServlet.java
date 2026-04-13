package edu.stanstate.degreeplanner.controllers;

import edu.stanstate.degreeplanner.dao.UserDao;
import edu.stanstate.degreeplanner.models.User;
import edu.stanstate.degreeplanner.services.PasswordService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
                req.setAttribute("error", "Please enter both email and password.");
                req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
                return;
            }

            email = email.trim().toLowerCase();

            User user = userDao.findByEmail(email);
            String hash = userDao.getPasswordHashByEmail(email);

            if (user == null || hash == null || !PasswordService.verify(password, hash)) {
                req.setAttribute("error", "Invalid email or password.");
                req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
                return;
            }

            HttpSession oldSession = req.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("userId", user.id());
            session.setAttribute("role", user.role());
            session.setAttribute("linkedStudentUserId", user.linkedStudentUserId());
            session.setMaxInactiveInterval(30 * 60);

            switch (user.role()) {
                case "STUDENT":
                    resp.sendRedirect(req.getContextPath() + "/student/dashboard");
                    break;
                case "ADVISOR":
                    resp.sendRedirect(req.getContextPath() + "/advisor/dashboard");
                    break;
                case "PARENT":
                    resp.sendRedirect(req.getContextPath() + "/parent/dashboard");
                    break;
                default:
                    session.invalidate();
                    req.setAttribute("error", "Unknown user role.");
                    req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
                    break;
            }

        } catch (Exception e) {
            throw new ServletException("Login failed.", e);
        }
    }
}