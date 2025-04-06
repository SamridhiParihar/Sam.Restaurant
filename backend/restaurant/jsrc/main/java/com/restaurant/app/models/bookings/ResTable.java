package com.restaurant.app.models.bookings;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.Map;

@DynamoDbBean
public class ResTable {
    private String id;
    private Integer capacity;
    private String date;
    private Map<String,Boolean> slots =
            Map.of(
                    "slot1",true,
                    "slot2",true,
                    "slot3",true,
                    "slot4",true,
                    "slot5",true,
                    "slot6",true,
                    "slot7",true
            );

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, Boolean> getSlots() {
        return slots;
    }

    public void setSlots(Map<String, Boolean> slots) {
        this.slots = slots;
    }
}
