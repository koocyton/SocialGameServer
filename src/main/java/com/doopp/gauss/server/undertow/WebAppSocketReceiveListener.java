package com.doopp.gauss.server.undertow;

import io.undertow.websockets.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class WebAppSocketReceiveListener extends AbstractReceiveListener {

    private static final Map<String, WebSocketChannel> allWebSocketChannels = new HashMap<>();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message)
    {
        WebSockets.sendText(message.getData(), channel, null);
    }

    @Override
    protected void onFullBinaryMessage(WebSocketChannel channel, BufferedBinaryMessage message) throws IOException
    {
        super.onFullBinaryMessage(channel, message);
    }

    @Override
    protected void onClose(WebSocketChannel webSocketChannel, StreamSourceFrameChannel channel) throws IOException
    {
        super.onClose(webSocketChannel, channel);
    }

    public void addChannel(WebSocketChannel channel) {
        allWebSocketChannels.put(channel, channel.getAttribute("access-token"));
    }

    public void delChannel(WebSocketChannel channel) {

    }
}
