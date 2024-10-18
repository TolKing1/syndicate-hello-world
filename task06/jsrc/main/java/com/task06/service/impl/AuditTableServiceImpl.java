package com.task06.service.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.task06.service.AuditTableService;
import com.task06.util.DateTimeUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class AuditTableServiceImpl implements AuditTableService {
    private final String table;
    private final AmazonDynamoDB dynamoDb;

    public AuditTableServiceImpl(String region, String table) {
        this.dynamoDb = AmazonDynamoDBClientBuilder.standard()
                .withRegion(region)
                .build();
        this.table = table;
    }

    private void putItem(Map<String, AttributeValue> auditEntry) {
        dynamoDb.putItem(new PutItemRequest().withTableName(table).withItem(auditEntry));
    }

    private  Map<String, AttributeValue> getCommonAttributes(DynamodbEvent.DynamodbStreamRecord record){
        Map<String, AttributeValue> auditEvent = new HashMap<>();
        String key = record.getDynamodb().getKeys().get("key").getS();
        auditEvent.put("id", new AttributeValue().withS(UUID.randomUUID().toString()));
        auditEvent.put("itemKey", new AttributeValue().withS(key));
        auditEvent.put("modificationTime", new AttributeValue().withS(DateTimeUtil.now()));

        return auditEvent;
    }

    @Override
    public void putInsertEvent(DynamodbEvent.DynamodbStreamRecord record) {
        Map<String, AttributeValue> auditEvent = getCommonAttributes(record);
        auditEvent.put("newValue", new AttributeValue().withM(getValueMap(record)));
        putItem(auditEvent);
    }

    private static Map<String, AttributeValue> getValueMap(DynamodbEvent.DynamodbStreamRecord record) {
        Map<String, AttributeValue> newValueMap = new HashMap<>();
        newValueMap.put("key", new AttributeValue().withS(record.getDynamodb().getKeys().get("key").getS()));
        newValueMap.put("value", new AttributeValue().withN(record.getDynamodb().getNewImage().get("value").getN()));
        return newValueMap;
    }

    @Override
    public void putModifyEvent(DynamodbEvent.DynamodbStreamRecord record) {
        Map<String, AttributeValue> auditEvent = getCommonAttributes(record);
        putModificationEvent(record, auditEvent);
        putItem(auditEvent);
    }

    private static void putModificationEvent(DynamodbEvent.DynamodbStreamRecord record, Map<String, AttributeValue> auditEvent) {
        auditEvent.put("oldValue", new AttributeValue().withN(record.getDynamodb().getOldImage().get("value").getN()));
        auditEvent.put("newValue", new AttributeValue().withN(record.getDynamodb().getNewImage().get("value").getN()));
        auditEvent.put("updatedValue", new AttributeValue().withS("value"));
    }
}
