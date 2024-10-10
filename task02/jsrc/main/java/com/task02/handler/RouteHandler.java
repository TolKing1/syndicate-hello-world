package com.task02.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.task02.model.RouteKey;

import java.util.HashMap;
import java.util.Map;

public class  RouteHandler {
    private final Map<RouteKey, Handler> handlers = new HashMap<>();

    public RouteHandler() {
        handlers.put(new RouteKey("GET", "/hello"), new GetHelloHandler());
    }

    public Map<String, Object> handleRequest(APIGatewayV2HTTPEvent requestEvent) {
        RouteKey routeKey = new RouteKey(getMethod(requestEvent), getPath(requestEvent));
        Handler handler = handlers.getOrDefault(routeKey, new NotFoundHandler());
        return handler.handle(requestEvent);
    }

    private String getMethod(APIGatewayV2HTTPEvent requestEvent) {
        return requestEvent.getRequestContext().getHttp().getMethod();
    }

    private String getPath(APIGatewayV2HTTPEvent requestEvent) {
        return requestEvent.getRequestContext().getHttp().getPath();
    }
}

