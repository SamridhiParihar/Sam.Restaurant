package com.restaurant.app.handlers.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.restaurant.app.models.auth.SignUpRequest;
import com.restaurant.app.services.auth.UserService;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Map;

/**
 * AWS Lambda handler for processing user sign-up requests.
 *
 * <p>This handler validates incoming sign-up requests, interacts with {@link UserService}
 * to register users, and returns the appropriate API Gateway response.</p>
 */
public class SignUpHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final UserService userService;

    private static final int BAD_REQUEST = 400;
    private static final int INTERNAL_ERROR = 500;

    /**
     * Constructs a new SignUpHandler with a {@link UserService} dependency.
     *
     * @param userService The service responsible for handling user registration.
     */
    @Inject
    public SignUpHandler(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles an incoming API Gateway request to register a new user.
     *
     * <p>It extracts the request body, processes the registration, and returns an appropriate
     * response with a message and an optional access token upon successful sign-up.</p>
     *
     * @param request The incoming API Gateway request event.
     * @param context The Lambda execution context.
     * @return The API Gateway response event with registration results.
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            String body = request.getBody();
            JSONObject inputJson = new JSONObject(body);

            String[] requiredFields = {"email", "firstName", "lastName", "password"};
            if (!hasRequiredFields(inputJson, requiredFields)) {
                return buildErrorResponse(BAD_REQUEST, "Missing required fields: email, firstName, lastName, or password");
            }

            SignUpRequest signUpRequest = SignUpRequest.fromJson(body);
            Map<String, Object> response = userService.registerUser(signUpRequest);

            return buildSuccessResponse(response);
        } catch (IllegalArgumentException e) {
            return buildErrorResponse(BAD_REQUEST, "Invalid request: " + e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse(INTERNAL_ERROR, "Internal Server Error: " + e.getMessage());
        }
    }

    /**
     * Checks whether all required fields are present and non-blank in the input JSON.
     *
     * @param inputJson The JSON object representing the request body.
     * @param requiredFields Array of required field names.
     * @return true if all required fields are present and non-blank, false otherwise.
     */
    private boolean hasRequiredFields(JSONObject inputJson, String[] requiredFields) {
        for (String field : requiredFields) {
            if (!inputJson.has(field) || inputJson.getString(field).isBlank()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Builds a success response for API Gateway based on the user registration result.
     *
     * @param response The response map from {@link UserService}.
     * @return A properly formatted API Gateway response event.
     */
    private APIGatewayProxyResponseEvent buildSuccessResponse(Map<String, Object> response) {
        int statusCode = (Integer) response.get("statusCode");
        Map<String, Object> body = (Map<String, Object>) response.get("body");

        JSONObject responseBody = new JSONObject();

        if (body.containsKey("error")) {
            responseBody.put("message", body.get("error"));
        } else if (body.containsKey("message")) {
            responseBody.put("message", body.get("message"));
        }

        if (body.containsKey("accessToken")) {
            responseBody.put("accessToken", body.get("accessToken"));
        }

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody(responseBody.toString());
    }

    /**
     * Builds an error response for API Gateway.
     *
     * @param statusCode The HTTP status code for the error.
     * @param message The error message to be returned.
     * @return A formatted API Gateway response event with the error message.
     */
    private APIGatewayProxyResponseEvent buildErrorResponse(int statusCode, String message) {
        JSONObject responseBody = new JSONObject().put("message", message);
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody(responseBody.toString());
    }
}
