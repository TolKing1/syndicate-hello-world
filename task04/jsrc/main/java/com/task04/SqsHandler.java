package com.task04;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.resources.DependsOn;
import com.syndicate.deployment.model.ResourceType;
import com.syndicate.deployment.model.RetentionSetting;

import java.util.HashMap;
import java.util.Map;

@LambdaHandler(
    lambdaName = "sqs_handler",
	roleName = "sqs_handler-role",
	isPublishVersion = true,
	aliasName = "${lambdas_alias_name}",
	logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@DependsOn(
		resourceType = ResourceType.SQS_QUEUE,
		name = "async_queue"
)
public class SqsHandler implements RequestHandler<SQSEvent , Map<String, Object>> {

	public Map<String, Object> handleRequest(SQSEvent sqsEvent, Context context) {
		Map<String, Object> resultMap = new HashMap<>();
		sqsEvent.getRecords().forEach(record -> {
			resultMap.put(record.getMessageId(), record.getBody());
			System.out.println(record.getBody());
		});
		return resultMap;
	}
}
