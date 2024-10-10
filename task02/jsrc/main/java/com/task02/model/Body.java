package com.task02.model;


import java.util.HashMap;
import java.util.Map;

public class Body {
    private final Map<String, String> body;

    public Body(int statusCode, String message) {
        body = new HashMap<>();
        body.put("statusCode", String.valueOf(statusCode));
        body.put("message", message);
    }

    public Map<String, String> getBody() {
        return body;
    }
}
