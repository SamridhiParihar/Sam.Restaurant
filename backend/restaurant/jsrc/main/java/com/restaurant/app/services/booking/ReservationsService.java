//package com.restaurant.app.services.booking;
//
//import com.restaurant.app.Utils.TimeSlotUtil;
//import com.restaurant.app.models.location.Location;
//import com.restaurant.app.models.bookings.AvailableTableResponse;
//import com.restaurant.app.models.bookings.ResTable;
//import com.restaurant.app.models.bookings.Reservation;
//import com.restaurant.app.models.reservation.ReservationHistoryResponse;
//import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
//import software.amazon.awssdk.enhanced.dynamodb.Key;
//import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
//
//import javax.inject.Inject;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//public class ReservationsService {
//    private DynamoDbTable<Reservation> reservationsDynamoDbTable;
//    private DynamoDbTable<ResTable> resTableDynamoDbTable;
//    private DynamoDbTable<Location> locationDynamoDbTable;
//    Map<String, List<String>> timeMap = Map.of(
//            "slot1", List.of("10:30", "12:00"),
//            "slot2", List.of("12:15", "13:45"),
//            "slot3", List.of("14:00", "15:30"),
//            "slot4", List.of("15:45", "17:15"),
//            "slot5", List.of("17:30", "19:00"),
//            "slot6", List.of("19:15", "20:45"),
//            "slot7", List.of("21:00", "22:30")
//    );
//
//    private static Map<String , Boolean> map =
//            Map.of(
//                    "slot1",true,
//                    "slot2",true,
//                    "slot3",true,
//                    "slot4",true,
//                    "slot5",true,
//                    "slot6",true,
//                    "slot7",true
//            );
//
//    @Inject
//    public ReservationsService(DynamoDbTable<Reservation> reservationsDynamoDbTable, DynamoDbTable<ResTable> resTableDynamoDbTable, DynamoDbTable<Location> locationDynamoDbTable) {
//        this.reservationsDynamoDbTable = reservationsDynamoDbTable;
//        this.resTableDynamoDbTable = resTableDynamoDbTable;
//        this.locationDynamoDbTable = locationDynamoDbTable;
//    }
//
//    public Map<String, Object> fun(String locationId, String date, Integer capacity) {
//        try {
//
//            if (locationId.isEmpty() || locationId.isBlank() || date.isEmpty() || date.isBlank()) {
//                return Map.of(
//                        "statusCode", 400,
//                        "message", "Data required"
//                );
//            }
//
//            if (capacity == null || capacity <= 0) {
//                return Map.of(
//                        "statusCode", 400,
//                        "message", "Invalid no of guest"
//                );
//            }
//
//            if (!TimeSlotUtil.validateDateFormat(date)) {
//                return Map.of(
//                        "statusCode", 400,
//                        "message", "Invalid date format"
//                );
//            }
//
//            LocalDate requestedDate = LocalDate.parse(date);
//            if(LocalDate.now().isAfter(requestedDate)){
//                System.out.println("enters the past date ");
//                return Map.of(
//                        "statusCode",400,
//                        "message","Sorry can't travel in the past !"
//                );
//            }
//
//            Map<String, Map<String, Boolean>> result = getAvailableTableWithSlots(date,locationId,capacity);
//            if(result==null || result.isEmpty()){
//                return Map.of(
//                        "statusCode", 404,
//                        "message", "sorry slots not available "
//                );
//            }
//
//            Key key_ = Key.builder().partitionValue(locationId).build();
//            Location location = locationDynamoDbTable.getItem(key_);
//
//            List<AvailableTableResponse> availableTableResponses =
//                    result.entrySet()
//                            .stream()
//                            .map(e->{
//                                        String t_id = e.getKey();
//                                        Key ke = Key.builder().partitionValue(t_id).build();
//                                        ResTable table = resTableDynamoDbTable.getItem(ke);
//                                        return AvailableTableResponse.toAvailableTableResponse(e,locationId,location.getAddress(), table.getCapacity());
//                                    }
//                            )
//                            .toList();
//
//            return Map.of(
//                    "statusCode", 200,
//                        "body", availableTableResponses
//                    );
//
//
//        } catch (Exception e) {
//            return Map.of(
//                    "statusCode", 500,
//                    "message", "Error retrieving available table slots: " + e.getMessage()
//            );
//        }
//    }
//
//    public Map<String, Map<String, Boolean>> getAvailableTableWithSlots(String date, String locationId, Integer capacity) {
//
//        Map<String, Map<String, Boolean>> result = new HashMap<>();
//
//        Key key = Key.builder().partitionValue(locationId).build();
//
//        Location location = locationDynamoDbTable.getItem(key);
//
//        List<String> tableIds = location.getTables();
//
//        List<String> ids = new ArrayList<>();
//
//        for (String tableId : tableIds) {
//
//            result.put(tableId, new HashMap<>(map));
//
//            for (int i = 1; i <=7; i++) {
//
//                String id = date + locationId + tableId + "slot" + i;
//
//                ids.add(id);
//
//            }
//
//        }
//
//        Map<String, List<String>> filledMap = new HashMap<>();
//
//        for (String id : ids) {
//
//            Key key1 = Key.builder().partitionValue(id).build();
//
//            Reservation reservation = reservationsDynamoDbTable.getItem(key1);
//
//            if (reservation != null && reservation.getStatus().equalsIgnoreCase("Reserved") ) {
//                String kii = reservation.getTableId();
//                String timeSlot = reservation.getTimeSlot();
//                filledMap.putIfAbsent(kii,new ArrayList<>());
//                filledMap.get(kii).add(timeSlot);
//            }
//
//        }
//
//
//
//        for(Map.Entry<String,List<String>> entry: filledMap.entrySet()){
//
//            String table_id = entry.getKey();
//
//            Key kyun = Key.builder().partitionValue(table_id).build();
//
//            ResTable resTable = resTableDynamoDbTable.getItem(kyun);
//
//            for (String slot : entry.getValue()) {
//
//                result.get(table_id).remove(slot);
//            }
//
//        }
//
//        Map<String,Map<String,Boolean>> finalResult = new HashMap<>();
//
//        //...
//
//        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
//        LocalDate today = LocalDate.now();
////        LocalTime currentTime = LocalTime.now(); this would work for paris region or the
//        // region it is being hosted on aws but for india
//        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
//        LocalTime indiaTime = now.toLocalTime();
//        LocalDate requestDate = LocalDate.parse(date);
//
//        Map<String , Map<String,Boolean>> copyResult = new HashMap<>(result);
//
//        if(today.equals(requestDate)) {
//            System.out.println("Enters the current date block");
//            for (Map.Entry<String, Map<String, Boolean>> entry : copyResult.entrySet()) {
//                String table_id = entry.getKey();
//
//                Key key1 = Key.builder().partitionValue(table_id).build();
//
//                ResTable resTable = resTableDynamoDbTable.getItem(key1);
//
//                Map<String, Boolean> slots = resTable.getSlots();
//
//                int i=0;
//                for (String slot : slots.keySet()) {
//                    String currentStartTimeOfSlot = timeMap.get(slot).get(0);
//                    String currentEndTimeOfSlot = timeMap.get(slot).get(1);
//
//                    LocalTime slotStartTime = LocalTime.parse(currentStartTimeOfSlot, timeFormatter);
//                    LocalTime slotEndTime = LocalTime.parse(currentEndTimeOfSlot, timeFormatter);
//
//                    System.out.println("Current slot end time : "+ slotEndTime);
//                    System.out.println("Current time : "+ LocalTime.now());
//                    if (slotEndTime.isBefore(indiaTime)) {
//                        System.out.println("slot end time is before current time "+i);
//                        result.get(table_id).remove(slot);
//                        System.out.println("this slot is being deleted "+i++);
//                    }
//
//
//                }
//            }
//        }
//
//        //..
//
//
//        for(Map.Entry<String,Map<String,Boolean>> entry: result.entrySet()){
//            String table_id = entry.getKey();
//            Key key1 = Key.builder().partitionValue(table_id).build();
//            ResTable resTable = resTableDynamoDbTable.getItem(key1);
//            if(resTable.getCapacity()>=capacity){
//                finalResult.put(table_id,result.get(table_id));
//            }
//        }
//        return finalResult;
//    }
//
//    public Map<String,Object> getReservationHistory(String email){
//        try{
//            List<ReservationHistoryResponse> reservationHistoryResponses =
//                    reservationsDynamoDbTable.scan(
//                            ScanEnhancedRequest.builder().build()
//                    ).items()
//                            .stream()
//                            .filter(reservation -> reservation.getUserId().equalsIgnoreCase(email))
//                            .map(ReservationHistoryResponse::toReservationHistory)
//                            .toList();
//            if(reservationHistoryResponses==null || reservationHistoryResponses.isEmpty()){
//                return Map.of(
//                        "statusCode",204,
//                        "message" , "no reservation history found"
//                );
//            }
//
//            for(ReservationHistoryResponse reservationHistoryResponse : reservationHistoryResponses){
//                Key keys = Key.builder().partitionValue(reservationHistoryResponse.getLocationAddress()).build();
//                Location location = locationDynamoDbTable.getItem(keys);
//                reservationHistoryResponse.setLocationAddress(location.getAddress());
//            }
//
//            return Map.of(
//                    "statusCode", 200,
//                    "body",reservationHistoryResponses
//                    );
//        }
//        catch (Exception e){
//            return Map.of(
//                    "statusCode",500,
//                    "message","Error retrieving reservation history"
//            );
//        }
//    }
//
//    public Map<String,Object> deleteReservationById(String id, String email) {
//
//        Key key = Key.builder().partitionValue(id).build();
//
//        Reservation reservation = reservationsDynamoDbTable.getItem(key);
//
//        if (reservation == null) {
//            System.out.println("Reservation not found for id: " + id);
//            return Map.of(
//                    "statusCode",404,
//                    "message" , "Reservation not found for locationId: " + id
//            );
//        }
//        if(!reservation.getUserId().equals(email)){
//            return Map.of(
//                    "statusCode", 403,
//                    "message", "You are not authorized to delete this reservation."
//            );
//        }
//        if(reservation.getStatus().equalsIgnoreCase("cancelled"))
//            return Map.of(
//                    "statusCode", 408,
//                    "message","Reservations already being cancelled"
//            );
//
//        String bookingDateStr=reservation.getDate();
//        LocalDate bookingDate = LocalDate.parse(bookingDateStr, DateTimeFormatter.ISO_DATE);
//        LocalDate currentDate = LocalDate.now();
//        if (currentDate.isBefore(bookingDate)) {
//            reservation.setStatus("Cancelled");
//            reservationsDynamoDbTable.updateItem(reservation);
//            return Map.of(
//                    "statusCode" ,200,
//                    "message" , "Reservation Cancelled"
//            );
//
//        }
//        else if(currentDate.isAfter(bookingDate))
//            return Map.of(
//                    "statusCode",400,
//                    "message", "Bad Request!"
//            );
//        else
//        {
//            String slot = reservation.getTimeSlot();
//
//            String time = timeMap.get(slot).get(0);
//            String s[]=time.split(":");
//            LocalTime slotTime = LocalTime.of(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
//            LocalTime currentTime = LocalTime.now();
//            if (currentTime.isBefore(slotTime.minusMinutes(30)) ) {
//                reservation.setStatus("Cancelled");
//                reservationsDynamoDbTable.updateItem(reservation);
//                return Map.of(
//                        "statusCode" ,200,
//                        "message" , "Reservation Cancelled"
//                );
//            } else {
//                return Map.of(
//                        "statusCode",400,
//                        "message", "Bad Request!"
//                );
//            }
//        }
//    }
//}



package com.restaurant.app.services.booking;

import com.restaurant.app.Utils.TimeSlotUtil;
import com.restaurant.app.models.location.Location;
import com.restaurant.app.models.bookings.AvailableTableResponse;
import com.restaurant.app.models.bookings.ResTable;
import com.restaurant.app.models.bookings.Reservation;
import com.restaurant.app.models.reservation.ReservationHistoryResponse;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

import javax.inject.Inject;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class responsible for managing restaurant table reservations.
 * It provides functionality for checking availability, retrieving
 * reservation history, and deleting reservations.
 */
public class ReservationsService {

    private final DynamoDbTable<Reservation> reservationsDynamoDbTable;
    private final DynamoDbTable<ResTable> resTableDynamoDbTable;
    private final DynamoDbTable<Location> locationDynamoDbTable;

    // Predefined time slots for reservations
    private static final Map<String, List<String>> timeMap = Map.of(
            "slot1", List.of("10:30", "12:00"),
            "slot2", List.of("12:15", "13:45"),
            "slot3", List.of("14:00", "15:30"),
            "slot4", List.of("15:45", "17:15"),
            "slot5", List.of("17:30", "19:00"),
            "slot6", List.of("19:15", "20:45"),
            "slot7", List.of("21:00", "22:30")
    );

    // Default availability for all time slots
    private static final Map<String, Boolean> defaultSlotAvailability = Map.of(
            "slot1", true, "slot2", true, "slot3", true, "slot4", true,
            "slot5", true, "slot6", true, "slot7", true
    );

    @Inject
    public ReservationsService(DynamoDbTable<Reservation> reservationsDynamoDbTable,
                               DynamoDbTable<ResTable> resTableDynamoDbTable,
                               DynamoDbTable<Location> locationDynamoDbTable) {
        this.reservationsDynamoDbTable = reservationsDynamoDbTable;
        this.resTableDynamoDbTable = resTableDynamoDbTable;
        this.locationDynamoDbTable = locationDynamoDbTable;
    }

    /**
     * Retrieves available tables at a specific location on a given date
     * that match or exceed the required capacity.
     */
    public Map<String, Object> getAvailableTables(String locationId, String date, Integer capacity) {
        // Input validation
        if (locationId == null || locationId.isBlank() || date == null || date.isBlank()) {
            return Map.of("statusCode", 400, "message", "Data required");
        }
        if (capacity == null || capacity <= 0) {
            return Map.of("statusCode", 400, "message", "Invalid number of guests");
        }
        if (!TimeSlotUtil.validateDateFormat(date)) {
            return Map.of("statusCode", 400, "message", "Invalid date format");
        }

        LocalDate requestedDate = LocalDate.parse(date);
        if (LocalDate.now().isAfter(requestedDate)) {
            return Map.of("statusCode", 400, "message", "Sorry, can't travel to the past!");
        }


        Location location__ = getLocation(locationId);
        if(location__==null){
            return Map.of(
                    "statusCode",404,
                    "message","sorry no location with this id available"
            );
        }

        Map<String, Map<String, Boolean>> availableSlots = getAvailableTableWithSlots(date, locationId, capacity);
        if (availableSlots == null || availableSlots.isEmpty()) {
            return Map.of("statusCode", 404, "message", "Sorry, no slots available");
        }

        Location location = locationDynamoDbTable.getItem(Key.builder().partitionValue(locationId).build());

        List<AvailableTableResponse> responses = availableSlots.entrySet().stream().map(entry -> {
            ResTable table = resTableDynamoDbTable.getItem(Key.builder().partitionValue(entry.getKey()).build());
            return AvailableTableResponse.toAvailableTableResponse(entry, locationId, location.getAddress(), table.getCapacity());
        }).collect(Collectors.toList());

        return Map.of("statusCode", 200, "body", responses);
    }

    /**
     * Determines available slots for all tables in a location on a specific date,
     * filtered by table capacity and previously booked slots.
     */
    public Map<String, Map<String, Boolean>> getAvailableTableWithSlots(String date, String locationId, Integer capacity) {
        Map<String, Map<String, Boolean>> availabilityMap = new HashMap<>();
        Location location = locationDynamoDbTable.getItem(Key.builder().partitionValue(locationId).build());
        List<String> tableIds = location.getTables();
        List<String> reservationIds = new ArrayList<>();

        // Initialize slot availability and build reservation IDs
        for (String tableId : tableIds) {
            availabilityMap.put(tableId, new HashMap<>(defaultSlotAvailability));
            for (int i = 1; i <= 7; i++) {
                reservationIds.add(date + locationId + tableId + "slot" + i);
            }
        }

        // Find already reserved slots
        Map<String, List<String>> filledSlots = new HashMap<>();
        for (String reservationId : reservationIds) {
            Reservation reservation = reservationsDynamoDbTable.getItem(Key.builder().partitionValue(reservationId).build());
            if (reservation != null && reservation.getStatus().equalsIgnoreCase("Reserved")) {
                filledSlots.computeIfAbsent(reservation.getTableId(), k -> new ArrayList<>()).add(reservation.getTimeSlot());
            }
        }

        // Remove reserved slots from availability map
        filledSlots.forEach((tableId, slots) -> slots.forEach(slot -> availabilityMap.get(tableId).remove(slot)));

        // Remove past time slots if the reservation is for today
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        LocalTime currentTime = now.toLocalTime();
        LocalDate today = LocalDate.now();
        LocalDate requestDate = LocalDate.parse(date);

        if (today.equals(requestDate)) {
            for (Map.Entry<String, Map<String, Boolean>> entry : new HashMap<>(availabilityMap).entrySet()) {
                for (String slot : timeMap.keySet()) {
                    LocalTime slotEndTime = LocalTime.parse(timeMap.get(slot).get(1), DateTimeFormatter.ofPattern("HH:mm"));
                    if (slotEndTime.isBefore(currentTime)) {
                        availabilityMap.get(entry.getKey()).remove(slot);
                    }
                }
            }
        }

        // Filter tables by capacity
        Map<String, Map<String, Boolean>> filteredMap = new HashMap<>();
        for (Map.Entry<String, Map<String, Boolean>> entry : availabilityMap.entrySet()) {
            ResTable table = resTableDynamoDbTable.getItem(Key.builder().partitionValue(entry.getKey()).build());
            if (table.getCapacity() >= capacity) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }

        return filteredMap;
    }

    /**
     * Retrieves reservation history for a given user email.
     */
    public Map<String, Object> getReservationHistory(String email) {
        try {
            List<ReservationHistoryResponse> history = reservationsDynamoDbTable.scan(ScanEnhancedRequest.builder().build())
                    .items()
                    .stream()
                    .filter(res -> res.getUserId().equalsIgnoreCase(email))
                    .map(ReservationHistoryResponse::toReservationHistory)
                    .collect(Collectors.toList());

            if (history.isEmpty()) {
                return Map.of("statusCode", 204, "message", "No reservation history found");
            }

            for (ReservationHistoryResponse r : history) {
                Location loc = locationDynamoDbTable.getItem(Key.builder().partitionValue(r.getLocationAddress()).build());
                r.setLocationAddress(loc.getAddress());
            }

            return Map.of("statusCode", 200, "body", history);

        } catch (Exception e) {
            return Map.of("statusCode", 500, "message", "Error retrieving reservation history");
        }
    }

    /**
     * Deletes a reservation by ID after validating the user and reservation timing.
     */
    public Map<String, Object> deleteReservationById(String id, String email) {
        Reservation reservation = reservationsDynamoDbTable.getItem(Key.builder().partitionValue(id).build());

        if (reservation == null) {
            return Map.of("statusCode", 404, "message", "Reservation not found for given id");
        }

        if (!reservation.getUserId().equals(email)) {
            return Map.of("statusCode", 403, "message", "You are not authorized to delete this reservation.");
        }

        if (reservation.getStatus().equalsIgnoreCase("cancelled")) {
            return Map.of("statusCode", 408, "message", "Reservation already cancelled");
        }

        LocalDate bookingDate = LocalDate.parse(reservation.getDate());
        LocalDate currentDate = LocalDate.now();

        if (currentDate.isBefore(bookingDate)) {
            reservation.setStatus("Cancelled");
            reservationsDynamoDbTable.updateItem(reservation);
            return Map.of("statusCode", 200, "message", "Reservation Cancelled");
        } else if (currentDate.isAfter(bookingDate)) {
            return Map.of("statusCode", 400, "message", "Bad Request!");
        } else {
            String time = timeMap.get(reservation.getTimeSlot()).get(0);
            String[] parts = time.split(":");
            LocalTime slotTime = LocalTime.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
            LocalTime indianTime = now.toLocalTime();

            if (indianTime.isBefore(slotTime.minusMinutes(30))) {
                reservation.setStatus("Cancelled");
                reservationsDynamoDbTable.updateItem(reservation);
                return Map.of("statusCode", 200, "message", "Reservation Cancelled");
            } else {
                return Map.of("statusCode", 400, "message", "Bad Request!");
            }
        }
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
