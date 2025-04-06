package com.restaurant.app.models.bookings;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalTime;
import java.util.List;

@DynamoDbBean
public class Reservation {
    private String id;
    private String userId;
    private String status;
    private String locationAddress;
    private String tableId;
    private String date;
    private String timeSlot;
    private List<String> preOrder;
    private Integer noOfGuests;
    private String feedbackId;
    private LocalTime bookedAt;

    public Reservation(){

    }
    public Reservation(String id, String userId, String status, String locationAddress, String tableId, String date, String timeSlot, List<String> preOrder, Integer noOfGuests, String feedbackId, LocalTime bookedAt) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.locationAddress = locationAddress;
        this.tableId = tableId;
        this.date = date;
        this.timeSlot = timeSlot;
        this.preOrder = preOrder;
        this.noOfGuests = noOfGuests;
        this.feedbackId = feedbackId;
        this.bookedAt = bookedAt;
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public List<String> getPreOrder() {
        return preOrder;
    }

    public void setPreOrder(List<String> preOrder) {
        this.preOrder = preOrder;
    }

    public Integer getNoOfGuests() {
        return noOfGuests;
    }

    public void setNoOfGuests(Integer noOfGuests) {
        this.noOfGuests = noOfGuests;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public LocalTime getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(LocalTime bookedAt) {
        this.bookedAt = bookedAt;
    }
}
