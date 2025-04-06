package com.restaurant.app.models.location;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import java.util.List;
import java.util.Map;

public class PaginatedFeedbackResponse {
    private final List<Feedback> items;
    private final Map<String, AttributeValue> lastEvaluatedKey;

    public PaginatedFeedbackResponse(List<Feedback> items, Map<String, AttributeValue> lastEvaluatedKey) {
        this.items = items;
        this.lastEvaluatedKey = lastEvaluatedKey;
    }

    public List<Feedback> getItems() {
        return items;
    }

    public Map<String, AttributeValue> getLastEvaluatedKey() {
        return lastEvaluatedKey;
    }
}
