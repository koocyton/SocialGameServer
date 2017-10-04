<%--
  Created by IntelliJ IDEA.
  User: henry
  Date: 2017/4/12
  Time: 下午2:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="zh-cn">
<head>
  <jsp:include page="piece/header.jsp" />
  <title>User Login</title>
</head>
<body>

<jsp:include page="piece/background.jsp" />

<nav class="navbar navbar-inverse opacity-80" role="navigation">
  <div class="container">
    <div id="navbar" class="collapse navbar-collapse">
      <ul class="nav navbar-nav navbar-right">
        <li class="dropdown">
          <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown">语言：简体中文 <span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu">
            <li class="dropdown-menu-arrow"><b class="angle-up"></b></li>
            <li><a native="yes" href="?locale=cn">简体中文</a></li>
            <li><a native="yes" href="?locale=tw">繁體中文</a></li>
            <li><a native="yes" href="?locale=kr">한국어</a></li>
            <li><a native="yes" href="?locale=jp">日本語</a></li>
            <li><a native="yes" href="?locale=en">English</a></li>
          </ul>
        </li>
      </ul>
    </div><!-- /#navbar -->
  </div><!-- /.container -->
</nav><!-- /.navbar -->

<div class="cn-modal-dialog modal-dialog modal-my-sm opacity-95">
  <form action="/admin/sign-in" method="post">
    <input type="password" style="position:absolute;top:-999px;"/>
    <div class="modal-content">
      <div class="modal-body">
        <div class="form-group">
          <input type="text" name="account" class="form-control" autocomplete="off" placeholder="请输入邮箱账号" validation="/email:请输入邮箱账号/" value="test@test.com">
        </div>
        <div class="form-group">
          <input type="password" name="password" class="form-control" autocomplete="off" placeholder="请输入您的密码" validation="/password:请输入您的密码/" value="test@test.com">
        </div>
        <div>
          <label>
            <input type="checkbox" name="remember" value="1"> 记住我
          </label>
          <span class="separator">·</span>
          <a href="javascript:;" native="yes" style="color:#0084b4">忘记密码了?</a>
        </div>
      </div>
      <div class="modal-footer">
        <button type="submit" class="btn btn-success">登录</button>
      </div>
    </div><!-- /.modal-content -->
  </form><!-- /.modal-form -->
</div><!-- /.modal-dialog -->

<jsp:include page="piece/alert.jsp" />

<jsp:include page="piece/footer.jsp" />

</body>
</html>
