<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<title>Chat Room</title>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=0.5,minimum-scale=0.5,maximum-scale=0.5,user-scalable=no"/>
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="format-detection" content="telephone=no"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="white" />
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
                "url"  : "/api/v1/fast-login",
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

        function joinRoom() {

            let accessToken = $("#access-token").val();

            $.ajax({
                "url"  : "/api/v1/user/random-room",
                "type" : "get",
                "data" : null,
                "contentType" : "application/x-www-form-urlencoded; charset=UTF-8",
                "processData" : false,
                "headers" : {"access-token":accessToken},
                "success" : function(responseText) {
                    socketConnect();
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
            try {
                ws.close();
            }
            catch(e) {;};
            ws = null;
            let accessToken = $("#access-token").val();
            ws = new WebSocket("ws://127.0.0.1:8080/game-socket?access-token=" + accessToken);
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
                let message = e.data;
                console.log("Message received: " + message);
                let showMsgElt = document.getElementById("chat-window");
                let newMsg = document.createElement("div");
                try {
                    var obj = JSON.parse(e.data);
                    newMsg.innerHTML = "<b style=\"font-size:15px;color:blue;\">" + obj.data.sender + " </b> &gt; " + obj.data.message;
                }
                catch(e) {
                    newMsg.innerHTML = message;
                }
                let lastChild = showMsgElt.lastChild;// firstChild;
                showMsgElt.insertBefore(newMsg, lastChild);
                showMsgElt.scrollTop = showMsgElt.scrollHeight;
            };
            return false;
        }

        function sendMessage() {
            let msg = $("#new-message").val();
            let json = "{\"action\":\"room-chat\", \"data\":{\"message\":\"" + msg + "\"}}";
            ws.send(json);
            $("#new-message").val("");
            $("#new-message").focus();
            console.log("Message sent : " + json);
        }
    </script>
</head>

<body>

<div style="width:600px;padding:20px;">

    <div style="width:600px;display:block;">
        <form action="/api/v1/fast-login" id="login-form" method="post" onsubmit="onLogin();return false;">
            <div class="form-group" style="float:left;width:400px;margin-right:10px;">
                <input type="input" id="form-account" class="form-control" name="account" value="">
            </div>
            <!-- <div class="form-group" style="float:left;width:200px;margin-right:10px;">
                <input type="input" id="form-password" class="form-control" name="password" value="123456">
            </div> -->
            <div class="form-group" style="float:left;width:100px;">
                <button class="btn btn-success" type="submit">登陆</button>
            </div>
        </form>
    </div>

    <div style="width:600px;">
        <div class="form-group" style="float:left;width:100px;margin-right:10px;">
            <span style="height:30px;line-height:30px;">access-token :</span>
        </div>
        <div class="form-group" style="float:left;width:300px;margin-right:10px;">
            <input type="text" id="access-token" class="form-control" name="access-token" style="width:300px;" value="">
        </div>
        <div class="form-group" id="socket-connect" style="float:left;width:100px;display:none;">
            <a href="javascript:joinRoom();">
                <button class="btn btn-success">进入聊天房间</button>
            </a>
        </div>
    </div>

    <div style="width:600px;display:block;">
        <div id="chat-window" style="vertical-align:bottom; height:300px;display:block;width:500px;padding:10px;border:1px solid #cccccc;overflow-y:scroll;">
            <div style="height:1px;"></div>
        </div>
    </div>

    <div style="width:600px;margin-top:10px;display:block;">
        <div>
            <form action="#" onsubmit="sendMessage();return false;">
                <div class="form-group" style="float:left;width:400px;margin-right:10px;">
                    <input type="text" id="new-message" class="form-control" name="access-token" onkeydown="" style="width:400px;" value="">
                </div>
                <div class="form-group" style="float:left;width:100px;">
                    <button type="submit" class="btn btn-success">发言</button>
                </div>
            </form>
        </div>
    </div>

</div>

</body>

</html>