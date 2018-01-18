<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/1/10
  Time: 15:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/user/login" method="post">
<label>username</label><input type="text" name="username">
<label>password</label><input type="password" name="password">
<input type="submit">
</form>
<p>${errorMessage}</p>
</body>
</html>
