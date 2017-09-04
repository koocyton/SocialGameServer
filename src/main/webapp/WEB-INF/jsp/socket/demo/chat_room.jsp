<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="zh-cn">
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>chat room</title>
    <script src="/js/jquery-1.11.3.min.js"></script>
    <script src="/js/socket.io.js"></script>

    <script>
        function onLogin() {
            var form = form[0];
        }
    </script>
</head>

<body>

<div>
    <form action="/api/v1/login" method="post" onsubmit="onLogin();return false;">
        <input type="text" name="account" value="">
        <input type="password" name="password" value="">
        <input type="submit" value="Get Access Token">
    </form>
</div>

</body>

</html>