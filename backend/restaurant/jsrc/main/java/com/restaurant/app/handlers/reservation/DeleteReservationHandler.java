//package com.restaurant.app.handlers.reservation;
//
//
//
//import com.amazonaws.services.lambda.runtime.Context;
//
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
//
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
//import com.restaurant.app.services.auth.TokenVerifier;
//import com.restaurant.app.services.booking.ReservationsService;
//import org.json.JSONObject;
//
//import java.util.Map;
//import java.util.Objects;
//
//import javax.inject.Inject;
//
//public class DeleteReservationHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
//
//    private ReservationsService reservationsService;
//
//    private TokenVerifier tokenVerifier;
//
//    @Inject
//
//    public DeleteReservationHandler(ReservationsService reservationsService, TokenVerifier verifier) {
//
//        this.reservationsService = reservationsService;
//
//        this.tokenVerifier = verifier;
//
//    }
//
//    @Override
//
//    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
//
//        try {
//
//            Map<String, String> pathParams = request.getPathParameters();
//
//            String id = pathParams != null ? pathParams.get("id") : null;
//
//            if (id == null || id.isEmpty()) {
//
//                return new APIGatewayProxyResponseEvent()
//
//                        .withStatusCode(400)
//
//                        .withBody("Missing reservation id in path parameter");
//
//            }
//
//            Map<String, String> headers = request.getHeaders();
//
//            if (headers == null || !headers.containsKey("Authorization")) {
//
//                return unauthorizedResponse("Missing Authorization token");
//
//            }
//
//            String token = headers.get("Authorization").replace("Bearer ", "");
//
//            Map<String, String> userClaims = tokenVerifier.verifyTokenAndExtractClaims(token);
//
//            if (userClaims == null) {
//
//                return unauthorizedResponse("Invalid or expired token");
//
//            }
//
//            String email = userClaims.get("email");
//            Map<String, Object> response = reservationsService.deleteReservationById(id, email);
//
//            int statusCode =(Integer) response.get("statusCode");
//            String message = (String) response.get("message");
//
//           return new APIGatewayProxyResponseEvent()
//                   .withStatusCode(statusCode)
//                   .withBody(new JSONObject().put("message",message).toString());
//
//
//        } catch (Exception e) {
//
//            return new APIGatewayProxyResponseEvent()
//
//                    .withStatusCode(500)
//
//                    .withBody("Internal Server Error!");
//
//        }
//
//    }
//
//    private APIGatewayProxyResponseEvent unauthorizedResponse(String message) {
//
//        return new APIGatewayProxyResponseEvent().withStatusCode(401)
//                .withBody(new JSONObject().put("message",message).toString());
//
//    }
//
//}
//
//


package com.restaurant.app.handlers.reservation;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.restaurant.app.services.auth.TokenVerifier;
import com.restaurant.app.services.booking.ReservationsService;

import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Map;

/**
 * AWS Lambda handler for deleting a reservation by its ID.
 * <p>
 * This handler verifies the request's authorization token, extracts the user email,
 * and deletes the reservation if the user has proper access.
 * </p>
 *
 * Expected input:
 * - Path parameter: reservation ID
 * - Header: Authorization (Bearer token)
 *
 * Expected output:
 * - 200 OK: if reservation deleted successfully
 * - 400 Bad Request: if missing reservation ID
 * - 401 Unauthorized: if token missing or invalid
 * - 500 Internal Server Error: for unhandled exceptions
 */
public class DeleteReservationHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private ReservationsService reservationsService;
    private TokenVerifier tokenVerifier;

    /**
     * Constructor with dependency injection for service classes.
     *
     * @param reservationsService service to manage reservation-related operations
     * @param verifier            service to verify and decode JWT tokens
     */
    @Inject
    public DeleteReservationHandler(ReservationsService reservationsService, TokenVerifier verifier) {
        this.reservationsService = reservationsService;
        this.tokenVerifier = verifier;
    }

    /**
     * Lambda function handler to delete a reservation.
     *
     * @param request incoming API Gateway proxy request event
     * @param context Lambda context object
     * @return APIGatewayProxyResponseEvent containing status code and message
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            Map<String, String> pathParams = request.getPathParameters();
            String id = pathParams != null ? pathParams.get("id") : null;

            if (id == null || id.isEmpty()) {
                // Missing reservation ID in the request path
                return buildErrorResponse(400, "Missing reservation id in path parameter");
            }

            Map<String, String> headers = request.getHeaders();
            if (headers == null || !headers.containsKey("Authorization")) {
                // Missing Authorization header
                return buildErrorResponse(401, "Missing Authorization token");
            }

            // Remove "Bearer " prefix from token and extract claims
            String token = headers.get("Authorization").replace("Bearer ", "");
            Map<String, String> userClaims = tokenVerifier.verifyTokenAndExtractClaims(token);

            if (userClaims == null) {
                // Token is invalid or expired
                return buildErrorResponse(401, "Invalid or expired token");
            }

            // Extract email from token and attempt to delete reservation
            String email = userClaims.get("email");
            Map<String, Object> response = reservationsService.deleteReservationById(id, email);

            int statusCode = (Integer) response.get("statusCode");
            String message = (String) response.get("message");

            return buildSuccessResponse(statusCode, message);

        } catch (Exception e) {
            // Internal server error
            return buildErrorResponse(500, "Internal Server Error!");
        }
    }

    /**
     * Builds a successful response containing the message and status code.
     *
     * @param statusCode HTTP status code (e.g., 200)
     * @param message    response body message
     * @return formatted APIGatewayProxyResponseEvent
     */
    private APIGatewayProxyResponseEvent buildSuccessResponse(int statusCode, String message) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody(new JSONObject().put("message", message).toString());
    }

    /**
     * Builds an error response containing the message and status code.
     *
     * @param statusCode HTTP status code (e.g., 400, 401, 500)
     * @param message    error message to return in response
     * @return formatted APIGatewayProxyResponseEvent
     */
    private APIGatewayProxyResponseEvent buildErrorResponse(int statusCode, String message) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody(new JSONObject().put("message", message).toString());
    }
}
