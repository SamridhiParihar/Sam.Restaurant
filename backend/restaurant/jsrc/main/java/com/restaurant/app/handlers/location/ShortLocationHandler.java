//package com.restaurant.app.handlers.location;
//
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
//import com.restaurant.app.models.location.ShortLocationResponse;
//import com.restaurant.app.services.location.LocationService;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import javax.inject.Inject;
//import java.util.List;
//import java.util.Map;
//
//public class ShortLocationHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
//    private final LocationService locationService;
//
//    @Inject
//    public ShortLocationHandler(LocationService locationService) {
//        this.locationService = locationService;
//    }
//
//    @Override
//    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
//        try{
//            Map<String,Object> response = locationService.getShortLocations();
//            int statusCode = (Integer) response.get("statusCode");
//
//
//            if(statusCode==200){
//                List<ShortLocationResponse> locationResponses = (List<ShortLocationResponse>)response.get("body");
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
//    public APIGatewayProxyResponseEvent createSuccessResponse(List<ShortLocationResponse> locationResponses) {
//        JSONArray jsonArray = new JSONArray();
//
//        for (ShortLocationResponse location : locationResponses) {
//            JSONObject locationJson = new JSONObject();
//            locationJson.put("id", location.getId());
//            locationJson.put("address", location.getAddress());
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
import com.restaurant.app.models.location.ShortLocationResponse;
import com.restaurant.app.services.location.LocationService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * AWS Lambda handler that returns a short list of location information (ID and address).
 * This handler is triggered by API Gateway events and returns a JSON response.
 */
public class ShortLocationHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    // Service responsible for fetching location data from the data source.
    private final LocationService locationService;

    /**
     * Constructor for dependency injection. LocationService is provided by Dagger.
     *
     * @param locationService the service used to retrieve short location details
     */
    @Inject
    public ShortLocationHandler(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * Lambda entry point. Handles API Gateway request to return short location data.
     *
     * @param request incoming API Gateway request
     * @param context Lambda execution context
     * @return API Gateway response with list of locations or error message
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            // Invoke the service method to fetch all short location entries
            Map<String, Object> response = locationService.getShortLocations();
            int statusCode = (Integer) response.get("statusCode");

            // If the call was successful, extract and return the list of locations
            if (statusCode == 200) {
                List<ShortLocationResponse> locations = (List<ShortLocationResponse>) response.get("body");
                return buildSuccessResponse(locations);
            } else {
                // If the service returned an error status, build an appropriate error response
                return buildErrorResponse(statusCode, (String) response.get("message"));
            }

        } catch (Exception e) {
            // Catch any unexpected exceptions and return a 500 Internal Server Error response
            return buildErrorResponse(500, "Internal Server Error: " + e.getMessage());
        }
    }

    /**
     * Builds a successful response with a list of short location details.
     *
     * @param locations the list of locations to return
     * @return APIGatewayProxyResponseEvent with JSON body and 200 status
     */
    private APIGatewayProxyResponseEvent buildSuccessResponse(List<ShortLocationResponse> locations) {
        JSONArray jsonArray = new JSONArray();

        // Convert each ShortLocationResponse into a JSONObject and add it to the array
        for (ShortLocationResponse location : locations) {
            JSONObject locationJson = new JSONObject();
            locationJson.put("id", location.getId());               // Add location ID
            locationJson.put("address", location.getAddress());     // Add location address
            jsonArray.put(locationJson);
        }

        // Return the JSON array as the response body with status code 200 (OK)
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(jsonArray.toString());
    }

    /**
     * Builds a structured error response with the given HTTP status and message.
     *
     * @param statusCode the HTTP status code (e.g., 400, 404, 500)
     * @param message    the error message to be returned to the client
     * @return APIGatewayProxyResponseEvent with error JSON body
     */
    private APIGatewayProxyResponseEvent buildErrorResponse(int statusCode, String message) {
        // Create a JSON object with the error message
        JSONObject errorJson = new JSONObject().put("message", message);

        // Return it as the body of the response
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody(errorJson.toString());
    }
}
