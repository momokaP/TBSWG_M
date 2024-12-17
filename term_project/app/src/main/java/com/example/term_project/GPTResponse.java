package com.example.term_project;

import java.util.List;

public class GPTResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<Choice> choices;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getObject() {
        return object;
    }

    public long getCreated() {
        return created;
    }

    public String getModel() {
        return model;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public static class Choice {
        private int index;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }

        private Message message;

        // Getter and Setter

        public static class Message {
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

            private String role;
            private String content;

            // Getter and Setter
        }
    }
}