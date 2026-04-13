package edu.stanstate.degreeplanner.controllers;

import edu.stanstate.degreeplanner.utils.DB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/test-db")
public class TestDbServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><body>");
            out.println("<h2>Starting DB test...</h2>");

            try (Connection conn = DB.getConnection()) {
                out.println("<p>Connected to DB successfully ✅</p>");

                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT id, email, role FROM users")) {

                    out.println("<table border='1' cellpadding='6'>");
                    out.println("<tr><th>ID</th><th>Email</th><th>Role</th></tr>");

                    while (rs.next()) {
                        out.println("<tr>");
                        out.println("<td>" + rs.getLong("id") + "</td>");
                        out.println("<td>" + rs.getString("email") + "</td>");
                        out.println("<td>" + rs.getString("role") + "</td>");
                        out.println("</tr>");
                    }

                    out.println("</table>");
                }
            } catch (Exception e) {
                out.println("<h3>DB Error ❌</h3>");
                out.println("<pre>" + e.getMessage() + "</pre>");
                e.printStackTrace(out);
            }

            out.println("</body></html>");
        }
    }
}