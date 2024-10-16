package com.task03.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.task03.util.ResponseBuilder;

import java.util.Map;

public class NotFoundHandler implements Handler {
    @Override
    public Map<String, Object> handle(APIGatewayV2HTTPEvent requestEvent) {
        String rawPath = requestEvent.getRequestContext().getHttp().getPath();
        String method = requestEvent.getRequestContext().getHttp().getMethod();
        String message = "{\"statusCode\": 400, \"message\": \"" + String.format( "Bad request syntax or unsupported method. Request path: %s. HTTP method: %s", rawPath, method) + "\"}";

        return ResponseBuilder.buildResponse(400, message);
    }
}
