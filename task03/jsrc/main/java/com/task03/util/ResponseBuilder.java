package com.task03.util;

import java.util.HashMap;
import java.util.Map;

public class ResponseBuilder {
    public static Map<String, Object> buildResponse(int statusCode, String responseBody) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("statusCode", statusCode);
        resultMap.put("body", responseBody);
        return resultMap;
    }
}
