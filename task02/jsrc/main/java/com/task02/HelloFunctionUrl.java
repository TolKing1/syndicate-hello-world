package com.task02;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.RetentionSetting;
import com.syndicate.deployment.model.lambda.url.AuthType;
import com.task02.handler.RouteHandler;

import java.util.Map;

@LambdaHandler(
    lambdaName = "hello_world",
	roleName = "hello-function-url-role",
	aliasName = "${lambdas_alias_name}",
	logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@LambdaUrlConfig(
		authType = AuthType.NONE
)
public class HelloFunctionUrl implements RequestHandler<APIGatewayV2HTTPEvent, Map<String, Map<String, String>>> {
	private final RouteHandler routeHandler = new RouteHandler();


	@Override
	public Map<String, Map<String, String>> handleRequest(APIGatewayV2HTTPEvent apiGatewayV2HTTPEvent, Context context) {
		return routeHandler.handleRequest(apiGatewayV2HTTPEvent);
	}
}
