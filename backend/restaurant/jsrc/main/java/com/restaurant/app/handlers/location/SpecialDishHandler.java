//package com.restaurant.app.handlers.location;
//
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.restaurant.app.models.location.SpecialDishResponse;
//import com.restaurant.app.services.location.LocationService;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import javax.inject.Inject;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//public class SpecialDishHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
//
//    private final LocationService locationService ;
//
//    @Inject
//    public SpecialDishHandler(LocationService locationService) {
//        this.locationService = locationService;
//    }
//
//    @Override
//    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
//        try {
//            Map<String, String> pathParams = request.getPathParameters();
//            String id = pathParams != null ? pathParams.get("id") : null;
//
//            if (id == null || id.isEmpty()) {
//                return createResponse(400, "Missing dish ID in path parameters");
//            }
//
//            Map<String,Object> response = locationService.getLocationSpecialDishes(id);
//            int statusCode = (int) response.get("statusCode");
//
//            if(statusCode==200){
//                List<SpecialDishResponse> specialDishResponses = (List<SpecialDishResponse>)response.get("body");
//                return createSuccessResponse(specialDishResponses);
//            }
//            else{
//                return createResponse(statusCode,(String)response.get("message"));
//            }
//
//        }
//        catch (Exception e){
//            return createResponse(500,"Internal Server Error "+e.getMessage());
//        }
//    }
//
////    public APIGatewayProxyResponseEvent createSuccessResponse(List<SpecialDishResponse> specialDishResponses) {
////        JSONArray jsonArray = new JSONArray();
////
////        for (SpecialDishResponse specialDish : specialDishResponses) {
////            JSONObject dishJson = new JSONObject();
////            dishJson.put("name", specialDish.getName());
////            dishJson.put("price", specialDish.getPrice());
////            dishJson.put("weight", specialDish.getWeight());
////            dishJson.put("imageUrl", specialDish.getImageUrl());
////            jsonArray.put(dishJson);
////        }
////
////        return new APIGatewayProxyResponseEvent()
////                .withStatusCode(200)
////                .withBody(jsonArray.toString());
////    }
//
//    public APIGatewayProxyResponseEvent createSuccessResponse(List<SpecialDishResponse> specialDishResponses) throws Exception {
//        List<Map<String, Object>> responseList = new ArrayList<>();
//
//        for (SpecialDishResponse specialDish : specialDishResponses) {
//            Map<String, Object> dishMap = new LinkedHashMap<>(); // preserves insertion order
//            dishMap.put("name", specialDish.getName());
//            dishMap.put("price", specialDish.getPrice());
//            dishMap.put("weight", specialDish.getWeight());
//            dishMap.put("imageUrl", specialDish.getImageUrl());
//            responseList.add(dishMap);
//        }
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonBody = objectMapper.writeValueAsString(responseList);
//
//        return new APIGatewayProxyResponseEvent()
//                .withStatusCode(200)
//                .withBody(jsonBody);
//    }
//
//    private APIGatewayProxyResponseEvent createResponse(int statusCode, String message) {
//        return new APIGatewayProxyResponseEvent()
//                .withStatusCode(statusCode)
//                .withBody(new JSONObject().put("message", message).toString());
//    }
//
//
//
//}

package com.restaurant.app.handlers.location;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.app.models.location.SpecialDishResponse;
import com.restaurant.app.services.location.LocationService;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * AWS Lambda handler for retrieving special dishes offered at a specific restaurant location.
 *
 * Triggered via API Gateway and returns a list of dish details (name, price, weight, imageUrl) for a given location ID.
 */
public class SpecialDishHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final LocationService locationService;

    /**
     * Constructor for dependency injection of the LocationService.
     *
     * @param locationService The service responsible for location-related business logic.
     */
    @Inject
    public SpecialDishHandler(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * Main entry point for the Lambda function. It processes incoming API Gateway requests.
     *
     * @param request The API Gateway request event containing path parameters.
     * @param context The AWS Lambda context object (includes metadata, logger, etc.).
     * @return A structured API Gateway response, either a list of dishes or an error message.
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            // Extract path parameters from the request to get the location ID
            Map<String, String> pathParams = request.getPathParameters();
            String id = (pathParams != null) ? pathParams.get("id") : null;

            // Return 400 Bad Request if no ID is provided
            if (id == null || id.isEmpty()) {
                return createResponse(400, "Missing dish ID in path parameters");
            }

            // Delegate to the service layer to fetch special dishes for the given ID
            Map<String, Object> response = locationService.getLocationSpecialDishes(id);
            int statusCode = (int) response.get("statusCode");

            // Return a successful response if status is 200
            if (statusCode == 200) {
                List<SpecialDishResponse> dishes = (List<SpecialDishResponse>) response.get("body");
                return createSuccessResponse(dishes);
            } else {
                // Return service-level error message
                return createResponse(statusCode, (String) response.get("message"));
            }

        } catch (Exception e) {
            // Catch-all for unexpected errors
            return createResponse(500, "Internal Server Error " + e.getMessage());
        }
    }

    /**
     * Builds a success response by converting the list of special dishes to JSON.
     *
     * @param specialDishResponses The list of special dishes to include in the response.
     * @return APIGatewayProxyResponseEvent with HTTP 200 status and JSON array of dishes.
     * @throws Exception If JSON serialization fails.
     */
    public APIGatewayProxyResponseEvent createSuccessResponse(List<SpecialDishResponse> specialDishResponses) throws Exception {
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (SpecialDishResponse dish : specialDishResponses) {
            // Maintain insertion order using LinkedHashMap
            Map<String, Object> dishMap = new LinkedHashMap<>();
            dishMap.put("name", dish.getName());
            dishMap.put("price", dish.getPrice());
            dishMap.put("weight", dish.getWeight());
            dishMap.put("imageUrl", dish.getImageUrl());
            responseList.add(dishMap);
        }

        // Convert list of maps to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(responseList);

        // Return a successful API response
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(jsonBody);
    }

    /**
     * Constructs a standardized error response in JSON format.
     *
     * @param statusCode The HTTP status code to return.
     * @param message    The error message to include in the response body.
     * @return APIGatewayProxyResponseEvent with the error message.
     */
    private APIGatewayProxyResponseEvent createResponse(int statusCode, String message) {
        JSONObject errorJson = new JSONObject().put("message", message);
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody(errorJson.toString());
    }
}

