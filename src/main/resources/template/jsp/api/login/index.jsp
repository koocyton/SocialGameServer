<%--
  Created by IntelliJ IDEA.
  User: henry
  Date: 2017/7/5
  Time: 下午6:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>登录</title>
    <style>.error{color:red;}</style>
</head>
<body>

<div class="error">${error}</div>
<!--
<form action="/accessToken?redirect_uri=http%3A%2F%2Flocalhost%3A8080%2FaccessToken&client_id=c1ebe466-1cdc-4bd3-ab69-77c3561b9dee&client_secret=d8346ea2-6017-43ed-ad68-19c0f971738b&grant_type=authorization_code&response_type=code" method="post">
-->
<form action="" method="post">
    用户名：<input type="text" name="username" value="admin" autocomplete="false"><br/>
    密码：<input type="password" name="password" value="123456" autocomplete="false"><br/>
    自动登录：<input type="checkbox" name="rememberMe" value="true"><br/>
    <input type="submit" value="登录">
</form>

</body>
</html>
