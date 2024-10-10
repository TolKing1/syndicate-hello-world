package com.task02.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.task02.util.ResponseBuilder;

import java.util.Map;

public class GetHelloHandler implements Handler {
    @Override
    public Map<String, Map<String, String>> handle(APIGatewayV2HTTPEvent requestEvent) {
        return ResponseBuilder.buildResponse(200, "Hello from lambda");
    }
}
