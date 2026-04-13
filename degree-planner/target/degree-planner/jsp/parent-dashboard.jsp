<%@ page import="edu.stanstate.degreeplanner.models.*" %>
<%@ page import="java.util.List" %>
<%
  Plan plan = (Plan) request.getAttribute("plan");
  List<PlanCourse> planCourses = (List<PlanCourse>) request.getAttribute("planCourses");
%>
<!DOCTYPE html>
<html>
<head>
  <title>Parent View</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body class="container">
  <h2>Parent Read-Only View</h2>
  <p><a href="<%=request.getContextPath()%>/logout">Logout</a></p>

  <p class="error"><%= request.getAttribute("error")==null ? "" : request.getAttribute("error") %></p>

  <% if (plan != null) { %>
    <div class="card">
      <p><b>Status:</b> <%=plan.status()%></p>
      <p><b>Completed Units:</b> <%=plan.totalCompletedUnits()%> / <%=plan.totalRequiredUnits()%></p>

      <h3>Plan Courses</h3>
      <ul>
        <% for (PlanCourse pc : planCourses) { %>
          <li><%=pc.term()%> <%=pc.year()%> - <%=pc.course().code()%> (<%=pc.course().title()%>)</li>
        <% } %>
      </ul>
    </div>
  <% } %>
</body>
</html>