<%@ page import="java.util.List" %>
<%@ page import="edu.stanstate.degreeplanner.models.*" %>
<%
  Plan plan = (Plan) request.getAttribute("plan");
  List<PlanCourse> planCourses = (List<PlanCourse>) request.getAttribute("planCourses");
  List<Course> allCourses = (List<Course>) request.getAttribute("allCourses");
%>

<!DOCTYPE html>
<html>
<head>
  <title>Edit Plan</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body class="container">
  <h2>Edit Degree Plan</h2>
  <p>
    <a href="<%=request.getContextPath()%>/student/dashboard">Back</a> |
    <a href="<%=request.getContextPath()%>/logout">Logout</a>
  </p>

  <% if (request.getAttribute("error") != null) { %>
    <div style="color: red; font-weight: bold; margin-bottom: 12px;">
      <%= request.getAttribute("error") %>
    </div>
  <% } %>

  <div class="card">
    <h3>Add Course</h3>
    <form method="post" action="<%=request.getContextPath()%>/student/plan">
      <input type="hidden" name="action" value="add"/>

      <label>Term</label>
      <select name="term">
        <option>FALL</option>
        <option>SPRING</option>
        <option>SUMMER</option>
        <option>WINTER</option>
      </select>

      <label>Year</label>
      <input type="number" name="year" value="2026" required />

      <label>Course</label>
      <select name="courseId">
        <% for (Course c : allCourses) { %>
          <option value="<%=c.id()%>"><%=c.code()%> - <%=c.title()%> (<%=c.units()%>u)</option>
        <% } %>
      </select>

      <button type="submit">Add</button>
    </form>
  </div>

  <div class="card">
    <h3>Current Plan Courses</h3>

    <% if (plan != null) { %>
      <p><strong>Planned Units:</strong> <%= plan.totalCompletedUnits() %> / <%= plan.totalRequiredUnits() %></p>
    <% } %>

    <table>
      <tr>
        <th>Term</th>
        <th>Year</th>
        <th>Course</th>
        <th>Days/Time</th>
        <th>Action</th>
      </tr>
      <% for (PlanCourse pc : planCourses) { %>
        <tr>
          <td><%=pc.term()%></td>
          <td><%=pc.year()%></td>
          <td><%=pc.course().code()%> - <%=pc.course().title()%></td>
          <td><%=pc.course().days()%> <%=pc.course().startTime()%>-<%=pc.course().endTime()%></td>
          <td>
            <form method="post" action="<%=request.getContextPath()%>/student/plan">
              <input type="hidden" name="action" value="remove"/>
              <input type="hidden" name="planCourseId" value="<%=pc.id()%>"/>
              <button type="submit">Remove</button>
            </form>
          </td>
        </tr>
      <% } %>
    </table>
  </div>
</body>
</html>