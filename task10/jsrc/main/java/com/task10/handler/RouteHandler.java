package com.task10.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.task10.handler.impl.*;
import com.task10.model.RouteKey;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

import java.util.HashMap;
import java.util.Map;

public class  RouteHandler {
    private final Map<RouteKey, Handler> handlers = new HashMap<>();

    public RouteHandler(CognitoIdentityProviderClient cognitoProvider) {

        handlers.put(new RouteKey( "/signup", "POST"), new CreateAccountHandler(cognitoProvider));
        handlers.put(new RouteKey( "/signin", "POST"), new LoginHandler(cognitoProvider));
        handlers.put(new RouteKey( "/tables","POST"), new TableCreateHandler());
        handlers.put(new RouteKey( "/tables", "GET"), new TableGetHandler());
        handlers.put(new RouteKey("/tables/{tableId}" ,"GET"), new TableGetByIdHandler());
        handlers.put(new RouteKey( "/reservations", "POST"), new ReservationCreateHandler());
        handlers.put(new RouteKey( "/reservations", "GET"), new ReservationGetHandler());
    }

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent) {
        RouteKey routeKey = getRouteKey(requestEvent);
        Handler handler = handlers.getOrDefault(routeKey, new NotFoundHandler());
        return handler.handle(requestEvent);
    }

    private RouteKey getRouteKey(APIGatewayProxyRequestEvent requestEvent) {
        String path = filterTableId(requestEvent);
        return new RouteKey(path, requestEvent.getHttpMethod());
    }

    private static String filterTableId(APIGatewayProxyRequestEvent requestEvent) {
        String path = requestEvent.getPath();
        if (path.matches("/tables/\\d+")) {
            path = "/tables/{tableId}";
        }
        return path;
    }

    private String getMethod(APIGatewayProxyRequestEvent requestEvent) {
        return requestEvent.getHttpMethod();
    }

    private String getPath(APIGatewayProxyRequestEvent requestEvent) {
        return requestEvent.getPath();
    }
}

