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
    <script src="https://cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <script>
        let ws = null;
        function onLogin() {
            // let account = $("#form-account").val();
            // let password = $("#form-password").val();
            let $form = $("#login-form");
            $.ajax({
                "url"  : "/api/v1/login",
                "type" : "post",
                "data" : $form.serialize(),
                "contentType" : "application/x-www-form-urlencoded; charset=UTF-8",
                "processData" : false,
                "headers" : null,
                "success" : function(responseText) {
                    if (typeof(responseText)=="object" && typeof(responseText["access-token"])=="string") {
                        $("#access-token").val(responseText["access-token"]);
                        $("#socket-connect").css("display", "block");
                    }
                },
                "error" : function(XMLHttpRequest) {
                    //
                },
                "complete" : function(XMLHttpRequest) {
                    //
                }
            });
            return false;
        }

        function socketConnect() {

            let accessToken = $("#access-token").val();
            ws = new WebSocket("ws://127.0.0.1:8080/game-socket?t=1&access-token=" + accessToken + "&b=2");
            // Listen for the connection open event then call the sendMessage function
            ws.onopen = function(e) {
                console.log("WebSocket Connected ...");
                // let accessToken = document.getElementById("access-token").value;
                // sendMessage({"access-token":accessToken})
            };
            // Listen for the close connection event
            ws.onclose = function(e) {
                console.log("Disconnected: " + e.reason);
            };
            // Listen for connection errors
            ws.onerror = function(e) {
                console.log("Error ");
            };
            // Listen for new messages arriving at the client
            ws.onmessage = function(e) {
                console.log("Message received: " + e.data);

                let showMsgElt = document.getElementById("chat-window");
                let newMsg = document.createElement("div");
                newMsg.innerHTML = e.data;
                let firstChild = showMsgElt.firstChild;
                showMsgElt.insertBefore(newMsg, firstChild);
            };
            return false;
        }

        function sendMessage() {
            let msg = $("#new-message").val();
            ws.send(msg);
            $("#new-message").val("");
            $("#new-message").focus();
            console.log("Message sent : " + msg);
        }
    </script>
</head>

<body>

<div style="width:600px;padding:20px;">

    <div style="width:600px;display:block;">
        <form action="/api/v1/login" id="login-form" method="post" onsubmit="onLogin();return false;">
            <div class="form-group" style="float:left;width:200px;margin-right:10px;">
                <input type="text" id="form-account" class="form-control" name="account" value="koocyton@gmail.com">
            </div>
            <div class="form-group" style="float:left;width:200px;margin-right:10px;">
                <input type="text" id="form-password" class="form-control" name="password" value="123456">
            </div>
            <div class="form-group" style="float:left;width:100px;">
                <button class="btn btn-success" type="submit">Get Token</button>
            </div>
        </form>
    </div>

    <div style="width:600px;display:block;">
        <div class="form-group" style="float:left;width:100px;margin-right:10px;">
            <span style="height:30px;line-height:30px;">access-token :</span>
        </div>
        <div class="form-group" style="float:left;width:300px;margin-right:10px;">
            <input type="text" id="access-token" class="form-control" name="access-token" style="width:300px;" value="">
        </div>
        <div class="form-group" id="socket-connect" style="float:left;width:100px;display:none;">
            <a href="javascript:socketConnect();">
                <button class="btn btn-success">连接游戏服</button>
            </a>
        </div>
    </div>




    <div style="width:600px;display:block;">

        <div id="chat-window" style="height:300px;display:block;width:500px;padding:10px;border:1px solid #cccccc;overflow-y:scroll;"></div>

        <div>
            <div class="form-group" style="float:left;width:400px;margin-right:10px;">
                <input type="text" id="new-message" class="form-control" name="access-token" style="width:400px;" value="">
            </div>
            <div class="form-group" style="float:left;width:100px;">
                <a href="javascript:sendMessage();">
                    <button class="btn btn-success">发言</button>
                </a>
            </div>
        </div>
    </div>
</div>

</body>

</html>