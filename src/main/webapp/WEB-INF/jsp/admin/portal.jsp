<%--
  Created by IntelliJ IDEA.
  User: henry
  Date: 2017/4/27
  Time: 下午2:39
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <jsp:include page="piece/header.jsp" />
    <title>OAuth2.0 Manager</title>
</head>
<body>

<jsp:include page="piece/background.jsp" />

<div class="body-content">
    <div class="body-content-left scroll-container opacity-80">
        <div style="top:0px;position:relative;padding:0 0;" id="main-menu"></div>
    </div>
    <div class="body-content-right scroll-container opacity-95">
    </div>
</div>

<nav class="navbar navbar-inverse opacity-95" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <a href="javascript:void(0);" class="navbar-brand"> &nbsp; 狼人杀</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <a href="javascript:void(0);"> ${manager.username} </a>
                </li>
                <li class="dropdown">
                    <a href="javascript:;"  class="dropdown-toggle" data-toggle="dropdown" style="padding:8px 6px 4px 6px;"><span class="glyphicon glyphicon-cog" style="font-size:23px;"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li class="dropdown-menu-arrow"><b class="angle-up"></b></li>
                        <li>
                            <a href="javascript:;">
                                <span class="glyphicon glyphicon-list-alt"></span>　问题反馈
                            </a>
                        </li>
                        <li>
                            <a href="javascript:;">
                                <span class="glyphicon glyphicon-lock"></span>　账号安全
                            </a>
                        </li>
                        <li>
                            <a href="javascript:$.KTAnchor.popupLoader('/sundry/icons')">
                                <span class="glyphicon glyphicon-th"></span>　图标大全　
                                <span class="badge">200</span>
                            </a>
                        </li>
                        <li>
                            <a pushstate="no" href="/login/sign-out" confirm="要退出登录么 ？">
                                <span class="glyphicon glyphicon-log-out"></span>　退出
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div><!-- /#navbar -->
    </div><!-- /.container -->
</nav><!-- /.navbar -->




<jsp:include page="piece/alert.jsp" />

<jsp:include page="piece/popup.jsp" />

<jsp:include page="piece/confirm.jsp" />

<script>
    var menu_data = [
        {text:"运营", menus:[
            {text:"公告系统", href:"/notice/list"},
            {text:"玩家管理", href:"/player/list"},
            {text:"数据分析", href:"/analysis-setup/list", open:true, menus:[
                {text:"新增用户分析", href:"/analysis/active-player"}
            ]}
        ]},
        {text:"研发", menus:[
            {text:"开发任务", href:"/task/list", badge:"3"},
            {text:"游戏控制台", href:"/game-manager/panel"},
            {text:"服务器配置", href:"/game-setup/list", open:true, menus:[
                {text:"Room Tree", href:"/game-tree/panel"},
                {text:"包 & 版本管理", href:"/game-client/list"},
                {text:"游戏大厅", href:"/game-hall/list"},
                // {text:"游戏服", href:"/game-server/list"},
                {text:"游戏", href:"/game/list"}
            ]}
        ]},
        {text:"Admin", href:"/operation/list", icon:"list-alt", menus:[
            {text:"管理员", href:"/manager/list" },
            {text:"角色管理", href:"/role/list" }
        ]}
    ];
    $("#main-menu").html($.KTTreeMenuHTML.getMenuHtml(menu_data));
</script>

<jsp:include page="piece/footer.jsp" />

</body>
</html>
