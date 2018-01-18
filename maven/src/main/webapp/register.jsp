<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/1/11
  Time: 10:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>register</title>
</head>
<body>
<form action="/user/register" method="post">
    <label>username</label><input type="text" name="userName">
    <label>password</label><input type="password" name="password">
    <label>age</label><input type="text" name="age">
    <input type="submit">
</form>
</body>
</html>
