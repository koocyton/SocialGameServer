<%--
  Created by IntelliJ IDEA.
  User: henry
  Date: 2017/7/4
  Time: 上午11:53
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<c:forEach items="${users}" var="user">

    <div>#${user.id} ${user.account}</div>

</c:forEach>

