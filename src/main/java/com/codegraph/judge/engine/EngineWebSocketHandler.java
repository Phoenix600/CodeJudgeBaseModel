package com.codegraph.judge.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class EngineWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private String engineStatus = "ONLINE";
    private final long startTime = System.currentTimeMillis();

    public EngineWebSocketHandler() {
        // Ping sessions periodically to simulate heartbeats/latency checks
        scheduler.scheduleAtFixedRate(this::broadcastStatus, 0, 5, TimeUnit.SECONDS);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        
        // Initial greeting: Check if session is still open and catch transient IO errors (Broken pipe)
        try {
            if (session.isOpen()) {
                sendStatus(session);
            }
        } catch (IOException e) {
            // This happens if the user refreshes the page immediately after connecting
            sessions.remove(session.getId());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Map<String, Object> payload = objectMapper.readValue(message.getPayload(), Map.class);
        String type = (String) payload.get("type");

        if ("COMMAND".equals(type)) {
            String command = (String) payload.get("command");
            if ("START".equals(command)) {
                engineStatus = "ONLINE";
                broadcastStatus();
            } else if ("STOP".equals(command)) {
                engineStatus = "OFFLINE";
                broadcastStatus();
                // Shutdown the engine process after a small delay to allow status update
                scheduler.schedule(() -> {
                    System.out.println("Shutting down engine as requested by frontend...");
                    System.exit(0);
                }, 1, TimeUnit.SECONDS);
            }
        } else if ("PING".equals(type)) {
            sendPong(session, (Long) payload.get("timestamp"));
        }
    }

    private void broadcastStatus() {
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                try {
                    sendStatus(session);
                } catch (IOException e) {
                    sessions.remove(session.getId());
                }
            }
        }
    }

    private void sendStatus(WebSocketSession session) throws IOException {
        long uptime = System.currentTimeMillis() - startTime;
        
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long usedMemory = memoryBean.getHeapMemoryUsage().getUsed() / (1024 * 1024);
        long maxMemory = memoryBean.getHeapMemoryUsage().getMax() / (1024 * 1024);
        
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        double load = osBean.getSystemLoadAverage();
        int cores = osBean.getAvailableProcessors();

        Map<String, Object> response = new HashMap<>();
        response.put("status", engineStatus);
        response.put("timestamp", System.currentTimeMillis());
        response.put("uptime", uptime);
        response.put("memory", String.format("%dMB / %dMB", usedMemory, maxMemory));
        response.put("cpuLoad", load < 0 ? "N/A" : String.format("%.2f", load));
        response.put("cores", cores);
        response.put("sessions", sessions.size());
        response.put("os", osBean.getName() + " " + osBean.getArch());
        
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
    }

    private void sendPong(WebSocketSession session, Long timestamp) throws IOException {
        Map<String, Object> response = Map.of(
                "type", "PONG",
                "timestamp", timestamp
        );
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
    }
}
