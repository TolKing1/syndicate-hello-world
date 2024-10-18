package com.task05.service.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.task05.model.Event;
import com.task05.util.DateTimeUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class EventTableServiceImpl implements com.task05.service.EventTableService {
    private final String table;
    private final AmazonDynamoDB dynamoDb;

    public EventTableServiceImpl(String region, String table) {
        this.dynamoDb = AmazonDynamoDBClientBuilder.standard()
                .withRegion(region)
                .build();
        this.table = table;
    }

    @Override
    public Event putItem(int principalId, Map<String, Object> body) {
        Event event = new Event.Builder()
                .withId(UUID.randomUUID().toString())
                .withPrincipalId(principalId)
                .withCreatedAt(DateTimeUtil.now())
                .withBody(body)
                .build();

        Map<String, AttributeValue> bodyMap = new HashMap<>();
        event.getBody().forEach((key, value) ->
                bodyMap.put(key, new AttributeValue().withS(value.toString())))
        ;

        dynamoDb.putItem(
                new PutItemRequest()
                        .withTableName(table)
                        .withItem(Map.of(
                                        "id", new AttributeValue().withS(event.getId()),
                                        "principalId", new AttributeValue().withN(Integer.toString(event.getPrincipalId())),
                                        "createdAt", new AttributeValue().withS(event.getCreatedAt()),
                                        "body", new AttributeValue().withM(bodyMap)
                                )
                        )
        );

        return event;
    }

}
