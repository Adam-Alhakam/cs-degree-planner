<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Degree Planner</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body class="container">
    <h1>Degree Planning and Advising System</h1>
    <p>Welcome to the Stan State CS Degree Planning System.</p>

    <div class="card">
        <p>This system allows:</p>
        <ul>
            <li>Students to build and submit semester plans</li>
            <li>Advisors to review and approve or deny plans</li>
            <li>Parents to view student progress in read-only mode</li>
        </ul>
    </div>

    <p>
        <a href="<%=request.getContextPath()%>/jsp/login.jsp">Go to Login</a>
    </p>

    <p>
        <a href="<%=request.getContextPath()%>/test-db">Test Database</a>
    </p>
</body>
</html>
