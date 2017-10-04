<!DOCTYPE html>
<html lang="zh-cn">

<head>
    <title>You draw, I guess</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta charset="utf-8"/>
    <meta content="yes" name="apple-mobile-web-app-capable"/>
    <meta content="yes" name="apple-touch-fullscreen"/>
    <meta content="telephone=no" name="format-detection"/>
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="#ffffff" name="msapplication-TileColor"/>
    <meta http-equiv="Expires" content="-1">
    <meta http-equiv="Cache-Control" content="no-store">
    <meta http-equiv="Pragma" content="no-cache">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
    <script src="/js/jquery-1.11.3.min.js"></script>
    <style>
        * {
            margin: 0;
            padding: 0;
            text-align: center;
            vertical-align: middle;
        }

        html, body {
        }

        body {
            background-color: #eeeeee;
            font-size: 14px;
            color: #5d656b;
        }

        a, button, input {
            outline: 0 none;
            text-decoration: none;
            -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
        }

        input {
            box-sizing: border-box;
        }

        .head-content {
            position: relative;
            left: 0;
            top: 0;
            width: 100%;
            display: block;
            height: 44px;
            line-height: 44px;
            background-color: #dddddd;
        }

        .body-content {
            width: 100%;
            text-align: center;
        }

        .center-content, .message-layout, .canvas-layout {
            top: 0;
            left: 0;
        }

        .center-content {
            position: relative;
            margin: 0 auto;
            max-width: 640px;
            min-width: 320px;
        }

        .message-layout {
            position: relative;
            display: block;
            vertical-align: bottom;
            width: 100%;
        }

        .canvas-layout {
            position: absolute;
            width: 100%;
        }

        .message-layout div {
            text-align: left;
            margin: 5px 110px;
        }
    </style>
    <script>

    </script>
</head>

<body>
<div class="body-content">
    <div class="head-content">
        <!-- <a href="mregist1.html"><span class="kt-icons">&#xf0c7;</span></a> -->
        <h2>狼人杀</h2>
    </div>
    <div class="center-content">
        <div class="message-layout">
            <div><b>第一晚</b></div>
            <div>* 狼人出来杀人</div>
            <div>* 狼人选择杀掉 2 号</div>
            <div>* 女巫对 2 号 ................. 说 see you</div>

            <div><b>第一天</b></div>
            <div>* 2 号留言</div>
            <div>* 村民对 2 号扼腕叹息</div>
            <div>* 翠花说 : 死鬼，活该</div>
            <div>* 安东说 : 啊，丫还没还钱呢</div>
            <div>* 晓华说 : 叉，我的 iPhone X</div>
            <div>* 陈老师说 : 陈家沟居然有狼 ...</div>
            <div>* 方(wo)汉(fu) 笑而不语</div>

            <div><b>第二晚</b></div>
            <div>* 狼人又出来杀人了</div>
            <div>* 狼人选择杀掉 3 号</div>
            <div>* 女巫对 3 号 ................. 说 see you</div>
        </div>
        <div class="canvas-layout" game='lrs-replay' game-data="lrsReplayData1">
            <script>
                window.lrsReplayData1 = {
                    "playerList": [
                        {face: "img/face/t1x2.png"},
                        {face: "img/face/t2x2.png"},
                        {face: "img/face/t3x2.png"},
                        {face: "img/face/t4x2.png"},
                        {face: "img/face/t5x2.png"},
                        {face: "img/face/t6x2.png"},
                        {face: "img/face/t7x2.png"},
                        {face: "img/face/t8x2.png"},
                        {face: "img/face/t9x2.png"},
                        {face: "img/face/t10x2.png"},
                        {face: "img/face/t11x2.png"},
                        {face: "img/face/t12x2.png"}
                    ],
                    "playData": [
                        {
                            "time": "night",
                            "actions": [
                                // 狼人出来杀人
                                {action:"wolf-killing", "data":{targetPlayer:1}},
                                // 女巫下药
                                {action:"witch-poison", "data":{targetPlayer:1}},
                                // 女巫救人
                                {action:"witch-save-life", "data":{targetPlayer:1}}
                            ]
                        },
                        {
                            "time": "day",
                            "actions": [
                                // 平安夜
                                {"action": "is-silent-night"},
                                // 有人挂
                                {"action": "player-die", "data":{player:[1,2,3]}},
                                // 发言
                                {"action": "player-speak", "data":{player:1, message:"who kill me ? fuck"}},
                                {"action": "player-speak", "data":{player:2, message:"who kill me ? fuck"}},
                                {"action": "player-speak", "data":{player:3, message:"who kill me ? fuck"}},
                                {"action": "player-speak", "data":{player:4, message:"who kill me ? fuck"}},
                                {"action": "player-speak", "data":{player:5, message:"who kill me ? fuck"}},
                                {"action": "player-speak", "data":{player:6, message:"who kill me ? fuck"}},
                                {"action": "player-speak", "data":{player:7, message:"who kill me ? fuck"}},
                                {"action": "player-speak", "data":{player:8, message:"who kill me ? fuck"}},
                                {"action": "player-speak", "data":{player:9, message:"who kill me ? fuck"}},
                                // 玩家投票
                                {"action": "player-vote", "data":{result:[
                                    {voter:[1,2,3], voterFor:1},
                                    {voter:[4,5], voterFor:2}
                                ]}},
                                // 猎人生气了
                                {"action": "被杀的是猎人，发动技能"},
                                // 发言
                                {"action": "player-speak", data:{player:1, message:"who kill me ? fuck"}}
                            ]
                        }
                    ]
                };
            </script>
        </div>
    </div>
</div>
</body>
</html>