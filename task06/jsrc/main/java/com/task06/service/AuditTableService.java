package com.task06.service;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;

public interface AuditTableService {
    void putInsertEvent(DynamodbEvent.DynamodbStreamRecord record);
    void putModifyEvent(DynamodbEvent.DynamodbStreamRecord record);
}
