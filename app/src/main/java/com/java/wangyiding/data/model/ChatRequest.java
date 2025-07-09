package com.java.wangyiding.data.model;

import java.util.List;

public class ChatRequest {
    public String model = "glm-4"; // 或 "chatglm4" 根据实际
    public List<ChatMessage> messages;
    public String request_id;
    public Boolean stream = false;

    public ChatRequest(String requestId, List<ChatMessage> messages) {
        this.request_id = requestId;
        this.messages = messages;
    }
}