//package com.restaurant.app.handlers.dish;
//
//
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
//import com.restaurant.app.services.dish.DishService;
//
//import org.json.JSONObject;
//
//import javax.inject.Inject;
//import java.util.Map;
//
//// DishHandler.java
//public class DishHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
//    private final DishService dishService;
//
//    @Inject
//    public DishHandler(DishService dishService) {
//        this.dishService = dishService;
//    }
//
//    @Override
//    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
//        try {
//            // Extract ID from path parameters
//            Map<String, String> pathParams = request.getPathParameters();
//            String id = pathParams != null ? pathParams.get("id") : null;
//
//            if (id == null || id.isEmpty()) {
//                return createResponse(400, "Missing dish ID in path parameters");
//            }
//
//            Map<String, Object> serviceResponse = dishService.getDishById(id);
//            int statusCode = (Integer) serviceResponse.get("statusCode");
//            Map<String, Object> body = (Map<String, Object>) serviceResponse.get("body");
//
//            if (statusCode == 200) {
//                return createSuccessResponse(body);
//            } else {
//                return createResponse(statusCode, (String) body.get("message"));
//            }
//
//        } catch (Exception e) {
//            return createResponse(500, "Internal Server Error: " + e.getMessage());
//        }
//    }
//
//    private APIGatewayProxyResponseEvent createSuccessResponse(Map<String, Object> dishData) {
//        JSONObject responseBody = new JSONObject();
//        dishData.forEach((key, value) -> {
//            if (value != null) responseBody.put(key, value);
//        });
//        return new APIGatewayProxyResponseEvent()
//                .withStatusCode(200)
//                .withBody(responseBody.toString());
//    }
//
//    private APIGatewayProxyResponseEvent createResponse(int statusCode, String message) {
//        return new APIGatewayProxyResponseEvent()
//                .withStatusCode(statusCode)
//                .withBody(new JSONObject().put("message", message).toString());
//    }
//}


package com.restaurant.app.handlers.dish;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.restaurant.app.services.dish.DishService;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Map;

/**
 * AWS Lambda handler responsible for processing HTTP requests to fetch dish details by ID.
 * This handler is triggered via API Gateway and interacts with the DishService to retrieve
 * dish information stored in the backend (e.g., DynamoDB).
 */
public class DishHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final DishService dishService;

    /**
     * Constructs a new DishHandler with a DishService dependency.
     *
     * @param dishService The service used to interact with the dish data store.
     */
    @Inject
    public DishHandler(DishService dishService) {
        this.dishService = dishService;
    }

    /**
     * Entry point for the Lambda function.
     * Handles HTTP GET requests for retrieving dish details based on path parameter "id".
     *
     * @param request Incoming API Gateway request event.
     * @param context Lambda execution context (includes metadata such as request ID, function name, etc.).
     * @return API Gateway proxy response containing the dish details or an error message.
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            // Extract dish ID from path parameters
            String dishId = extractDishId(request);

            // Validate that the dish ID is provided
            if (dishId == null || dishId.isEmpty()) {
                return buildErrorResponse(400, "Missing dish ID in path parameters");
            }

            // Call the service to retrieve dish data
            Map<String, Object> serviceResponse = dishService.getDishById(dishId);
            int statusCode = (Integer) serviceResponse.get("statusCode");

            // Cast body to a map for further processing
            Map<String, Object> responseBody = (Map<String, Object>) serviceResponse.get("body");

            // Return success or error response based on service status code
            return (statusCode == 200)
                    ? buildSuccessResponse(responseBody)
                    : buildErrorResponse(statusCode, (String) responseBody.get("message"));

        } catch (Exception e) {
            // Handle unexpected errors gracefully and return a 500 response
            return buildErrorResponse(500, "Internal Server Error: " + e.getMessage());
        }
    }

    /**
     * Extracts the "id" parameter from the API Gateway path parameters.
     *
     * @param request The API Gateway request event.
     * @return The extracted dish ID, or null if not found.
     */
    private String extractDishId(APIGatewayProxyRequestEvent request) {
        Map<String, String> pathParams = request.getPathParameters();
        return pathParams != null ? pathParams.get("id") : null;
    }

    /**
     * Builds a successful API response with dish data in JSON format.
     *
     * @param dishData A map containing dish attributes (e.g., name, price, category).
     * @return A 200 OK API Gateway proxy response with a JSON body.
     */
    private APIGatewayProxyResponseEvent buildSuccessResponse(Map<String, Object> dishData) {
        JSONObject json = new JSONObject();

        // Add non-null entries to the response JSON
        dishData.forEach((key, value) -> {
            if (value != null) json.put(key, value);
        });

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(json.toString());
    }

    /**
     * Builds an error response with the given status code and message.
     *
     * @param statusCode The HTTP status code to return (e.g., 400, 404, 500).
     * @param message    A message describing the error.
     * @return An API Gateway proxy response containing the error message in JSON format.
     */
    private APIGatewayProxyResponseEvent buildErrorResponse(int statusCode, String message) {
        JSONObject errorJson = new JSONObject().put("message", message);

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody(errorJson.toString());
    }
}
