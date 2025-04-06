//package com.restaurant.app.services.dish;
//import com.restaurant.app.models.dish.Dish;
//import com.restaurant.app.services.DynamoDBService;
//
//import javax.inject.Inject;
//
//import java.util.List;
//import java.util.Map;
//
//// DishService.java
//public class DishService {
//    private final DynamoDBService dynamoDBService;
//
//    @Inject
//    public DishService(DynamoDBService dynamoDBService) {
//        this.dynamoDBService = dynamoDBService;
//    }
//
//    public Map<String, Object> getDishById(String id) {
//        try {
//            Dish dish = dynamoDBService.getDish(id);
//            if (dish == null) {
//                return Map.of(
//                        "statusCode", 404,
//                        "body", Map.of("message", "Dish not found with ID: " + id)
//                );
//            }
//
//            return Map.of(
//                    "statusCode", 200,
//                    "body", createDishResponse(dish) // Now correctly structured
//            );
//
//        }
//        catch (Exception e) {
//            return Map.of(
//                    "statusCode", 500,
//                    "message", "Error retrieving dish: " + e.getMessage()
//            );
//        }
//    }
//    private Map<String, Object> createDishResponse(Dish dish) {
//        // Remove nested statusCode here
//        return Map.ofEntries(
//                Map.entry("calories", dish.getCalories()),
//                Map.entry("carbohydrates", dish.getCarbohydrates()),
//                Map.entry("description", dish.getDescription()),
//                Map.entry("dishType", dish.getDishType()),
//                Map.entry("fats", dish.getFats()),
//                Map.entry("id", dish.getId()),
//                Map.entry("imageUrl", dish.getImageUrl()),
//                Map.entry("name", dish.getName()),
//                Map.entry("price", dish.getPrice()),
//                Map.entry("proteins", dish.getProteins()),
//                Map.entry("state", dish.getState()),
//                Map.entry("vitamins", dish.getVitamins()),
//                Map.entry("weight", dish.getWeight()) // Verify field name matches Dish class
//        );
//    }
//    public List<Dish> getPopularDishes(){
//        return dynamoDBService.getAllDishesSortedByPopularity();
//    }
//}



package com.restaurant.app.services.dish;

import com.restaurant.app.models.dish.Dish;
import com.restaurant.app.services.DynamoDBService;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Service layer that handles business logic for dish-related operations.
 * It communicates with the {@link DynamoDBService} to retrieve dish data.
 */
public class DishService {

    private final DynamoDBService dynamoDBService;

    /**
     * Constructor for {@code DishService}.
     *
     * @param dynamoDBService The service used to interact with DynamoDB.
     */
    @Inject
    public DishService(DynamoDBService dynamoDBService) {
        this.dynamoDBService = dynamoDBService;
    }

    /**
     * Retrieves a dish by its ID.
     *
     * @param id The ID of the dish to retrieve.
     * @return A map containing a status code and either:
     *         - a {@code body} map with dish details if found (status 200),
     *         - a message indicating not found (status 404),
     *         - or an error message if an exception occurred (status 500).
     */
    public Map<String, Object> getDishById(String id) {
        try {
            Dish dish = dynamoDBService.getDish(id);

            if (dish == null) {
                // Dish not found
                return Map.of(
                        "statusCode", 404,
                        "body", Map.of("message", "Dish not found with ID: " + id)
                );
            }

            // Dish found, return with 200 OK
            return Map.of(
                    "statusCode", 200,
                    "body", createDishResponse(dish)
            );

        } catch (Exception e) {
            // Exception occurred while fetching dish
            return Map.of(
                    "statusCode", 500,
                    "body", Map.of("message", "Error retrieving dish: " + e.getMessage())
            );
        }
    }

    /**
     * Retrieves a list of popular dishes sorted by popularity.
     *
     * @return A list of {@link Dish} objects sorted by popularity.
     */
    public List<Dish> getPopularDishes() {
        return dynamoDBService.getAllDishesSortedByPopularity();
    }

    /**
     * Helper method to convert a {@link Dish} object into a map representation.
     *
     * @param dish The dish to convert.
     * @return A map containing dish attributes.
     */
    private Map<String, Object> createDishResponse(Dish dish) {
        return Map.ofEntries(
                Map.entry("calories", dish.getCalories()),
                Map.entry("carbohydrates", dish.getCarbohydrates()),
                Map.entry("description", dish.getDescription()),
                Map.entry("dishType", dish.getDishType()),
                Map.entry("fats", dish.getFats()),
                Map.entry("id", dish.getId()),
                Map.entry("imageUrl", dish.getImageUrl()),
                Map.entry("name", dish.getName()),
                Map.entry("price", dish.getPrice()),
                Map.entry("proteins", dish.getProteins()),
                Map.entry("state", dish.getState()),
                Map.entry("vitamins", dish.getVitamins()),
                Map.entry("weight", dish.getWeight())
        );
    }
}
