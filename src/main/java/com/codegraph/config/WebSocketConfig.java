package com.codegraph.config;

import com.codegraph.judge.engine.EngineWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final EngineWebSocketHandler engineWebSocketHandler;

    public WebSocketConfig(EngineWebSocketHandler engineWebSocketHandler) {
        this.engineWebSocketHandler = engineWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(engineWebSocketHandler, "/ws/engine")
                .setAllowedOriginPatterns("*")
                .setAllowedOrigins("http://localhost:3000", "http://localhost:3001", "http://0.0.0.0:3000", "http://localhost:5173");
    }
}
