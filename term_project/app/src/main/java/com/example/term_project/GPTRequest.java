package com.example.term_project;

import android.os.Message;

import java.util.List;

// GPTRequest.java
public class GPTRequest {
    private String model;
    private List<Message> messages;

    private String prompt;

    public GPTRequest(String prompt) {
        this.model = "gpt-3.5-turbo"; // GPT 모델 선택
        this.messages = List.of(new Message("user", prompt));
    }

    // Getters and Setters
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    // 내부 Message 클래스
    public static class Message {
        private String role;
        private String content;

        // Message 생성자
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
