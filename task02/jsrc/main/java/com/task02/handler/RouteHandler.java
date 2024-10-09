package com.task02.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.task02.model.RouteKey;

import java.util.Map;
import java.util.function.Function;

public class  RouteHandler {
    private final Map<RouteKey, Function<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse>> handlers = Map.of(
            new RouteKey("GET", "/hello"), this::handleGetHello
    );

    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent requestEvent) {
        RouteKey routeKey = new RouteKey(getMethod(requestEvent), getPath(requestEvent));
        return handlers.getOrDefault(routeKey, this::notFoundResponse).apply(requestEvent);
    }

    private APIGatewayV2HTTPResponse handleGetHello(APIGatewayV2HTTPEvent requestEvent) {
        String message = "Hello from lambda";
        return buildResponse(200, message);
    }

    private APIGatewayV2HTTPResponse notFoundResponse(APIGatewayV2HTTPEvent requestEvent) {
        String message = String.format("Bad request syntax or unsupported method. Request path: %s. HTTP method: %s",
                getPath(requestEvent), getMethod(requestEvent));
        return buildResponse(400, message);
    }

    private APIGatewayV2HTTPResponse buildResponse(int statusCode, String responseBody) {
        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(statusCode)
                .withBody(responseBody)
                .build();
    }

    private String getMethod(APIGatewayV2HTTPEvent requestEvent) {
        return requestEvent.getRequestContext().getHttp().getMethod();
    }

    private String getPath(APIGatewayV2HTTPEvent requestEvent) {
        return requestEvent.getRequestContext().getHttp().getPath();
    }
}

