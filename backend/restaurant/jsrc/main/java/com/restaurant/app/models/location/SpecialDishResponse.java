package com.restaurant.app.models.location;

import com.restaurant.app.models.dish.Dish;

public class SpecialDishResponse {
    private String name;
    private String price;
    private String weight;
    private String imageUrl;

    public SpecialDishResponse(String name, String price, String weight, String imageUrl) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static SpecialDishResponse toSpecialDishResponse(Dish dish){
        return new SpecialDishResponse(dish.getName(),dish.getPrice(),dish.getWeight(),dish.getImageUrl());
    }
}
