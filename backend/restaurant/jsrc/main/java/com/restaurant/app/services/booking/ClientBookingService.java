//package com.restaurant.app.services.booking;
//
//import com.restaurant.app.models.location.Location;
//import com.restaurant.app.models.auth.User;
//import com.restaurant.app.models.bookings.BookingServiceRequest;
//import com.restaurant.app.models.bookings.BookingServiceResponse;
//import com.restaurant.app.models.bookings.Reservation;
//import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
//import software.amazon.awssdk.enhanced.dynamodb.Key;
//import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
//
//import javax.inject.Inject;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//public class ClientBookingService {
//    private DynamoDbTable<Reservation> reservationsDynamoDbTable;
//    private DynamoDbTable<Location> locationDynamoDbTable;
//    private Map<String, List<String>> map = Map.of(
//            "slot1", List.of("10:30", "12:00"),
//            "slot2", List.of("12:15", "13:45"),
//            "slot3", List.of("14:00", "15:30"),
//            "slot4", List.of("15:45", "17:15"),
//            "slot5", List.of("17:30", "19:00"),
//            "slot6", List.of("19:15", "20:45"),
//            "slot7", List.of("21:00", "22:30")
//    );
//
//    public static final Set<String> TIME_POINTS = Set.of(
//            "10:30", "12:00",
//            "12:15", "13:45",
//            "14:00", "15:30",
//            "15:45", "17:15",
//            "17:30", "19:00",
//            "19:15", "20:45",
//            "21:00", "22:30"
//    );
//
//
//    @Inject
//    public ClientBookingService(DynamoDbTable<Reservation> reservationsDynamoDbTable,
//                                DynamoDbTable<Location> locationDynamoDbTable) {
//        this.reservationsDynamoDbTable = reservationsDynamoDbTable;
//        this.locationDynamoDbTable = locationDynamoDbTable;
//    }
//
//
//    public Map<String, Object> saveResponseForBooking(BookingServiceRequest request, User user) {
//        try {
//
//            String startTime = request.getTimeFrom(); //"12:15"
//            String endTime = request.getTimeTo();// "14:00"
//            String Date = request.getDate();
//            LocalDate actualDate = LocalDate.parse(Date);
//
//            System.out.println("actualDate: "+actualDate);
//            System.out.println("current date: "+LocalDate.now());
//            if(actualDate.isBefore(LocalDate.now())){
//                System.out.println("actual day is before current date ");
//                return Map.of(
//                        "statusCode",400,
//                        "message","Can't book in the past date"
//                );
//            }
//
//            if(!TIME_POINTS.contains(startTime) || !TIME_POINTS.contains(endTime)){
//                return Map.of(
//                        "statusCode",400,
//                        "message","Wrong time slot selected"
//
//                );
//            }
//
//            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
//            LocalTime startTimeDate = LocalTime.parse(startTime, timeFormatter);
//            LocalTime endTimeDate = LocalTime.parse(endTime,timeFormatter);
//            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
//            LocalTime indiaTime = now.toLocalTime();
//            if(LocalDate.now().equals(actualDate) && endTimeDate.isBefore(indiaTime)){
//                return Map.of(
//                        "statusCode",400,
//                        "message","sorry can't book the past slot "
//                );
//            }
//
//            if(startTimeDate.isAfter(endTimeDate)){
//                return Map.of(
//                        "statusCode",400,
//                        "message"," Start time of slot can't be after than End time "
//                );
//            }
//
//            String tableId = request.getTableNumber();
//            String userId = user.getEmail();
//            String locationId = request.getLocationId();
//
//            // checking for the table ...whether it exists on the given location or not
//            boolean flag = locationHasTable(locationId, tableId);
//            System.out.println("flag" + flag);
//            System.out.println("sam save response for booking");
//            if(!flag){
//                return Map.of(
//                        "statusCode", 400,
//                        "message","Table not found for location : "+locationId
//                );
//            }
//
//            String date = request.getDate();
//            String status = "Reserved";
//            String noOfGuest = request.getGuestsNumber();
//            String feedbackId = "";
//            List<String> preOrder = new ArrayList<>();
//            LocalTime bookedAt = LocalTime.now();
//
//            String timeSlot1 = null;
//            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
//                List<String> list = entry.getValue();
//                if (list.get(0).equals(startTime)) {
//                    timeSlot1 = entry.getKey();
//                    break;
//                }
//            }
//
//            String timeSlot2 = null;
//            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
//                List<String> list = entry.getValue();
//                if (list.get(1).equals(endTime)) {
//                    timeSlot2 = entry.getKey();
//                    break;
//                }
//            }
//
//            boolean isValid = isBookingTimeValid(request.getDate(), startTime, endTime);
//            if (!isValid) {
//                return Map.of(
//                        "statusCode",400,
//                        "message", "Booking date and time is not valid "
//                );
//            }
//
//            int s = Integer.parseInt(String.valueOf(timeSlot1.charAt(timeSlot1.length() - 1)));
//            int e = Integer.parseInt(String.valueOf(timeSlot2.charAt(timeSlot2.length() - 1)));
//
//            List<BookingServiceResponse> bookingServiceResponseList = new ArrayList<>();
//            for (int i = s; i <= e; i++) {
//                String slot = "slot" + i;
//                String id = date + locationId + tableId + slot;
//
//                Key key1 = Key.builder().partitionValue(id).build();
//                Reservation already_reserved_or_not = reservationsDynamoDbTable.getItem(key1);
//                if(already_reserved_or_not!=null && already_reserved_or_not.getStatus().equalsIgnoreCase("Reserved")){
//                    return Map.of(
//                            "statusCode",400,
//                            "message","table already Reserved!"
//
//                    );
//                }
//
//                Reservation reservation = new Reservation(id, userId, status, locationId, tableId, date, slot, preOrder, Integer.parseInt(noOfGuest), feedbackId, bookedAt);
//
//                Key key = Key.builder().partitionValue(reservation.getLocationAddress()).build();
//                Location location = locationDynamoDbTable.getItem(key);
//
//                if (location == null) {
//                    return Map.of(
//                            "statusCode", 400,
//                            "message", "locationId doesn't exists!"
//                    );
//                }
//                String locationAddress = location.getAddress();
//
//                // userTable.putItem(PutItemEnhancedRequest.builder(User.class).item(user).build());
//                reservationsDynamoDbTable.putItem(PutItemEnhancedRequest.builder(Reservation.class).item(reservation).build());
//
//                bookingServiceResponseList.add((BookingServiceResponse.toBookingService(reservation, locationAddress)));
//            }
//            return Map.of(
//                    "statusCode", 200,
//                    "body", bookingServiceResponseList
//            );
//        } catch (Exception e) {
//            return Map.of(
//                    "statusCode", 500,
//                    "message", "Error reserving the table "
//            );
//        }
//
//    }
//
//    public boolean locationHasTable(String locationId, String tableId){
//
//        Key key = Key.builder().partitionValue(locationId).build();
//        List<String> tableIds = locationDynamoDbTable.getItem(key).getTables();
//        for(String str : tableIds){
//            if(str.equals(tableId)){
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    public boolean isBookingTimeValid(String requestDateStr, String startTimeStr, String endTimeStr) {
//        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
//
//        LocalDate today = LocalDate.now();
//        LocalDate requestDate = LocalDate.parse(requestDateStr);
//
//        LocalTime currentTime = LocalTime.now();
//        LocalTime slotEnd = LocalTime.parse(endTimeStr, timeFormatter);
//        LocalTime slotStart = LocalTime.parse(startTimeStr,timeFormatter);
//        System.out.println();
//        if (requestDate.isAfter(today)) {
//            return true;
//        }
//
//        if (requestDate.equals(today)) {
//            return currentTime.isBefore(slotStart);
//        }
//
//        // Otherwise, booking is not allowed
//        return false;
//    }
//}


package com.restaurant.app.services.booking;

import com.restaurant.app.models.location.Location;
import com.restaurant.app.models.auth.User;
import com.restaurant.app.models.bookings.BookingServiceRequest;
import com.restaurant.app.models.bookings.BookingServiceResponse;
import com.restaurant.app.models.bookings.Reservation;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Service class for handling client booking operations. It provides functionalities
 * to validate user booking requests, check slot availability, ensure location and table
 * validity, and persist reservations into DynamoDB.
 */
public class ClientBookingService {

    private final DynamoDbTable<Reservation> reservationsDynamoDbTable;
    private final DynamoDbTable<Location> locationDynamoDbTable;

    // Predefined time slots mapping
    private static final Map<String, List<String>> TIME_SLOT_MAP = Map.of(
            "slot1", List.of("10:30", "12:00"),
            "slot2", List.of("12:15", "13:45"),
            "slot3", List.of("14:00", "15:30"),
            "slot4", List.of("15:45", "17:15"),
            "slot5", List.of("17:30", "19:00"),
            "slot6", List.of("19:15", "20:45"),
            "slot7", List.of("21:00", "22:30")
    );

    // All unique time points from slots (start and end)
    private static final Set<String> TIME_POINTS = new HashSet<>();

    static {
        TIME_SLOT_MAP.values().forEach(times -> TIME_POINTS.addAll(times));
    }

    /**
     * Constructs a new instance with injected DynamoDB tables.
     *
     * @param reservationsDynamoDbTable Table used to store reservations.
     * @param locationDynamoDbTable     Table used to fetch location/table details.
     */
    @Inject
    public ClientBookingService(DynamoDbTable<Reservation> reservationsDynamoDbTable,
                                DynamoDbTable<Location> locationDynamoDbTable) {
        this.reservationsDynamoDbTable = reservationsDynamoDbTable;
        this.locationDynamoDbTable = locationDynamoDbTable;
    }

    /**
     * Validates and processes a booking request for a given user.
     *
     * @param request Booking request details.
     * @param user    The authenticated user making the reservation.
     * @return A response map with either success details or an error message.
     */
    public Map<String, Object> saveResponseForBooking(BookingServiceRequest request, User user) {
        try {
            String startTime = request.getTimeFrom();
            String endTime = request.getTimeTo();
            String date = request.getDate();
            LocalDate bookingDate = LocalDate.parse(date);

            if(Integer.parseInt(request.getGuestsNumber())<=0){
                return error(400,"Guest numbers must be greater than 0");
            }
            if (!isDateValid(bookingDate)) {
                return error(400, "Can't book in the past date");
            }

            if (!isTimePointValid(startTime, endTime)) {
                return error(400, "Wrong time slot selected");
            }

            if (!isTimeRangeValid(bookingDate, startTime, endTime)) {
                return error(400, "Sorry, can't book the past slot");
            }

            if (isStartTimeAfterEndTime(startTime, endTime)) {
                return error(400, "Start time of slot can't be after End time");
            }

            if (!locationHasTable(request.getLocationId(), request.getTableNumber())) {
                return error(400, "Table not found for location: " + request.getLocationId());
            }

            return reserveTable(request, user);
        } catch (Exception e) {
            return error(500, "Error reserving the table");
        }
    }

    /**
     * Checks if the provided date is today or in the future.
     */
    private boolean isDateValid(LocalDate date) {
        return !date.isBefore(LocalDate.now());
    }

    /**
     * Verifies that both start and end times are valid predefined slot times.
     */
    private boolean isTimePointValid(String start, String end) {
        return TIME_POINTS.contains(start) && TIME_POINTS.contains(end);
    }

    /**
     * Ensures that a time slot on the current day isn't already past.
     */
    private boolean isTimeRangeValid(LocalDate bookingDate, String start, String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime endTime = LocalTime.parse(end, formatter);
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        return !LocalDate.now().equals(bookingDate) || !endTime.isBefore(now.toLocalTime());
    }

    /**
     * Returns true if start time is after end time.
     */
    private boolean isStartTimeAfterEndTime(String start, String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(start, formatter).isAfter(LocalTime.parse(end, formatter));
    }

    /**
     * Attempts to reserve the table across all slots between the start and end time.
     *
     * @param request Booking request details.
     * @param user    User performing the booking.
     * @return Success response or error if a slot is already booked.
     */
    private Map<String, Object> reserveTable(BookingServiceRequest request, User user) {
        List<BookingServiceResponse> responseList = new ArrayList<>();
        String date = request.getDate();
        String tableId = request.getTableNumber();
        String userId = user.getEmail();
        String locationId = request.getLocationId();
        String noOfGuests = request.getGuestsNumber();
        String feedbackId = "";
        List<String> preOrder = new ArrayList<>();
        LocalTime bookedAt = LocalTime.now();
        String startTime = request.getTimeFrom();
        String endTime = request.getTimeTo();

        String slotFrom = getSlotByStartTime(startTime);
        String slotTo = getSlotByEndTime(endTime);

        if (!isBookingTimeValid(date, startTime, endTime)) {
            return error(400, "Booking date and time is not valid");
        }

        int s = extractSlotIndex(slotFrom);
        int e = extractSlotIndex(slotTo);

        for (int i = s; i <= e; i++) {
            String slot = "slot" + i;
            String id = date + locationId + tableId + slot;

            if (isSlotReserved(id)) {
                return error(409, "Table already Reserved!");
            }

            Reservation reservation = new Reservation(id, userId, "Reserved", locationId,
                    tableId, date, slot, preOrder, Integer.parseInt(noOfGuests), feedbackId, bookedAt);

            Location location = getLocation(locationId);
            if (location == null) {
                return error(400, "locationId doesn't exist!");
            }

            reservationsDynamoDbTable.putItem(PutItemEnhancedRequest.builder(Reservation.class).item(reservation).build());
            responseList.add(BookingServiceResponse.toBookingService(reservation, location.getAddress()));
        }

        return Map.of(
                "statusCode", 200,
                "body", responseList
        );
    }

    /**
     * Checks if a slot reservation already exists and is marked as reserved.
     */
    private boolean isSlotReserved(String reservationId) {
        Reservation existing = reservationsDynamoDbTable.getItem(Key.builder().partitionValue(reservationId).build());
        return existing != null && "Reserved".equalsIgnoreCase(existing.getStatus());
    }

    /**
     * Retrieves the slot key (e.g., "slot1") by matching the given start time.
     */
    private String getSlotByStartTime(String start) {
        return TIME_SLOT_MAP.entrySet().stream()
                .filter(e -> e.getValue().get(0).equals(start))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the slot key (e.g., "slot2") by matching the given end time.
     */
    private String getSlotByEndTime(String end) {
        return TIME_SLOT_MAP.entrySet().stream()
                .filter(e -> e.getValue().get(1).equals(end))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    /**
     * Parses the numeric slot index from a slot key.
     */
    private int extractSlotIndex(String slot) {
        return Integer.parseInt(slot.replace("slot", ""));
    }

    /**
     * Checks if the specified table exists at the given location.
     *
     * @param locationId The location ID.
     * @param tableId    The table number.
     * @return True if the location has the table; false otherwise.
     */
    public boolean locationHasTable(String locationId, String tableId) {
        Location location = locationDynamoDbTable.getItem(Key.builder().partitionValue(locationId).build());
        return location != null && location.getTables().contains(tableId);
    }

    /**
     * Validates that booking time is not in the past for the same day.
     *
     * @param requestDateStr Requested date in yyyy-MM-dd format.
     * @param startTimeStr   Requested start time (HH:mm).
     * @param endTimeStr     Requested end time (HH:mm).
     * @return True if booking time is valid.
     */
    public boolean isBookingTimeValid(String requestDateStr, String startTimeStr, String endTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalDate requestDate = LocalDate.parse(requestDateStr);
        LocalDate today = LocalDate.now();

        LocalTime slotStart = LocalTime.parse(startTimeStr, formatter);
        return requestDate.isAfter(today) || (requestDate.equals(today) && LocalTime.now().isBefore(slotStart));
    }

    /**
     * Utility method to create a standardized error response map.
     *
     * @param code    HTTP-like status code.
     * @param message Error message.
     * @return A map representing the error.
     */
    private Map<String, Object> error(int code, String message) {
        return Map.of("statusCode", code, "message", message);
    }

    /**
     * Retrieves location data by locationId from the DynamoDB table.
     *
     * @param locationId Location identifier.
     * @return Location object or null if not found.
     */
    private Location getLocation(String locationId) {
        return locationDynamoDbTable.getItem(Key.builder().partitionValue(locationId).build());
    }
}
