package com.task05.service;

import com.task05.model.Event;

import java.util.Map;

public interface EventTableService {
    Event putItem(int principalId, Map<String, Object> body);
}
