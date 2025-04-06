package com.restaurant.app.models.location;

public class ShortLocationResponse {
    private String id;
    private String address;

    public ShortLocationResponse(String id, String address) {
        this.id = id;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static ShortLocationResponse toShortLocationResponse(Location location){
        return new ShortLocationResponse(location.getId(),location.getAddress());
    }
}
