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
        var msg = [];
        var addMsg = [];
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
            ws = new WebSocket("ws://j.doopp.com/game-socket?access-token=" + accessToken);
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
                    let obj = JSON.parse(e.data);
                    if (typeof(obj.data.target)!="undefined" && obj.data.target=="canvas-marquee") {

                        console.log(obj);
                        let canvas = document.getElementById('mycanvas');
                        let marguess2d = canvas.getContext("2d");
                        //设置字体样式
                        marguess2d.font = "20px Courier New";
                        //设置字体颜色
                        marguess2d.strokeStyle = "blue";
                        //从坐标点(50,50)开始绘制文字
                        let fontX = 600;
                        let fontY = Math.floor(Math.random() * 200 + 40);
                        let message = obj.data.sender + " : " + obj.data.message;
                        marguess2d.strokeText(message, fontX, fontY);
                        //
                        // setTimeout(marguess2d, 600, randomY);
                        // console.log(randomY);
                        // marguessMove(marguess2d, fontX, fontY);
                        let nn = addMsg.length;
                        // console.log(nn);
                        let speed = Math.floor(Math.random() * 4);
                        addMsg[nn] = {"ctx":marguess2d, "speed": speed, "x":fontX, "y":fontY, "message": message};
                    }
                    else if (typeof(obj.data.target)!="undefined" && obj.data.target=="drawing-board") {
                        if (obj.data.sender!=$("#form-account").val()) {
                            let canvas = document.getElementById('mycanvas2');
                            let cxt = canvas.getContext("2d");
                            cxt.strokeStyle = "blue";
                            cxt.lineTo(obj.data.x, obj.data.y);
                            cxt.stroke();
                        }
                    }
                    else if (typeof(obj.data.target)!="undefined" && obj.data.target=="drawing-move") {
                        if (obj.data.sender!=$("#form-account").val()) {
                            let canvas = document.getElementById('mycanvas2');
                            let cxt = canvas.getContext("2d");
                            cxt.strokeStyle = "blue";
                            cxt.moveTo(obj.data.x, obj.data.y);
                            // cxt.lineTo(obj.data.x, obj.data.y);
                            // cxt.stroke();
                        }
                    }
                    else {
                        newMsg.innerHTML = "<b style=\"font-size:15px;color:blue;\">" + obj.data.sender + " </b> &gt; " + obj.data.message;
                    }
                }
                catch(e) {
                    // console.log(e);
                    newMsg.innerHTML = message;
                }
                let lastChild = showMsgElt.lastChild;// firstChild;
                showMsgElt.insertBefore(newMsg, lastChild);
                showMsgElt.scrollTop = showMsgElt.scrollHeight;
            };
            return false;
        }

        function cleanCanvas() {
            let _canvas = document.getElementById('mycanvas2');
            let ctx = _canvas.getContext("2d");
            ctx.strokeStyle = "whitesmoke";
            ctx.save();
            ctx.beginPath();
            ctx.rect(-10, -10, _canvas.width+20 , _canvas.height+20 );
            ctx.clip();
            ctx.clearRect(-10, -10, _canvas.width + 20, _canvas.height + 20);
            ctx.restore();
            // ctx.strokeStyle = "blue";


            let _canvas2 = document.getElementById('mycanvas3');
            let ctx2 = _canvas2.getContext("2d");
            ctx2.strokeStyle = "whitesmoke";
            ctx2.save();
            ctx2.beginPath();
            ctx2.rect(-10, -10, _canvas.width+20 , _canvas.height+20 );
            ctx2.clip();
            ctx2.clearRect(-10, -10, _canvas.width + 20, _canvas.height + 20);
            ctx2.restore();
        }

        function marguessMove() {
            for(let ii=0; ii<msg.length; ii++) {
                if (msg[ii].x < -1000) {
                    msg[ii].ctx = null;
                    msg[ii] = null;
                    msg.splice(ii, 1);
                }
            }
            for (let ii=0; ii<addMsg.length; ii++) {
                let xx = msg.length;
                msg[xx] = addMsg[ii];
            }
            // console.log(addMsg, msg);
            addMsg = [];
            for(let ii=0; ii<msg.length; ii++) {
                if (ii==0) {
                    msg[ii].ctx.save();
                    msg[ii].ctx.beginPath();
                    msg[ii].ctx.rect(-10, -10, msg[ii].ctx.canvas.width + 20, msg[ii].ctx.canvas.height + 20);
                    msg[ii].ctx.clip();
                    msg[ii].ctx.clearRect(-10, -10, msg[ii].ctx.canvas.width + 20, msg[ii].ctx.canvas.height + 20);
                    msg[ii].ctx.restore();
                }
                msg[ii].ctx.strokeText(msg[ii].message, msg[ii].x, msg[ii].y);
                msg[ii].x = msg[ii].x - msg[ii].speed;
            }
            setTimeout(function () {
                marguessMove();
            }, 10);
        }
        marguessMove();

        function sendMessage() {
            let elt = $("#new-message");
            let msg = elt.val();
            let json = "{\"action\":\"room-chat\", \"data\":{\"message\":\"" + msg + "\"}}";
            ws.send(json);
            elt.val("");
            elt.focus();
            // console.log("Message sent : " + json);
        }

        function sendMarquee() {
            let elt = $("#new-marquee");
            let msg = elt.val();
            let json = "{\"action\":\"room-chat\", \"data\":{\"target\":\"canvas-marquee\", \"message\":\"" + msg + "\"}}";
            ws.send(json);
            elt.val("");
            elt.focus();
            // console.log("marquee sent : " + json);
        }

        function sendDrawing(x, y) {
            let elt = $("#new-marquee");
            let msg = elt.val();
            let json = "{\"action\":\"room-chat\", \"data\":{\"target\":\"drawing-board\", \"x\":\"" + x + "\", \"y\":\"" + y + "\"}}";
            ws.send(json);
            elt.val("");
            elt.focus();
            // console.log("marquee sent : " + json);
        }

        function sendDrawingMove(x, y) {
            let elt = $("#new-marquee");
            let msg = elt.val();
            let json = "{\"action\":\"room-chat\", \"data\":{\"target\":\"drawing-move\", \"x\":\"" + x + "\", \"y\":\"" + y + "\"}}";
            ws.send(json);
            elt.val("");
            elt.focus();
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
        <td style="width:50%;position:relative" rowspan="3">
            <canvas id="mycanvas" width="600" height="400" style="background-color:whitesmoke;"></canvas>
            <canvas id="mycanvas2" width="600" height="400" style="position:absolute;left:0px;"></canvas>
            <canvas id="mycanvas3" width="600" height="400" style="position:absolute;left:0px;"></canvas>
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


        <td>
            <div class="form-group">
                <form action="#" onsubmit="sendMarquee();return false;">
                    <div class="form-group" style="float:left;width:60%;margin-right:10px;">
                        <input type="text" id="new-marquee" class="form-control" name="access-token" onkeydown="" value="">
                    </div>
                    <div class="form-group" style="float:left;width:10%;margin-left:2px;">
                        <button type="submit" class="btn btn-success">发弹幕</button>
                    </div>
                </form>

                <div class="form-group" style="float:left;width:10%;margin-left:8px;">
                    <button type="submit" onclick="cleanCanvas();" class="btn btn-default">清屏</button>
                </div>
            </div>
        </td>
    </tr>
</table>

<script type="text/javascript">

    function canvasBrush() {
        let canvas = document.getElementById('mycanvas3');
        let cxt = canvas.getContext("2d");
        let flag = false;//判断鼠标是否按下
        canvas.onmousedown = function (e) {
            cxt.strokeStyle = "blue";
            flag = true;
            cxt.moveTo(e.layerX, e.layerY);
            sendDrawingMove(e.layerX, e.layerY);
        };
        canvas.onmousemove = function (e) {
            if (flag) {
                sendDrawing(e.layerX, e.layerY);
                cxt.lineTo(e.layerX, e.layerY);
                cxt.stroke();
            }
        };
        canvas.onmouseup = function () {
            flag = false;
            cxt.strokeStyle = "whitesmoke";
        };
        canvas.onmouseout = function () {
            flag = false;
            cxt.strokeStyle = "whitesmoke";
        };
    }
    canvasBrush();
</script>

</body>
</html>