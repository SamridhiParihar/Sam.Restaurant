//package com.restaurant.app.handlers.location;
//
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
//import com.restaurant.app.models.location.LocationResponse;
//import com.restaurant.app.services.location.LocationService;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import javax.inject.Inject;
//import java.util.List;
//import java.util.Map;
//
//public class LocationHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
//
//    private final LocationService  locationService;
//
//    @Inject
//    public LocationHandler(LocationService locationService) {
//        this.locationService = locationService;
//    }
//
//    @Override
//    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
//        try{
//            Map<String,Object> response = locationService.getAllLocations();
//            int statusCode = (Integer) response.get("statusCode");
//
//
//            if(statusCode==200){
//                List<LocationResponse> locationResponses = (List<LocationResponse>)response.get("body");
//                return createSuccessResponse(locationResponses);
//            }
//            else{
//                return createResponse(statusCode, (String) response.get("message"));
//            }
//
//        }
//        catch (Exception e){
//            return createResponse(500, "Internal Server Error: " + e.getMessage());
//
//        }
//
//    }
//
//    public APIGatewayProxyResponseEvent createSuccessResponse(List<LocationResponse> locationResponses) {
//        JSONArray jsonArray = new JSONArray();
//
//        for (LocationResponse location : locationResponses) {
//            JSONObject locationJson = new JSONObject();
//            locationJson.put("id", location.getId());
//            locationJson.put("address", location.getAddress());
//            locationJson.put("description", location.getDescription());
//            locationJson.put("totalCapacity", location.getTotalCapacity());
//            locationJson.put("averageOccupancy", location.getAverageOccupancy());
//            locationJson.put("imageUrl", location.getImageUrl());
//            locationJson.put("rating", location.getRating());
//            jsonArray.put(locationJson);
//        }
//
//        return new APIGatewayProxyResponseEvent()
//                .withStatusCode(200)
//                .withBody(jsonArray.toString());
//    }
//
//    private APIGatewayProxyResponseEvent createResponse(int statusCode, String message) {
//        return new APIGatewayProxyResponseEvent()
//                .withStatusCode(statusCode)
//                .withBody(new JSONObject().put("message", message).toString());
//    }
//}

package com.restaurant.app.handlers.location;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.restaurant.app.models.location.LocationResponse;
import com.restaurant.app.services.location.LocationService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * AWS Lambda handler that responds to HTTP requests for retrieving
 * all restaurant locations.
 * <p>
 * This handler is invoked via API Gateway and returns a list of
 * {@link LocationResponse} objects serialized as JSON.
 */
public class LocationHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final LocationService locationService;

    /**
     * Constructs the handler with a {@link LocationService} instance, injected by Dagger.
     *
     * @param locationService Service layer used to fetch location data.
     */
    @Inject
    public LocationHandler(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * Main Lambda handler method that gets triggered by an API Gateway HTTP request.
     *
     * @param request The incoming API Gateway HTTP request.
     * @param context The Lambda context providing metadata and logging.
     * @return An API Gateway-compatible HTTP response containing location data or error details.
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            // Call service to get location data
            Map<String, Object> response = locationService.getAllLocations();
            int statusCode = (Integer) response.get("statusCode");

            // Handle success or failure based on response status code
            if (statusCode == 200) {
                List<LocationResponse> locations = (List<LocationResponse>) response.get("body");
                return buildSuccessResponse(locations);
            } else {
                return buildErrorResponse(statusCode, (String) response.get("message"));
            }

        } catch (Exception e) {
            // Handle unexpected exceptions
            return buildErrorResponse(500, "Internal Server Error: " + e.getMessage());
        }
    }

    /**
     * Builds a successful HTTP response containing a JSON array of location data.
     *
     * @param locations A list of {@link LocationResponse} objects to return.
     * @return API Gateway-compatible response with HTTP 200 and JSON body.
     */
    private APIGatewayProxyResponseEvent buildSuccessResponse(List<LocationResponse> locations) {
        JSONArray jsonArray = new JSONArray();

        // Convert each location to JSON format
        for (LocationResponse location : locations) {
            jsonArray.put(buildLocationJson(location));
        }

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(jsonArray.toString());
    }

    /**
     * Converts a single {@link LocationResponse} object into a JSON representation.
     *
     * @param location The location object to convert.
     * @return A JSONObject containing location properties.
     */
    private JSONObject buildLocationJson(LocationResponse location) {
        JSONObject json = new JSONObject();
        json.put("id", location.getId());
        json.put("address", location.getAddress());
        json.put("description", location.getDescription());
        json.put("totalCapacity", location.getTotalCapacity());
        json.put("averageOccupancy", location.getAverageOccupancy());
        json.put("imageUrl", location.getImageUrl());
        json.put("rating", location.getRating());
        return json;
    }

    /**
     * Builds an error HTTP response with the given status code and message.
     *
     * @param statusCode The HTTP status code to return.
     * @param message    A human-readable error message.
     * @return API Gateway-compatible response with error message in JSON.
     */
    private APIGatewayProxyResponseEvent buildErrorResponse(int statusCode, String message) {
        JSONObject error = new JSONObject().put("message", message);
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody(error.toString());
    }
}
