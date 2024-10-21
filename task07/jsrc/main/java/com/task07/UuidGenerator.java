package com.task07;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syndicate.deployment.annotations.environment.EnvironmentVariable;
import com.syndicate.deployment.annotations.environment.EnvironmentVariables;
import com.syndicate.deployment.annotations.events.EventBridgeRuleSource;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.RetentionSetting;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@LambdaHandler(
    lambdaName = "uuid_generator",
	roleName = "uuid_generator-role",
	aliasName = "${lambdas_alias_name}",
	logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@EventBridgeRuleSource(targetRule = "uuid_trigger")
@EnvironmentVariables(value = {
		@EnvironmentVariable(key = "region", value = "${region}")
})
public class UuidGenerator implements RequestHandler<ScheduledEvent, String> {
	private static final AmazonS3 s3client= AmazonS3ClientBuilder.standard()
			.withRegion(System.getenv("region")).build();
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static final String BUCKET_NAME = "cmtr-2cd95cf2-uuid-storage-test";

	public String handleRequest(ScheduledEvent event, Context context) {
		LambdaLogger logger = context.getLogger();
		s3client.createBucket(BUCKET_NAME);
		logger.log( " Bucket created " );
		try
		{
			String fileName = getNow();
			String fileData = objectMapper.writeValueAsString(generateFileData());

			putObject(fileData, fileName);
			logger.log( "File written and uploaded to S3" );
		}
		catch(Exception ex)
		{
			logger.log("Error : "+ ex.getMessage());
			throw new RuntimeException(ex);
		}
		return "success";
	}

	private static void putObject(String fileData, String fileName) {
		byte[]  contentAsBytes = fileData.getBytes(StandardCharsets.UTF_8) ;
		ByteArrayInputStream contentsAsStream      = new ByteArrayInputStream(contentAsBytes);
		ObjectMetadata md = new ObjectMetadata();
		md.setContentLength(contentAsBytes.length);
		md.setContentType("application/json");
		s3client.putObject(new PutObjectRequest(BUCKET_NAME, fileName, contentsAsStream, md));
	}

	private String getNow(){
		Instant now = Instant.now();
		ZonedDateTime utcDateTime = now.atZone(ZoneOffset.UTC);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

		return utcDateTime.format(formatter);
	}

	private Map<String, List<String>> generateFileData(){
		Map<String, List<String>> fileData = new HashMap<>();
		fileData.put("ids", generateUuids(10));

		return fileData;
	}
	private List<String> generateUuids(int count){
		List<String> uuids = new ArrayList<>();

		for (int i = 0; i < count; i++) {
			uuids.add(UUID.randomUUID().toString());
		}

		return uuids;
	}
}
