package com.task03.util;

import java.util.Map;

public class ResponseBuilder {
    public static Map<String, Object> buildResponse(int statusCode, String responseBody) {
        return Map.of("body", Map.of("statusCode", String.valueOf(statusCode), "message", responseBody));
    }
}
