<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body class="container">
    <h2>Login</h2>

    <form method="post" action="<%=request.getContextPath()%>/login">
        <label>Email</label>
        <input type="email" name="email" required />

        <label>Password</label>
        <input type="password" name="password" required />

        <button type="submit">Login</button>
    </form>

    <p style="color:red;">
        <%= request.getAttribute("error") == null ? "" : request.getAttribute("error") %>
    </p>

    <div class="card">
        <h3>Demo Accounts</h3>
        <p>student@stan.edu / test123</p>
        <p>advisor@stan.edu / test123</p>
        <p>parent@stan.edu / test123</p>
    </div>
</body>
</html>