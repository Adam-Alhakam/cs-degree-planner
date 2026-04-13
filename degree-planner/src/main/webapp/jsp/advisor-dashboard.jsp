<%@ page import="java.util.List" %>
<%@ page import="edu.stanstate.degreeplanner.models.Plan" %>
<%
  List<Plan> submittedPlans = (List<Plan>) request.getAttribute("submittedPlans");
%>
<!DOCTYPE html>
<html>
<head>
  <title>Advisor Dashboard</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body class="container">
  <h2>Advisor Dashboard</h2>
  <p><a href="<%=request.getContextPath()%>/logout">Logout</a></p>

  <div class="card">
    <h3>Submitted Plans</h3>
    <table>
      <tr><th>Plan ID</th><th>Student User ID</th><th>Status</th><th>Review</th></tr>
      <% for (Plan p : submittedPlans) { %>
        <tr>
          <td><%=p.id()%></td>
          <td><%=p.studentUserId()%></td>
          <td><%=p.status()%></td>
          <td><a href="<%=request.getContextPath()%>/advisor/review?planId=<%=p.id()%>">Open</a></td>
        </tr>
      <% } %>
    </table>
  </div>
</body>
</html>