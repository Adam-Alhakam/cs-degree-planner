<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Error</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body class="container">
  <h2>Access Denied (403)</h2>
  <p>You do not have permission to view this page.</p>
  <p><a href="<%=request.getContextPath()%>/jsp/login.jsp">Login</a></p>
</body>
</html>