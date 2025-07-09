package com.java.wangyiding.data.model;

public class ChatMessage {
    public String role;
    public String content;

    public  ChatMessage(String _role,String _content)
    {
        role=_role;
        content=_content;
    }
}
