package com.restaurant.app.models.location;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

@DynamoDbBean
public class Location {
    private String id;
    private String description;
    private List<String> specialDishes;
    private List<String> feedbacks;
    private String address;
    private int totalCapacity;
    private double averageOccupancy;
    private String imageUrl;
    private double rating;
    private List<String> tables;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public List<String> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSpecialDishes() {
        return specialDishes;
    }

    public void setSpecialDishes(List<String> specialDishes) {
        this.specialDishes = specialDishes;
    }

    public List<String> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<String> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(int totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public double getAverageOccupancy() {
        return averageOccupancy;
    }

    public void setAverageOccupancy(double averageOccupancy) {
        this.averageOccupancy = averageOccupancy;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

}
