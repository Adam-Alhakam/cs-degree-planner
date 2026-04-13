<%@ page import="edu.stanstate.degreeplanner.models.*" %>
<%@ page import="java.util.List" %>
<%
  Plan plan = (Plan) request.getAttribute("plan");
  List<PlanCourse> planCourses = (List<PlanCourse>) request.getAttribute("planCourses");
  String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html>
<head>
  <title>Review Plan</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body class="container">
  <h2>Review Plan #<%= plan.id() %></h2>
  <p>
    <a href="<%=request.getContextPath()%>/advisor/dashboard">Back</a> |
    <a href="<%=request.getContextPath()%>/logout">Logout</a>
  </p>

  <% if (error != null) { %>
    <div style="color: red; font-weight: bold; margin-bottom: 12px;">
      <%= error %>
    </div>
  <% } %>

  <div class="card">
    <p><b>Status:</b> <%= plan.status() %></p>
    <p><b>Updated At:</b> <%= plan.updatedAt() %></p>

    <h3>Courses</h3>
    <ul>
      <% for (PlanCourse pc : planCourses) { %>
        <li>
          <%= pc.term() %> <%= pc.year() %>:
          <b><%= pc.course().code() %></b>
          (<%= pc.course().days() %> <%= pc.course().startTime() %>-<%= pc.course().endTime() %>)
        </li>
      <% } %>
    </ul>

    <h3>Decision</h3>
    <form method="post" action="<%=request.getContextPath()%>/advisor/review">
      <input type="hidden" name="planId" value="<%= plan.id() %>"/>
      <input type="hidden" name="updatedAt" value="<%= String.valueOf(plan.updatedAt()) %>"/>

      <label for="comment">Comment</label><br/>
      <textarea id="comment" name="comment" rows="4" cols="50"></textarea><br/><br/>

      <button type="submit" name="decision" value="APPROVED">Approve</button>
      <button type="submit" name="decision" value="DENIED">Deny</button>
    </form>
  </div>
</body>
</html>