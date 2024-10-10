package com.task02.util;

import com.task02.model.Body;

import java.util.Map;

public class ResponseBuilder {
    public static Map<String, String> buildResponse(int statusCode, String responseBody) {
        return new Body(statusCode, responseBody).getBody();
    }
}
