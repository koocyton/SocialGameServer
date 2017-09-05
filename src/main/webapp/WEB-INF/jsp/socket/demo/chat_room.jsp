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

<body style="padding:15px;">


<table style="width:100%;">
    <tr>
        <td style="width:50%;">
            <form action="/api/v1/fast-login" id="login-form" method="post" onsubmit="onLogin();return false;">
                <div class="form-group" style="float:left;width:60%;margin-right:10px;">
                    <input type="input" id="form-account" class="form-control" name="account" value="">
                </div>
                <!-- <div class="form-group" style="float:left;width:200px;margin-right:10px;">
                    <input type="input" id="form-password" class="form-control" name="password" value="123456">
                </div> -->
                <div class="form-group" style="float:left;width:30%;">
                    <button class="btn btn-success" type="submit">登陆</button>
                </div>
            </form>
        </td>
        <td style="width:50%;" rowspan="4">
            <canvas id="mycanvas" width="100%" height="400" style="width:100%;height:400px;background-color:whitesmoke;"></canvas>
            <script type="text/javascript">
                let canvas = document.getElementById('mycanvas');
                let cxt = canvas.getContext("2d");
                cxt.strokeStyle="black";
                let flag = false;//判断鼠标是否按下
                canvas.onmousedown = function (e) {
                    flag = true;
                    //e是鼠标按下事件，this是画布canvas.
                    //pageX是相对于浏览器的，offsetLeft是相对于父级容器的
                    let startx = e.pageX-this.offsetLeft;
                    let starty = e.pageY-this.offsetTop;
                    cxt.moveTo(startx,starty);
                };
                canvas.onmousemove = function (e) {
                    let endx = e.pageX-this.offsetLeft;
                    let endy = e.pageY-this.offsetTop;
                    // console.log(e.pageX, this.offsetLeft, e);
                    if(flag){
                        cxt.lineTo(endx,endy);
                        cxt.stroke();
                    }
                };
                canvas.onmouseup = function(){
                    flag = false;
                };
                canvas.onmouseout = function(){
                    flag = false;
                };
            </script>
        </td>
    </tr>
    <tr>
        <td>
            <div class="form-group" style="float:left;width:60%;margin-right:10px;">
                <input type="text" readonly="1" id="access-token" class="form-control" name="access-token" placeholder="Access Token" value="">
            </div>
            <div class="form-group" id="socket-connect" style="float:left;width:30%;display:none;">
                <button onclick="joinRoom()" class="btn btn-success">加入游戏</button>
            </div>
        </td>
    </tr>
    <tr>
        <td>
            <div class="form-group">
                <div id="chat-window" class="form-control" style="vertical-align:bottom; height:300px;display:block;width:90%;padding:10px;border:1px solid #cccccc;overflow-y:scroll;">
                    <div style="height:1px;"></div>
                </div>
            </div>
        </td>
    </tr>
    <tr>
        <td>
            <div class="form-group">
                <form action="#" onsubmit="sendMessage();return false;">
                    <div class="form-group" style="float:left;width:80%;margin-right:10px;">
                        <input type="text" id="new-message" class="form-control" name="access-token" onkeydown="" value="">
                    </div>
                    <div class="form-group" style="float:left;width:10%;">
                        <button type="submit" class="btn btn-success">发言</button>
                    </div>
                </form>
            </div>
        </td>
    </tr>
</table>

</body>
</html>