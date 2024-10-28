package com.task10.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;

import java.util.Map;

public interface Handler {
    APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent requestEvent);
}
