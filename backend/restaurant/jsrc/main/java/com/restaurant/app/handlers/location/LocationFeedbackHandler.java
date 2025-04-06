//package com.restaurant.app.handlers.location;
//
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
//import com.restaurant.app.models.location.PageFeedbackResponse;
//import com.restaurant.app.models.location.FeedbackResponse;
//import com.restaurant.app.models.location.PageableObject;
//import com.restaurant.app.models.location.SortObject;
//import com.restaurant.app.services.location.LocationService;
//import org.json.JSONArray;
//
//import org.json.JSONObject;
//
//import javax.inject.Inject;
//import java.util.*;
//
//
//public class LocationFeedbackHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
//
//    private final LocationService locationService;
//
//    @Inject
//    public LocationFeedbackHandler(LocationService locationService) {
//        this.locationService = locationService;
//    }
//
//    @Override
//    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
//        try {
//            Map<String, String> pathParams = request.getPathParameters();
//            if (pathParams == null || !pathParams.containsKey("id")) {
//                return createResponse(400, "Missing location ID in path parameters");
//            }
//            String locationId = pathParams.get("id");
//            if(locationId==null){
//                return createResponse(400, "Missing location ID in path parameters");
//
//            }
//
//            Map<String, String> queryParams = request.getQueryStringParameters() != null
//                    ? request.getQueryStringParameters()
//                    : Collections.emptyMap();
//
//
//            // Extract parameters
//            String type = queryParams.get("type");
//            if(type==null){
//                return createResponse(400, "Missing type in path parameters");
//            }
//
//            int size = Integer.parseInt(queryParams.getOrDefault("size", "20"));
//            String sortCriteria = queryParams.getOrDefault("sort","rating,asc");
//            int page = Integer.parseInt(queryParams.getOrDefault("page", "0"));
//
//
//            Map<String ,Object> response = locationService.getPagedFeedbackContent(locationId,page,size,type,sortCriteria);
//            int statusCode =(Integer) response.get("statusCode");
//            if(statusCode==200){
//                return createSuccessResponse(response);
//            }
//
//            return createResponse(statusCode,response.get("message"));
//
//
//        } catch (Exception e) {
//            return createResponse(500, "Internal error: " + e.getMessage());
//        }
//    }
//
//    private APIGatewayProxyResponseEvent createSuccessResponse(Map<String, Object> response) {
//        // Retrieve the PageFeedbackResponse from the response Map
//        PageFeedbackResponse pageFeedbackResponse = (PageFeedbackResponse) response.get("body");
//
//        // Build the main JSON object
//        JSONObject json = new JSONObject();
//        json.put("totalPages", pageFeedbackResponse.getTotalPages());
//        json.put("totalElements", pageFeedbackResponse.getTotalElements());
//        json.put("size", pageFeedbackResponse.getSize());
//
//        // Build content array from FeedbackResponse items
//        JSONArray contentArray = new JSONArray();
//        for (FeedbackResponse fb : pageFeedbackResponse.getContent()) {
//            JSONObject fbJson = new JSONObject();
//            fbJson.put("id", fb.getId());
//            fbJson.put("rate", fb.getRate());
//            fbJson.put("comment", fb.getComment());
//            fbJson.put("userName", fb.getUserName());
//            fbJson.put("userAvatarUrl", fb.getUserAvatarUrl());
//            fbJson.put("date", fb.getDate());
//            fbJson.put("type", fb.getType());
//            fbJson.put("locationId", fb.getLocationId());
//            contentArray.put(fbJson);
//        }
//        json.put("content", contentArray);
//
//        json.put("number", pageFeedbackResponse.getNumber());
//
//        // Build sort array from SortObject items
//        JSONArray sortArray = new JSONArray();
//        if (pageFeedbackResponse.getSort() != null) {
//            for (SortObject so : pageFeedbackResponse.getSort()) {
//                JSONObject soJson = new JSONObject();
//                soJson.put("direction", so.getDirection());
//                soJson.put("nullHandling", so.getNullHandling());
//                soJson.put("ascending", so.isAscending());
//                soJson.put("property", so.getProperty());
//                soJson.put("ignoreCase", so.isIgnoreCase());
//                sortArray.put(soJson);
//            }
//        }
//        json.put("sort", sortArray);
//
//        json.put("first", pageFeedbackResponse.isFirst());
//        json.put("last", pageFeedbackResponse.isLast());
//        json.put("numberOfElements", pageFeedbackResponse.getNumberOfElements());
//
//        // Build pageable JSON object
//        JSONObject pageableJson = new JSONObject();
//        pageableJson.put("offset", pageFeedbackResponse.getPageable().getOffset());
//
//        JSONArray pageableSortArray = new JSONArray();
//        if (pageFeedbackResponse.getPageable().getSort() != null) {
//            for (SortObject so : pageFeedbackResponse.getPageable().getSort()) {
//                JSONObject soJson = new JSONObject();
//                soJson.put("direction", so.getDirection());
//                soJson.put("nullHandling", so.getNullHandling());
//                soJson.put("ascending", so.isAscending());
//                soJson.put("property", so.getProperty());
//                soJson.put("ignoreCase", so.isIgnoreCase());
//                pageableSortArray.put(soJson);
//            }
//        }
//        pageableJson.put("sort", pageableSortArray);
//        pageableJson.put("paged", pageFeedbackResponse.getPageable().isPaged());
//        pageableJson.put("pageSize", pageFeedbackResponse.getPageable().getPageSize());
//        pageableJson.put("pageNumber", pageFeedbackResponse.getPageable().getPageNumber());
//        pageableJson.put("unpaged", pageFeedbackResponse.getPageable().isUnpaged());
//
//        json.put("pageable", pageableJson);
//        json.put("empty", pageFeedbackResponse.isEmpty());
//
//        return new APIGatewayProxyResponseEvent()
//                .withStatusCode(200)
//                .withBody(json.toString());
//    }
//
//    private APIGatewayProxyResponseEvent createResponse(int statusCode, Object message) {
//        JSONObject json = new JSONObject();
//        json.put("message", message);
//        return new APIGatewayProxyResponseEvent()
//                .withStatusCode(statusCode)
//                .withBody(json.toString());
//    }
//
//}

package com.restaurant.app.handlers.location;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.restaurant.app.models.location.PageFeedbackResponse;
import com.restaurant.app.models.location.FeedbackResponse;
import com.restaurant.app.models.location.SortObject;
import com.restaurant.app.services.location.LocationService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.*;

/**
 * AWS Lambda handler to process feedback requests for a specific restaurant location.
 * Retrieves paginated feedback data based on location ID, type, and optional query parameters.
 */
public class LocationFeedbackHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final LocationService locationService;

    /**
     * Constructor to inject LocationService dependency.
     * @param locationService Service that provides feedback-related operations.
     */
    @Inject
    public LocationFeedbackHandler(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * Handles the API Gateway request to fetch paged feedback data.
     *
     * @param request APIGatewayProxyRequestEvent containing path and query parameters.
     * @param context AWS Lambda execution context.
     * @return APIGatewayProxyResponseEvent containing paginated feedback or error message.
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            // Extract location ID from path parameters
            String locationId = extractLocationId(request.getPathParameters());
            if (locationId == null) {
                return createResponse(400, "Missing location ID in path parameters");
            }

            // Extract and validate query parameters
            Map<String, String> queryParams = Optional.ofNullable(request.getQueryStringParameters())
                    .orElse(Collections.emptyMap());

            String type = queryParams.get("type");
            if (type == null) {
                return createResponse(400, "Missing type in path parameters");
            }

            int size = Integer.parseInt(queryParams.getOrDefault("size", "20"));
            int page = Integer.parseInt(queryParams.getOrDefault("page", "0"));
            String sortCriteria = queryParams.getOrDefault("sort", "rating,asc");

            // Call service to get paginated feedback
            Map<String, Object> response = locationService.getPagedFeedbackContent(locationId, page, size, type, sortCriteria);
            int statusCode = (Integer) response.get("statusCode");

            // Return response based on status code
            return (statusCode == 200)
                    ? createSuccessResponse(response)
                    : createResponse(statusCode, response.get("message"));

        } catch (Exception e) {
            return createResponse(500, "Internal error: " + e.getMessage());
        }
    }

    /**
     * Helper method to extract the location ID from path parameters.
     *
     * @param pathParams Map of path parameters.
     * @return location ID or null if not found.
     */
    private String extractLocationId(Map<String, String> pathParams) {
        return (pathParams != null && pathParams.containsKey("id")) ? pathParams.get("id") : null;
    }

    /**
     * Builds a successful API response with paginated feedback data.
     *
     * @param response Map containing status code and body with PageFeedbackResponse.
     * @return APIGatewayProxyResponseEvent with HTTP 200 status and feedback JSON.
     */
    private APIGatewayProxyResponseEvent createSuccessResponse(Map<String, Object> response) {
        PageFeedbackResponse pageFeedback = (PageFeedbackResponse) response.get("body");
        JSONObject json = new JSONObject();

        // Basic pagination details
        json.put("totalPages", pageFeedback.getTotalPages());
        json.put("totalElements", pageFeedback.getTotalElements());
        json.put("size", pageFeedback.getSize());
        json.put("number", pageFeedback.getNumber());
        json.put("first", pageFeedback.isFirst());
        json.put("last", pageFeedback.isLast());
        json.put("numberOfElements", pageFeedback.getNumberOfElements());
        json.put("empty", pageFeedback.isEmpty());

        // Add feedback list
        json.put("content", buildFeedbackContentArray(pageFeedback.getContent()));

        // Add sort and pagination metadata
        json.put("sort", buildSortArray(pageFeedback.getSort()));
        json.put("pageable", buildPageableObject(pageFeedback));

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(json.toString());
    }

    /**
     * Converts list of FeedbackResponse objects into a JSON array.
     *
     * @param content List of feedback items.
     * @return JSONArray with feedback item objects.
     */
    private JSONArray buildFeedbackContentArray(List<FeedbackResponse> content) {
        JSONArray array = new JSONArray();
        for (FeedbackResponse fb : content) {
            JSONObject fbJson = new JSONObject();
            fbJson.put("id", fb.getId());
            fbJson.put("rate", fb.getRate());
            fbJson.put("comment", fb.getComment());
            fbJson.put("userName", fb.getUserName());
            fbJson.put("userAvatarUrl", fb.getUserAvatarUrl());
            fbJson.put("date", fb.getDate());
            fbJson.put("type", fb.getType());
            fbJson.put("locationId", fb.getLocationId());
            array.put(fbJson);
        }
        return array;
    }

    /**
     * Converts a list of SortObject into a JSON array.
     *
     * @param sortList List of SortObject.
     * @return JSONArray of sort criteria.
     */
    private JSONArray buildSortArray(List<SortObject> sortList) {
        JSONArray sortArray = new JSONArray();
        if (sortList != null) {
            for (SortObject so : sortList) {
                sortArray.put(buildSortJson(so));
            }
        }
        return sortArray;
    }

    /**
     * Builds JSON object for pagination information including sort config.
     *
     * @param pageFeedback PageFeedbackResponse containing pageable object.
     * @return JSONObject with pagination metadata.
     */
    private JSONObject buildPageableObject(PageFeedbackResponse pageFeedback) {
        JSONObject pageableJson = new JSONObject();
        pageableJson.put("offset", pageFeedback.getPageable().getOffset());
        pageableJson.put("paged", pageFeedback.getPageable().isPaged());
        pageableJson.put("pageSize", pageFeedback.getPageable().getPageSize());
        pageableJson.put("pageNumber", pageFeedback.getPageable().getPageNumber());
        pageableJson.put("unpaged", pageFeedback.getPageable().isUnpaged());
        pageableJson.put("sort", buildSortArray(pageFeedback.getPageable().getSort()));
        return pageableJson;
    }

    /**
     * Builds a JSON object from a single SortObject.
     *
     * @param so SortObject to convert.
     * @return JSONObject with sorting configuration.
     */
    private JSONObject buildSortJson(SortObject so) {
        JSONObject json = new JSONObject();
        json.put("direction", so.getDirection());
        json.put("nullHandling", so.getNullHandling());
        json.put("ascending", so.isAscending());
        json.put("property", so.getProperty());
        json.put("ignoreCase", so.isIgnoreCase());
        return json;
    }

    /**
     * Creates a basic JSON error/success response with a message.
     *
     * @param statusCode HTTP status code.
     * @param message Message string or object to send in the response.
     * @return APIGatewayProxyResponseEvent with given status and message body.
     */
    private APIGatewayProxyResponseEvent createResponse(int statusCode, Object message) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody(new JSONObject().put("message", message).toString());
    }
}
