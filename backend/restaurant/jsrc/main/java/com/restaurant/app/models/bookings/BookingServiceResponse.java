package com.restaurant.app.models.bookings;


public class BookingServiceResponse {
    private String id;
    private String status;
    private String locationAddress;
    private String date;
    private String timeSlot;
    private String preOrder;
    private String guestNumber;
    private String feedbackId;

    public BookingServiceResponse(String id, String status, String locationAddress, String date, String timeSlot, String preOrder, String guestNumber, String feedbackId) {
        this.id = id;
        this.status = status;
        this.locationAddress = locationAddress;
        this.date = date;
        this.timeSlot = timeSlot;
        this.preOrder = preOrder;
        this.guestNumber = guestNumber;
        this.feedbackId = feedbackId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPreOrder() {
        return preOrder;
    }

    public void setPreOrder(String preOrder) {
        this.preOrder = preOrder;
    }

    public String getGuestNumber() {
        return guestNumber;
    }

    public void setGuestNumber(String guestNumber) {
        this.guestNumber = guestNumber;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public static BookingServiceResponse toBookingService(Reservation reservation,String locationAddress) {
        return new BookingServiceResponse(reservation.getId(),
                reservation.getStatus(), locationAddress, reservation.getDate()
                , reservation.getTimeSlot(), reservation.getPreOrder().toString(),
                String.valueOf(reservation.getNoOfGuests()), reservation.getFeedbackId());

    }
}
