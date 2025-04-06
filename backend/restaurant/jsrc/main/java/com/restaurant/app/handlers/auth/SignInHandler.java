package com.restaurant.app.handlers.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.restaurant.app.models.auth.SignInRequest;
import com.restaurant.app.services.auth.UserService;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Map;

/**
 * AWS Lambda handler for processing user sign-in requests.
 *
 * <p>This handler validates incoming sign-in requests, interacts with {@link UserService}
 * to authenticate users, and returns the appropriate API Gateway response.</p>
 */
public class SignInHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final UserService userService;

    private static final int BAD_REQUEST = 400;
    private static final int INTERNAL_ERROR = 500;

    /**
     * Constructs a new SignInHandler with a {@link UserService} dependency.
     *
     * @param userService The service responsible for handling authentication logic.
     */
    @Inject
    public SignInHandler(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles an incoming API Gateway request to sign in a user.
     *
     * <p>It extracts the request body, processes authentication, and returns an appropriate
     * response with an access token, role, and username if successful.</p>
     *
     * @param request The incoming API Gateway request event.
     * @param context The Lambda execution context.
     * @return The API Gateway response event with authentication results.
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            String body = request.getBody();
            JSONObject inputJson = new JSONObject(body);

            String[] requiredFields = {"email", "password"};
            if (!hasRequiredFields(inputJson, requiredFields)) {
                return buildErrorResponse(BAD_REQUEST, "Missing required fields: email or password");
            }

            SignInRequest signInRequest = SignInRequest.fromJson(body);
            Map<String, Object> response = userService.signInUser(signInRequest);

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
     * Builds a successful API Gateway response containing authentication details.
     *
     * @param response The response map from {@link UserService}.
     * @return A properly formatted API Gateway response event.
     */
    private APIGatewayProxyResponseEvent buildSuccessResponse(Map<String, Object> response) {
        int statusCode = (Integer) response.get("statusCode");
        Map<String, Object> body = (Map<String, Object>) response.get("body");

        JSONObject responseBody = new JSONObject();

        if (body.containsKey("accessToken")) {
            responseBody.put("accessToken", body.get("accessToken"));
        }

        if (body.containsKey("role")) {
            responseBody.put("role", body.get("role"));
        }

        if (body.containsKey("username")) {
            responseBody.put("username", body.get("username"));
        }

        if (body.containsKey("message")) {
            responseBody.put("message", body.get("message"));
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
        JSONObject errorBody = new JSONObject().put("message", message);
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody(errorBody.toString());
    }
}
