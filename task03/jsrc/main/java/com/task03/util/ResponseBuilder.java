package com.task03.util;

import java.util.Map;

public class ResponseBuilder {
    public static Map<String, Object> buildResponse(int statusCode, String responseBody) {
        return Map.of("statusCode", statusCode, "message", responseBody);
    }
}
