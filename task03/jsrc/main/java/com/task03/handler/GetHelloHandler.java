package com.task03.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.task03.util.ResponseBuilder;

import java.util.Map;

public class GetHelloHandler implements Handler {
    @Override
    public Map<String, Object> handle(APIGatewayV2HTTPEvent requestEvent) {
        return ResponseBuilder.buildResponse(200, "{\"statusCode\": 200, \"message\": \"Hello from Lambda\"}");
    }
}
