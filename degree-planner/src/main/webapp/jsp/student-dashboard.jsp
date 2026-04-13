<%@ page import="edu.stanstate.degreeplanner.models.Plan" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.stanstate.degreeplanner.models.ValidationIssue" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
  Plan plan = (Plan) request.getAttribute("plan");
  List<ValidationIssue> issues = (List<ValidationIssue>) request.getAttribute("issues");
%>
<!DOCTYPE html>
<html>
<head>
  <title>Student Dashboard</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body class="container">
  <h2>Student Dashboard</h2>
  <p><a href="<%=request.getContextPath()%>/logout">Logout</a></p>

  <div class="card">
    <p><b>Status:</b> <%= plan == null ? "No plan" : plan.status() %></p>
    <p><b>Completed Units:</b> <%= plan == null ? 0 : plan.totalCompletedUnits() %> / <%= plan == null ? 120 : plan.totalRequiredUnits() %></p>
    <p><b>Advisor Comment:</b> <%= plan == null ? "" : (plan.advisorComment() == null ? "" : plan.advisorComment()) %></p>

    <p><a href="<%=request.getContextPath()%>/student/plan">Edit Plan</a></p>

    <form method="post" action="<%=request.getContextPath()%>/student/submit">
      <button type="submit">Submit Plan to Advisor</button>
    </form>

    <p class="error"><%= request.getAttribute("error")==null ? "" : request.getAttribute("error") %></p>

    <% if (issues != null && !issues.isEmpty()) { %>
      <h3>Validation Results</h3>
      <ul>
        <% for (ValidationIssue i : issues) { %>
          <li><b><%= i.type() %></b>: <%= i.message() %></li>
        <% } %>
      </ul>
    <% } %>
  </div>
</body>
</html>