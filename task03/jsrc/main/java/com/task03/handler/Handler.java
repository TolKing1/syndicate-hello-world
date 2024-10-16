package com.task03.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;

import java.util.Map;

public interface Handler {
    Map<String, Object> handle(APIGatewayV2HTTPEvent requestEvent);
}
