package com.task02.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.task02.util.ResponseBuilder;

import java.util.Map;

public class NotFoundHandler implements Handler {
    @Override
    public Map<String, Map<String, String>> handle(APIGatewayV2HTTPEvent requestEvent) {
        String message = String.format("Bad request syntax or unsupported method. Request path: %s. HTTP method: %s",
                requestEvent.getRequestContext().getHttp().getPath(),
                requestEvent.getRequestContext().getHttp().getMethod());
        return ResponseBuilder.buildResponse(400, message);
    }
}
