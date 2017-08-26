<%--
  Created by IntelliJ IDEA.
  User: henry
  Date: 2017/7/5
  Time: 下午6:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>WebSocket</title>
</head>
<body>
<div>
    host : <input id="socket-server" value="ws://localhost:8080/game-socket" style="width:500px">
    <br>
    access-token : <input id="access-token" value="a8d865913a1b71e2cd658a8481cd32ff" style="width:500px">
    <br>
    <button onclick="setup()">开始连接</button>
</div>
<br>
<div>
    <textarea style="width:500px;height:100px;" id="message-input"></textarea>
    <button onclick="sendMessage()"> &nbsp; 发送 &nbsp; </button>
</div>

<script>
    let ws = null;
    function setup() {
        let socketServer = document.getElementById("socket-server").value;
        ws = new WebSocket(socketServer);
        // Listen for the connection open event then call the sendMessage function
        ws.onopen = function(e) {
            log("WebSocket Connected ...");
            // let accessToken = document.getElementById("access-token").value;
            // sendMessage({"access-token":accessToken})
        }
        // Listen for the close connection event
        ws.onclose = function(e) {
            log("Disconnected: " + e.reason);
        }
        // Listen for connection errors
        ws.onerror = function(e) {
            log("Error ");
        }
        // Listen for new messages arriving at the client
        ws.onmessage = function(e) {
            log("Message received: " + e.data);
            let showMsgElt = document.getElementById("get-message");
            let newMsg = document.createElement("div");
            newMsg.innerHTML = e.data;
            let firstChild = showMsgElt.firstChild;
            showMsgElt.insertBefore(newMsg, firstChild);
            // Close the socket once one message has arrived.
            // ws.close();
        }
    }
    // Send a message on the WebSocket.
    function sendMessage(){
        let msg = document.getElementById("message-input").value;
        ws.send(msg);
        log("Message sent : " + msg);
    }
    // Display logging information in the document.
    function log(s) {
        console.log(s);
    }
</script>
<div id="get-message" style="height:400px;width:500px;padding:10px;border:1px solid #cccccc;overflow-y:scroll;"></div>
</body>
</html>