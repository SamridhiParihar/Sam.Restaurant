//package com.restaurant.app.handlers.dish;
//
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
//import com.restaurant.app.models.dish.Dish;
//import com.restaurant.app.services.dish.DishService;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import javax.inject.Inject;
//import java.util.List;
//
//public class DishPopularHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
//
//    private final DishService dishService;
//
//    @Inject
//
//    public DishPopularHandler(DishService dishService) {
//
//        this.dishService = dishService;
//
//    }
//
//    @Override
//
//    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
//
//        try {
//
//            List<Dish> popularDishes = dishService.getPopularDishes();
//
//            return createSuccessResponse(popularDishes);
//
//        } catch (Exception e) {
//
//            return createResponse(500, "Internal Server Error: " + e.getMessage());
//
//        }
//
//    }
//
//    private APIGatewayProxyResponseEvent createSuccessResponse(List<Dish> dishes) {
//
//        JSONArray responseBody = new JSONArray();
//
//        for (Dish dish : dishes) {
//
//            String name = dish.getName();
//
//            String price = dish.getPrice();
//
//            String weight = dish.getWeight();
//
//            String imageUrl = dish.getImageUrl();
//
//            JSONObject dishJson = new JSONObject();
//
//            dishJson.put("name", name);
//
//            dishJson.put("price", price);
//
//            dishJson.put("weight", weight);
//
//            dishJson.put("imageUrl", imageUrl);
//
//            responseBody.put(dishJson);
//
//        }
//
//        return new APIGatewayProxyResponseEvent()
//
//                .withStatusCode(200)
//
//                .withBody(responseBody.toString());
//
//    }
//
//    private APIGatewayProxyResponseEvent createResponse(int statusCode, String message) {
//
//        return new APIGatewayProxyResponseEvent()
//
//                .withStatusCode(statusCode)
//
//                .withBody(new JSONObject().put("message", message).toString());
//
//    }
//
//}


package com.restaurant.app.handlers.dish;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.restaurant.app.models.dish.Dish;
import com.restaurant.app.services.dish.DishService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.List;

/**
 * AWS Lambda handler for fetching the list of most popular dishes.
 * Invoked through API Gateway, this handler interacts with the DishService
 * to return a list of dishes in JSON format.
 */
public class DishPopularHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final DishService dishService;

    /**
     * Constructs the handler with dependency injection for DishService.
     *
     * @param dishService Service responsible for dish-related operations.
     */
    @Inject
    public DishPopularHandler(DishService dishService) {
        this.dishService = dishService;
    }

    /**
     * Handles the API Gateway event to return a list of popular dishes.
     *
     * @param request Incoming API Gateway request (not used in this handler).
     * @param context AWS Lambda execution context.
     * @return Response containing a list of popular dishes or error message.
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            List<Dish> popularDishes = dishService.getPopularDishes();
            return buildSuccessResponse(popularDishes);
        } catch (Exception e) {
            return buildErrorResponse(500, "Internal Server Error: " + e.getMessage());
        }
    }

    /**
     * Builds a 200 OK response with a JSON array of dish data.
     *
     * @param dishes List of popular Dish objects.
     * @return API Gateway response with a JSON array body.
     */
    private APIGatewayProxyResponseEvent buildSuccessResponse(List<Dish> dishes) {
        JSONArray responseBody = new JSONArray();

        for (Dish dish : dishes) {
            JSONObject dishJson = new JSONObject()
                    .put("name", dish.getName())
                    .put("price", dish.getPrice())
                    .put("weight", dish.getWeight())
                    .put("imageUrl", dish.getImageUrl());
            responseBody.put(dishJson);
        }

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(responseBody.toString());
    }

    /**
     * Builds an error response with a specific status code and message.
     *
     * @param statusCode HTTP status code to return.
     * @param message    Error message to include in the response body.
     * @return API Gateway response with error message in JSON format.
     */
    private APIGatewayProxyResponseEvent buildErrorResponse(int statusCode, String message) {
        JSONObject errorJson = new JSONObject().put("message", message);

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody(errorJson.toString());
    }
}
