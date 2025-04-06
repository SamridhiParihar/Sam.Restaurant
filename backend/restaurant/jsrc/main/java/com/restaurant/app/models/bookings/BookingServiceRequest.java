package com.restaurant.app.models.bookings;

import org.json.JSONObject;

public class BookingServiceRequest {
    private String locationId;
    private String tableNumber ;
    private String date;
    private String guestsNumber;
    private String timeFrom ;
    private String timeTo;

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGuestsNumber() {
        return guestsNumber;
    }

    public void setGuestsNumber(String guestsNumber) {
        this.guestsNumber = guestsNumber;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public static BookingServiceRequest fromJson(String jsonString) {
        JSONObject json = new JSONObject(jsonString);

        BookingServiceRequest request = new BookingServiceRequest();
        request.setLocationId(json.optString("locationId", null));
        request.setTableNumber(json.optString("tableNumber", null));
        request.setDate(json.optString("date", null));
        request.setGuestsNumber(json.optString("guestsNumber", null));
        request.setTimeFrom(json.optString("timeFrom", null));
        request.setTimeTo(json.optString("timeTo", null));

        return request;
    }
}
