package com.task06;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.syndicate.deployment.annotations.environment.EnvironmentVariable;
import com.syndicate.deployment.annotations.environment.EnvironmentVariables;
import com.syndicate.deployment.annotations.events.DynamoDbTriggerEventSource;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.RetentionSetting;
import com.task06.service.AuditTableService;
import com.task06.service.impl.AuditTableServiceImpl;

@LambdaHandler(
    lambdaName = "audit_producer",
	roleName = "audit_producer-role",
	aliasName = "${lambdas_alias_name}",
	logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@DynamoDbTriggerEventSource(
		targetTable = "${config_table}",
		batchSize = 10
)
@EnvironmentVariables(value = {
		@EnvironmentVariable(key = "region", value = "${region}"),
		@EnvironmentVariable(key = "table", value = "${target_table}")
})
public class AuditProducer implements RequestHandler<DynamodbEvent, Void> {
	private final AuditTableService auditTableService;


	public AuditProducer() {
		this.auditTableService = new AuditTableServiceImpl(
				System.getenv("region"),
				System.getenv("table")
		);
	}

	public Void handleRequest(DynamodbEvent dynamodbEvent, Context context) {
		dynamodbEvent.getRecords().forEach(dynamodbStreamRecord -> {
			switch (dynamodbStreamRecord.getEventName()) {
				case "INSERT" -> auditTableService.putInsertEvent(dynamodbStreamRecord);
				case "MODIFY" -> auditTableService.putModifyEvent(dynamodbStreamRecord);
				default -> throw new IllegalStateException("Unexpected value: " + dynamodbStreamRecord.getEventName());
			}
		});
		return null;
	}
}
