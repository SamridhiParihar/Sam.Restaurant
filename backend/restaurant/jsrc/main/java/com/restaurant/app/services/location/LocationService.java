//package com.restaurant.app.services.location;
//
//import com.restaurant.app.models.location.*;
//import com.restaurant.app.services.DynamoDBService;
//
//
//
//import javax.inject.Inject;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class LocationService {
//    private final DynamoDBService dynamoDBService;
////    private static final Logger log = LoggerFactory.getLogger(LocationService.class);
//
//    @Inject
//    public LocationService(DynamoDBService dynamoDBService) {
//        this.dynamoDBService = dynamoDBService;
//    }
//
//    public Map<String,Object>  getAllLocations(){
//        try{
//            List<Location> locations = dynamoDBService.getAllLocations();
//            List<LocationResponse> locationResponses = locations.stream()
//                    .map(LocationResponse::mapToLocationResponse)
//                    .toList();
//            if(locationResponses.isEmpty()){
//                return Map.of(
//                        "statusCode",204,
//                        "body", locationResponses
//                );
//            }
//            return Map.of(
//                    "statusCode",200,
//                    "body",locationResponses
//            );
//
//        }
//        catch (Exception e){
//            return Map.of(
//                    "statusCode",500,
//                    "message","Error retrieving locations "+e.getMessage()
//            );
//        }
//    }
//
//    public Map<String,Object> getLocationSpecialDishes(String id){
//        try{
//            Location location = dynamoDBService.getLocation(id);
//            if(location==null){
//                return Map.of(
//                        "statusCode",404,
//                        "message","Location doesn't exists with id : "+id
//                );
//            }
//            List<SpecialDishResponse> specialDishResponses = location
//                    .getSpecialDishes()
//                    .stream()
//                    .map(dynamoDBService::getDish)
//                    .map(SpecialDishResponse::toSpecialDishResponse)
//                    .toList();
//
//
//            if(specialDishResponses.isEmpty()){
//                return Map.of(
//                        "statusCode",204,
//                        "body", specialDishResponses
//                );
//            }
//
//            return Map.of(
//                    "statusCode",200,
//                    "body",specialDishResponses
//            );
//
//
//        }
//
//        catch (Exception e){
//            return Map.of(
//                    "statusCode",500,
//                    "message","Error retrieving location's special dishes for id "+id+" "+e.getMessage()
//            );
//        }
//
//    }
//    public Map<String,Object> getShortLocations(){
//        try{
//            List<Location> locations = dynamoDBService.getAllLocations();
//            List<ShortLocationResponse> locationResponses = locations.stream()
//                    .map(ShortLocationResponse::toShortLocationResponse)
//                    .toList();
//            if(locationResponses.isEmpty()){
//                return Map.of(
//                        "statusCode",204,
//                        "body", locationResponses
//                );
//            }
//            return Map.of(
//                    "statusCode",200,
//                    "body",locationResponses
//            );
//
//        }
//        catch (Exception e){
//            return Map.of(
//                    "statusCode",500,
//                    "message","Error retrieving locations "+e.getMessage()
//            );
//        }
//    }
//
//
//
//
////     feedback thing one more attempt(biggest blocker)
//
//    public List<Feedback> getFeedbacksUsingPage(List<Feedback> feedbacks, Integer page, Integer pageSize) {
//
//        int index = (page != null) ? page: 0;
//        int size = (pageSize != null ) ? pageSize : 20;
//
//        if (feedbacks == null || feedbacks.isEmpty() || size <= 0 || index < 0) {
//            return new ArrayList<>();
//        }
//
//        int start = index * size;
//        if (start >= feedbacks.size()) {
//            return new ArrayList<>();
//        }
//
//        int end = Math.min(start + size, feedbacks.size());
//        return feedbacks.subList(start, end);
//    }
//
//
//    public List<Feedback> getFeedbackContent(String locationId, Integer pageIndex, Integer size, String typeOfFeedback, String sortCriteria) {
//        Location location = dynamoDBService.getLocation(locationId);
//        if (location == null) {
//            return new ArrayList<>();
//        }
//
//
//        List<Feedback> feedbacks = location.getFeedbacks().stream()
//                .map(dynamoDBService::getFeedback)
//                .filter(feedback -> feedback.getType().equalsIgnoreCase(typeOfFeedback))
//                .collect(Collectors.toList());
//
//
//        // Sorting
//        try {
//            List<String> sortList = List.of(sortCriteria.split(","));
//            String sortParam = sortList.get(0);
//            String ascOrDesc = sortList.get(1);
//
//
//            switch (sortParam.toLowerCase()) {
//                case "rating":
//                    if(ascOrDesc.equalsIgnoreCase("desc")){
//                        feedbacks.sort((x,y)->Double.compare(y.getRate(), x.getRate()));
//                    }
//                    else feedbacks.sort((x,y)->Double.compare(x.getRate(), y.getRate()));
//
//                    break;
//
//                case "date":
//                    sortFeedbackByDate(feedbacks, ascOrDesc.equalsIgnoreCase("asc"));
//                    break;
//
//                default:
//                    feedbacks.sort((x,y)->Double.compare(x.getRate(), y.getRate()));
//            }
//        } catch (Exception e) {
//            System.out.println("Raj Error while sorting feedbacks: "+  e.getMessage() + e);
//        }
//
//        return getFeedbacksUsingPage(feedbacks, pageIndex, size);
//    }
//
//
//    public static void sortFeedbackByDate(List<Feedback> feedbackList, boolean ascending) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
//
//        feedbackList.sort((f1, f2) -> {
//            LocalDateTime date1 = LocalDateTime.parse(f1.getDate(), formatter);
//            LocalDateTime date2 = LocalDateTime.parse(f2.getDate(), formatter);
//            return ascending ? date1.compareTo(date2) : date2.compareTo(date1);
//        });
//    }
//
//
//    public Map<String ,Object>  getPagedFeedbackContent(String locationId, Integer pageIndex, Integer size, String typeOfFeedback, String sortCriteria) {
//        try{
//            // Retrieve the paged and sorted list of Feedback items using your existing business logic.
//            List<Feedback> pagedFeedbacks = getFeedbackContent(locationId, pageIndex, size, typeOfFeedback, sortCriteria);
//
//            // Retrieve the complete list (filtered by feedback type) to compute pagination metadata.
//            Location location = dynamoDBService.getLocation(locationId);
//
//            if (location == null) {
//                return Map.of("statusCode", 404,
//                        "message", "Location not found");
//            }
//
//
//            List<Feedback> allFeedbacks = location.getFeedbacks().stream()
//                    .map(dynamoDBService::getFeedback)
//                    .filter(feedback -> feedback.getType().equalsIgnoreCase(typeOfFeedback))
//                    .toList();
//
//            int totalElements = allFeedbacks.size();
//            int totalPages = (int) Math.ceil((double) totalElements / size);
//
//            // Map each Feedback to a FeedbackResponse.
//            List<FeedbackResponse> feedbackResponses = pagedFeedbacks.stream()
//                    .map(f -> new FeedbackResponse(
//                            f.getId(),
//                            String.valueOf(f.getRate()),
//                            f.getComment(),
//                            f.getUserName(),
//                            f.getUserAvatarUrl(),
//                            String.valueOf(f.getDate()),
//                            f.getType(),
//                            f.getLocationId()))
//                    .toList();
//
//            // Build the sort metadata from the sortCriteria string.
//            // Expected format: "property,direction" (for example, "rating,asc").
//            List<SortObject> sortObjects = new ArrayList<>();
//            if (sortCriteria != null && sortCriteria.contains(",")) {
//                String[] parts = sortCriteria.split(",");
//                if (parts.length == 2) {
//                    String property = parts[0].trim();
//                    String direction = parts[1].trim();
//                    boolean ascending = direction.equalsIgnoreCase("asc");
//                    sortObjects.add(new SortObject(direction, "NONE", ascending, property, true));
//                }
//            }
//
//            // Create a PageableObject.
//            // For this example, offset is calculated as pageIndex * size.
//            PageableObject pageable = new PageableObject(pageIndex * size, sortObjects, true, size, pageIndex, false);
//
//            // Determine if this page is the first or last, and count elements.
//            boolean isFirst = pageIndex == 0;
//            boolean isLast = pageIndex >= totalPages - 1;
//            int numberOfElements = feedbackResponses.size();
//            boolean empty = feedbackResponses.isEmpty();
//
//            // Construct and return the PageFeedbackResponse.
//            PageFeedbackResponse pageFeedbackResponse = new  PageFeedbackResponse(
//                    totalPages,
//                    totalElements,
//                    size,
//                    feedbackResponses,
//                    pageIndex,
//                    sortObjects,
//                    isFirst,
//                    isLast,
//                    numberOfElements,
//                    pageable,
//                    empty
//            );
//            return Map.of(
//                    "statusCode",200,
//                    "body",pageFeedbackResponse
//            );
//
//        }
//        catch (Exception e){
//            return Map.of(
//                    "statusCode",500,
//                    "message","Error while retrieving feedbacks from location"
//            );
//        }
//
//    }
//
//}


package com.restaurant.app.services.location;

import com.restaurant.app.models.location.*;
import com.restaurant.app.services.DynamoDBService;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class that handles business logic related to restaurant locations,
 * including fetching locations, special dishes, and feedback with pagination and sorting.
 */
public class LocationService {

    private final DynamoDBService dynamoDBService;

    /**
     * Constructor for dependency injection.
     *
     * @param dynamoDBService Service to interact with DynamoDB.
     */
    @Inject
    public LocationService(DynamoDBService dynamoDBService) {
        this.dynamoDBService = dynamoDBService;
    }

    /**
     * Retrieves all locations from the database.
     *
     * @return A map containing HTTP status code and body with a list of locations.
     */
    public Map<String, Object> getAllLocations() {
        try {
            List<LocationResponse> locationResponses = dynamoDBService.getAllLocations().stream()
                    .map(LocationResponse::mapToLocationResponse)
                    .collect(Collectors.toList());

            int statusCode = locationResponses.isEmpty() ? 204 : 200;
            return Map.of("statusCode", statusCode, "body", locationResponses);
        } catch (Exception e) {
            return errorResponse("Error retrieving locations ", e);
        }
    }

    /**
     * Retrieves special dishes for a specific location.
     *
     * @param id Location ID.
     * @return A map with HTTP status code and list of special dishes.
     */
    public Map<String, Object> getLocationSpecialDishes(String id) {
        try {
            Location location = dynamoDBService.getLocation(id);
            if (location == null) {
                return Map.of("statusCode", 404, "message", "Location doesn't exist with id: " + id);
            }

            List<SpecialDishResponse> specialDishResponses = location.getSpecialDishes().stream()
                    .map(dynamoDBService::getDish)
                    .map(SpecialDishResponse::toSpecialDishResponse)
                    .collect(Collectors.toList());

            int statusCode = specialDishResponses.isEmpty() ? 204 : 200;
            return Map.of("statusCode", statusCode, "body", specialDishResponses);
        } catch (Exception e) {
            return errorResponse("Error retrieving location's special dishes for id " + id, e);
        }
    }

    /**
     * Retrieves short version (id, name) of all locations.
     *
     * @return A map with status code and short location data.
     */
    public Map<String, Object> getShortLocations() {
        try {
            List<ShortLocationResponse> locationResponses = dynamoDBService.getAllLocations().stream()
                    .map(ShortLocationResponse::toShortLocationResponse)
                    .collect(Collectors.toList());

            int statusCode = locationResponses.isEmpty() ? 204 : 200;
            return Map.of("statusCode", statusCode, "body", locationResponses);
        } catch (Exception e) {
            return errorResponse("Error retrieving short locations ", e);
        }
    }

    /**
     * Applies pagination to a list of feedbacks.
     *
     * @param feedbacks List of all feedbacks.
     * @param page      Page number (0-indexed).
     * @param pageSize  Number of items per page.
     * @return Sublist of feedbacks for the given page.
     */
    public List<Feedback> getFeedbacksUsingPage(List<Feedback> feedbacks, Integer page, Integer pageSize) {
        int index = Optional.ofNullable(page).orElse(0);
        int size = Optional.ofNullable(pageSize).orElse(20);

        if (feedbacks == null || feedbacks.isEmpty() || size <= 0 || index < 0) {
            return new ArrayList<>();
        }

        int start = index * size;
        if (start >= feedbacks.size()) {
            return new ArrayList<>();
        }

        int end = Math.min(start + size, feedbacks.size());
        return feedbacks.subList(start, end);
    }

    /**
     * Retrieves and sorts feedbacks for a specific location and feedback type.
     *
     * @param locationId      Location ID.
     * @param pageIndex       Page index (0-based).
     * @param size            Page size.
     * @param typeOfFeedback  Feedback type (e.g., "service", "food").
     * @param sortCriteria    Sorting criteria, format: "rating,asc" or "date,desc".
     * @return A paginated list of sorted feedbacks.
     */
    public List<Feedback> getFeedbackContent(String locationId, Integer pageIndex, Integer size,
                                             String typeOfFeedback, String sortCriteria) {
        Location location = dynamoDBService.getLocation(locationId);
        if (location == null) return new ArrayList<>();

        List<Feedback> feedbacks = location.getFeedbacks().stream()
                .map(dynamoDBService::getFeedback)
                .filter(f -> f.getType().equalsIgnoreCase(typeOfFeedback))
                .collect(Collectors.toList());

        sortFeedbacks(feedbacks, sortCriteria);
        return getFeedbacksUsingPage(feedbacks, pageIndex, size);
    }

    /**
     * Sorts a list of feedbacks based on given criteria.
     *
     * @param feedbacks    List of feedbacks to be sorted.
     * @param sortCriteria Format: "rating,asc" or "date,desc".
     */
    private void sortFeedbacks(List<Feedback> feedbacks, String sortCriteria) {
        try {
            String[] parts = sortCriteria.split(",");
            String sortParam = parts[0].trim();
            boolean ascending = parts[1].trim().equalsIgnoreCase("asc");

            switch (sortParam.toLowerCase()) {
                case "rating" ->
                        feedbacks.sort((x, y) -> ascending ? Double.compare(x.getRate(), y.getRate()) : Double.compare(y.getRate(), x.getRate()));
                case "date" -> sortFeedbackByDate(feedbacks, ascending);
                default -> feedbacks.sort(Comparator.comparingDouble(Feedback::getRate)); // Default sort by rating
            }
        } catch (Exception e) {
            System.out.println("Error while sorting feedbacks: " + e.getMessage());
        }
    }

    /**
     * Sorts a list of feedbacks by date.
     *
     * @param feedbackList List of feedbacks.
     * @param ascending    Whether to sort ascending or descending.
     */
    public static void sortFeedbackByDate(List<Feedback> feedbackList, boolean ascending) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        feedbackList.sort((f1, f2) -> {
            LocalDateTime d1 = LocalDateTime.parse(f1.getDate(), formatter);
            LocalDateTime d2 = LocalDateTime.parse(f2.getDate(), formatter);
            return ascending ? d1.compareTo(d2) : d2.compareTo(d1);
        });
    }

    /**
     * Returns a paginated and sorted response of feedbacks for a location.
     *
     * @param locationId     Location ID.
     * @param pageIndex      Current page index.
     * @param size           Page size.
     * @param typeOfFeedback Type of feedback (food, service, etc.).
     * @param sortCriteria   Sorting instruction (e.g., "rating,asc").
     * @return Response map with HTTP status and paginated feedback info.
     */
    public Map<String, Object> getPagedFeedbackContent(String locationId, Integer pageIndex, Integer size,
                                                       String typeOfFeedback, String sortCriteria) {
        try {
            List<Feedback> pagedFeedbacks = getFeedbackContent(locationId, pageIndex, size, typeOfFeedback, sortCriteria);
            Location location = dynamoDBService.getLocation(locationId);

            if (location == null) {
                return Map.of("statusCode", 404, "message", "Location not found");
            }

            List<Feedback> allFeedbacks = location.getFeedbacks().stream()
                    .map(dynamoDBService::getFeedback)
                    .filter(f -> f.getType().equalsIgnoreCase(typeOfFeedback))
                    .toList();

            int totalElements = allFeedbacks.size();
            int totalPages = (int) Math.ceil((double) totalElements / size);

            List<FeedbackResponse> feedbackResponses = pagedFeedbacks.stream()
                    .map(f -> new FeedbackResponse(
                            f.getId(),
                            String.valueOf(f.getRate()),
                            f.getComment(),
                            f.getUserName(),
                            f.getUserAvatarUrl(),
                            String.valueOf(f.getDate()),
                            f.getType(),
                            f.getLocationId()
                    )).toList();

            boolean isFirst = pageIndex == 0;
            boolean isLast = pageIndex >= totalPages - 1;
            int numberOfElements = feedbackResponses.size();
            boolean empty = feedbackResponses.isEmpty();

            List<SortObject> sortObjects = buildSortObjects(sortCriteria);
            PageableObject pageable = new PageableObject(pageIndex * size, sortObjects, true, size, pageIndex, false);

            PageFeedbackResponse pageFeedbackResponse = new PageFeedbackResponse(
                    totalPages, totalElements, size, feedbackResponses,
                    pageIndex, sortObjects, isFirst, isLast,
                    numberOfElements, pageable, empty
            );

            return Map.of("statusCode", 200, "body", pageFeedbackResponse);
        } catch (Exception e) {
            return errorResponse("Error while retrieving feedbacks from location", e);
        }
    }

    /**
     * Builds a list of SortObject based on the given sort string.
     *
     * @param sortCriteria Format: "field,direction"
     * @return A list of sort objects to be passed to pageable.
     */
    private List<SortObject> buildSortObjects(String sortCriteria) {
        List<SortObject> sortObjects = new ArrayList<>();
        if (sortCriteria != null && sortCriteria.contains(",")) {
            String[] parts = sortCriteria.split(",");
            if (parts.length == 2) {
                String property = parts[0].trim();
                String direction = parts[1].trim();
                boolean ascending = direction.equalsIgnoreCase("asc");
                sortObjects.add(new SortObject(direction, "NONE", ascending, property, true));
            }
        }
        return sortObjects;
    }

    /**
     * Utility method to return a consistent error response format.
     *
     * @param message Custom message.
     * @param e       Exception thrown.
     * @return A map with error status and message.
     */
    private Map<String, Object> errorResponse(String message, Exception e) {
        return Map.of("statusCode", 500, "message", message + e.getMessage());
    }
}
