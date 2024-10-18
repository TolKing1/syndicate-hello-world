package com.task05.model;

import java.util.Map;

public class Event {
    private String id;
    private int principalId;
    private String createdAt;
    private Map<String, Object> body;

    private Event(Builder builder) {
        this.id = builder.id;
        this.principalId = builder.principalId;
        this.createdAt = builder.createdAt;
        this.body = builder.body;
    }

    public String getId() {
        return id;
    }

    public int getPrincipalId() {
        return principalId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public static class Builder {
        private String id;
        private int principalId;
        private String createdAt;
        private Map<String, Object> body;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withPrincipalId(int principalId) {
            this.principalId = principalId;
            return this;
        }

        public Builder withCreatedAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder withBody(Map<String, Object> body) {
            this.body = body;
            return this;
        }

        public Event build() {
            return new Event(this);
        }
    }
}
