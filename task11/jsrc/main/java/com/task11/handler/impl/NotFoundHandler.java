package com.task11.handler.impl;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.task11.handler.Handler;
import org.json.JSONObject;

import static com.task11.util.ResourceNames.SC_501;

public class NotFoundHandler implements Handler {
    @Override
    public APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent requestEvent) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(SC_501)
                .withBody(
                        new JSONObject().put(
                                "message",
                                "Handler for the %s method on the %s path is not implemented."
                                        .formatted(requestEvent.getHttpMethod(), requestEvent.getPath())
                        ).toString()
                );
    }
}
