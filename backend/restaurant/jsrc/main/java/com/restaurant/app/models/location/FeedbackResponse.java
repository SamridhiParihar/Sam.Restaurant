package com.restaurant.app.models.location;

public class FeedbackResponse {
    private String id;
    private String rate;
    private String comment;
    private String userName;
    private String userAvatarUrl;
    private String date;
    private String type;
    private String locationId;

    public FeedbackResponse(){

    }

    public FeedbackResponse(String id, String rate, String comment, String userName, String userAvatarUrl,
                            String date, String type, String locationId) {
        this.id = id;
        this.rate = rate;
        this.comment = comment;
        this.userName = userName;
        this.userAvatarUrl = userAvatarUrl;
        this.date = date;
        this.type = type;
        this.locationId = locationId;
    }

    // Getters and setters ...

    public String getId() {
        return id;
    }

    public String getRate() {
        return rate;
    }

    public String getComment() {
        return comment;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getLocationId() {
        return locationId;
    }
}
