package edu.stanstate.degreeplanner.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter(urlPatterns = {"/student/*", "/advisor/*", "/parent/*"})
public class AuthFilter implements Filter {
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;

    HttpSession session = req.getSession(false);
    if (session == null || session.getAttribute("role") == null) {
      resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp");
      return;
    }

    String role = (String) session.getAttribute("role");
    String uri = req.getRequestURI();

    if (uri.contains("/student/") && !"STUDENT".equals(role)) { resp.sendError(403); return; }
    if (uri.contains("/advisor/") && !"ADVISOR".equals(role)) { resp.sendError(403); return; }
    if (uri.contains("/parent/")  && !"PARENT".equals(role))  { resp.sendError(403); return; }

    chain.doFilter(request, response);
  }
}