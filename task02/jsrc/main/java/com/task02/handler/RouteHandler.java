package com.task02.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.task02.model.RouteKey;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class  RouteHandler {
    private final Map<RouteKey, Function<APIGatewayV2HTTPEvent, Map<String, Object>>> handlers = Map.of(
            new RouteKey("GET", "/hello"), this::handleGetHello
    );

    public Map<String, Object> handleRequest(APIGatewayV2HTTPEvent requestEvent) {
        RouteKey routeKey = new RouteKey(getMethod(requestEvent), getPath(requestEvent));
        return handlers.getOrDefault(routeKey, this::notFoundResponse).apply(requestEvent);
    }

    private Map<String, Object> handleGetHello(APIGatewayV2HTTPEvent requestEvent) {
        String message = "Hello from lambda";
        return buildResponse(200, message);
    }

    private Map<String, Object> notFoundResponse(APIGatewayV2HTTPEvent requestEvent) {
        String message = String.format("Bad request syntax or unsupported method. Request path: %s. HTTP method: %s",
                getPath(requestEvent), getMethod(requestEvent));
        return buildResponse(400, message);
    }

    private Map<String, Object> buildResponse(int statusCode, String responseBody) {
        Map<String, Object> map = new HashMap<>();
        map.put("statusCode", statusCode);
        map.put("message", responseBody);

        return map;
    }

    private String getMethod(APIGatewayV2HTTPEvent requestEvent) {
        return requestEvent.getRequestContext().getHttp().getMethod();
    }

    private String getPath(APIGatewayV2HTTPEvent requestEvent) {
        return requestEvent.getRequestContext().getHttp().getPath();
    }
}

