package com.task03;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.RetentionSetting;
import com.syndicate.deployment.model.lambda.url.AuthType;
import com.task03.util.ResponseBuilder;

import java.util.Map;

@LambdaHandler(
    lambdaName = "hello_world",
	roleName = "hello_lambda-role",
	isPublishVersion = true,
	aliasName = "${lambdas_alias_name}",
	logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@LambdaUrlConfig(
		authType = AuthType.NONE
)
public class HelloLambda implements RequestHandler<APIGatewayV2HTTPEvent, Map<String, Object>> {


	@Override
	public Map<String, Object> handleRequest(APIGatewayV2HTTPEvent apiGatewayV2HTTPEvent, Context context) {

		return ResponseBuilder.buildResponse(200, "{\"statusCode\": 200, \"message\": \"Hello from Lambda\"}");
	}
}
