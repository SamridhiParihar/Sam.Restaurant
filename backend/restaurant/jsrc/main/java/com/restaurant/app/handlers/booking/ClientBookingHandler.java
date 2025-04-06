package com.restaurant.app.handlers.booking;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.restaurant.app.models.bookings.BookingServiceRequest;
import com.restaurant.app.models.auth.User;
import com.restaurant.app.models.bookings.BookingServiceResponse;
import com.restaurant.app.services.auth.TokenVerifier;
import com.restaurant.app.services.booking.ClientBookingService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * AWS Lambda handler for processing client booking requests.
 * <p>
 * Responsibilities:
 * - Authenticates user via JWT token from the Authorization header
 * - Parses the booking request from the body
 * - Delegates to {@link ClientBookingService} to process and save booking
 * - Returns appropriate HTTP responses
 * </p>
 */
public class ClientBookingHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final TokenVerifier tokenVerifier;
    private final ClientBookingService clientBookingService;

    /**
     * Constructs a handler with dependencies injected.
     *
     * @param verifier             TokenVerifier used for decoding JWT and extracting claims.
     * @param clientBookingService Service layer responsible for processing booking requests.
     */
    @Inject
    public ClientBookingHandler(TokenVerifier verifier, ClientBookingService clientBookingService) {
        this.tokenVerifier = verifier;
        this.clientBookingService = clientBookingService;
    }

    /**
     * Handles the incoming API Gateway event and returns a response.
     *
     * @param request Incoming API Gateway request.
     * @param context Lambda context (unused).
     * @return API Gateway response event.
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            // Extract and verify bearer token from request headers
            String token = extractAuthToken(request.getHeaders());
            if (token == null) {
                return unauthorizedResponse("Missing Authorization token");
            }

            // Decode token and extract user claims
            Map<String, String> userClaims = tokenVerifier.verifyTokenAndExtractClaims(token);
            if (userClaims == null) {
                return unauthorizedResponse("Invalid or expired token");
            }

            // Construct User model from token claims
            User user = extractUserFromClaims(userClaims);

            // Deserialize client booking request
            BookingServiceRequest clientBookingRequest = BookingServiceRequest.fromJson(request.getBody());

            // Delegate to booking service
            Map<String, Object> response = clientBookingService.saveResponseForBooking(clientBookingRequest, user);
            int statusCode = (Integer) response.get("statusCode");

            // Prepare appropriate response based on status code
            if (statusCode == 200) {
                List<BookingServiceResponse> bookingServiceResponses =
                        (List<BookingServiceResponse>) response.get("body");
                return createSuccessResponse(bookingServiceResponses);
            } else {
                return createResponse(statusCode, (String) response.get("message"));
            }
        } catch (RuntimeException e) {
            return createResponse(500, "Internal Server Error: " + e.getMessage());
        }
    }

    /**
     * Extracts and cleans the bearer token from HTTP headers.
     *
     * @param headers HTTP request headers.
     * @return The raw JWT token, or null if missing.
     */
    private String extractAuthToken(Map<String, String> headers) {
        if (headers == null || !headers.containsKey("Authorization")) {
            return null;
        }
        return headers.get("Authorization").replace("Bearer ", "").trim();
    }

    /**
     * Constructs a User model using claims extracted from the token.
     *
     * @param claims Map of claims from the decoded JWT token.
     * @return Constructed User instance.
     */
    private User extractUserFromClaims(Map<String, String> claims) {
        User user = new User();
        user.setEmail(claims.get("email"));
        user.setFirstName(claims.getOrDefault("first_name", ""));
        user.setLastName(claims.getOrDefault("last_name", ""));
        user.setCognitoSub(claims.get("sub"));
        user.setRole(claims.getOrDefault("role", "USER"));
        return user;
    }

    /**
     * Constructs a 401 Unauthorized response.
     *
     * @param message Reason for unauthorized access.
     * @return API Gateway response event with status 401.
     */
    private APIGatewayProxyResponseEvent unauthorizedResponse(String message) {
        return createResponse(401, message);
    }

    /**
     * Constructs a 200 OK response with booking results.
     *
     * @param responses List of booking service responses to include in the response body.
     * @return API Gateway response event with booking data as JSON.
     */
    private APIGatewayProxyResponseEvent createSuccessResponse(List<BookingServiceResponse> responses) {
        JSONArray jsonArray = new JSONArray();

        for (BookingServiceResponse res : responses) {
            JSONObject json = new JSONObject();
            json.put("id", res.getId());
            json.put("status", res.getStatus());
            json.put("locationAddress", res.getLocationAddress());
            json.put("date", res.getDate());
            json.put("timeSlot", res.getTimeSlot());
            json.put("preOrder", res.getPreOrder());
            json.put("guestNumber", res.getGuestNumber());
            json.put("feedbackId", res.getFeedbackId());
            jsonArray.put(json);
        }

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(jsonArray.toString());
    }

    /**
     * Constructs a generic JSON response with given status code and message.
     *
     * @param statusCode HTTP status code.
     * @param message    Response message to include.
     * @return API Gateway response event with JSON message.
     */
    private APIGatewayProxyResponseEvent createResponse(int statusCode, String message) {
        JSONObject body = new JSONObject().put("message", message);
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody(body.toString());
    }
}
