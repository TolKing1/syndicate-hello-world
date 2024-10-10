package com.task02.util;

import com.task02.model.Body;

import java.util.HashMap;
import java.util.Map;

public class ResponseBuilder {
    public static Map<String, Map<String, String>> buildResponse(int statusCode, String responseBody) {
        Map<String, Map<String, String>> map = new HashMap<>();
        map.put("body", new Body(statusCode, responseBody).getBody());
        return map;
    }
}
