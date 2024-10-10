package com.task02.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;

import java.util.Map;

public interface Handler {
    Map<String, Map<String, String>>     handle(APIGatewayV2HTTPEvent requestEvent);
}