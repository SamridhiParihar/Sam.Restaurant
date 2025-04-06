package com.restaurant.app.models.dish;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean

public class Dish {

    private String calories;

    private String carbohydrates;

    private String description;

    private String dishType;

    private String fats;

    private String id;

    private String imageUrl;

    private String name;

    private String price;

    private String proteins;

    private String state;

    private String vitamins;

    private String weight;

    private int order;

    @DynamoDbPartitionKey
    public String getId() {

        return id;

    }

    public void setId(String id) {

        this.id = id;

    }

    public String getFats() {

        return fats;

    }

    public void setFats(String fats) {

        this.fats = fats;

    }

    public String getCalories() {

        return calories;

    }

    public void setCalories(String calories) {

        this.calories = calories;

    }

    public String getCarbohydrates() {

        return carbohydrates;

    }

    public void setCarbohydrates(String carbohydrates) {

        this.carbohydrates = carbohydrates;

    }

    public String getDescription() {

        return description;

    }

    public void setDescription(String description) {

        this.description = description;

    }

    public String getDishType() {

        return dishType;

    }

    public void setDishType(String dishType) {

        this.dishType = dishType;

    }


    public String getImageUrl() {

        return imageUrl;

    }

    public void setImageUrl(String imageUrl) {

        this.imageUrl = imageUrl;

    }

    public String getName() {

        return name;

    }

    public void setName(String name) {

        this.name = name;

    }

    public int getOrder() {

        return order;

    }

    public void setOrder(int order) {

        this.order = order;

    }

    public String getPrice() {

        return price;

    }

    public void setPrice(String price) {

        this.price = price;

    }

    public String getProteins() {

        return proteins;

    }

    public void setProteins(String proteins) {

        this.proteins = proteins;

    }

    public String getState() {

        return state;

    }

    public void setState(String state) {

        this.state = state;

    }

    public String getVitamins() {

        return vitamins;

    }

    public void setVitamins(String vitamins) {

        this.vitamins = vitamins;

    }

    public String getWeight() {

        return weight;

    }

    public void setWeight(String weight) {

        this.weight = weight;

    }

}

