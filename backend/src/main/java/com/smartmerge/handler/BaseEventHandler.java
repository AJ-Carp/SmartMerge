package com.smartmerge.handler;

import java.util.Map;

public interface BaseEventHandler {
    public void triggerEvent(Map<String, Object> webhookPayload, String action);
}