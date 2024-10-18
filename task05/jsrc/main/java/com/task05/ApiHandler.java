package com.task05;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.syndicate.deployment.annotations.environment.EnvironmentVariable;
import com.syndicate.deployment.annotations.environment.EnvironmentVariables;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.Architecture;
import com.syndicate.deployment.model.DeploymentRuntime;
import com.syndicate.deployment.model.RetentionSetting;
import com.task05.dto.ResponseBody;
import com.task05.model.Event;
import com.task05.service.EventTableService;
import com.task05.service.impl.EventTableServiceImpl;
import com.task05.util.JsonUtil;
import org.json.JSONObject;

@LambdaHandler(
    lambdaName = "api_handler",
	roleName = "api_handler-role",
		architecture = Architecture.X86_64,
	runtime = DeploymentRuntime.JAVA17,
	aliasName = "${lambdas_alias_name}",
	logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@EnvironmentVariables(value = {
		@EnvironmentVariable(key = "table", value = "${target_table}"),
		@EnvironmentVariable(key = "region", value = "${region}"),
})
public class ApiHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
	private final EventTableService eventTableService;


	public ApiHandler() {
		this.eventTableService = new EventTableServiceImpl(
				System.getenv("region"),
				System.getenv("table")
		);
	}


	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
		context.getLogger().log("Receiving request: %s".formatted(request));
		JSONObject requestBody = JsonUtil.convertJsonToJsonObject(request.getBody());
		context.getLogger().log("Getting body: %s".formatted(requestBody));

        Event event = eventTableService.putItem(
				requestBody.getInt("principalId"),
				requestBody.getJSONObject("body").toMap()
		);
		context.getLogger().log("Getting event: %s".formatted(event));
        return getApiGatewayProxyResponseEvent(event);
	}

	private static APIGatewayProxyResponseEvent getApiGatewayProxyResponseEvent(Event event) {
		APIGatewayProxyResponseEvent apiResponse = new APIGatewayProxyResponseEvent();
		ResponseBody responseBody = new ResponseBody(201, event);
		apiResponse.setStatusCode(201);
		apiResponse.setBody(JsonUtil.convertObjectToJson(responseBody));
		return apiResponse;
	}
}
