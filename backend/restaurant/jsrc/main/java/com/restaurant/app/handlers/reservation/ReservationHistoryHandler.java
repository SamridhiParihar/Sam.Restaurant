//package com.restaurant.app.handlers.reservation;
//
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
//import com.restaurant.app.models.reservation.ReservationHistoryResponse;
//import com.restaurant.app.services.auth.TokenVerifier;
//import com.restaurant.app.services.booking.ReservationsService;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import javax.inject.Inject;
//import java.util.List;
//import java.util.Map;
//
//public class ReservationHistoryHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
//    private ReservationsService reservationsService;
//    private TokenVerifier tokenVerifier;
//    @Inject
//    public ReservationHistoryHandler(ReservationsService reservationsService, TokenVerifier tokenVerifier) {
//        this.reservationsService = reservationsService;
//        this.tokenVerifier = tokenVerifier;
//    }
//
//    @Override
//    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
//        try{
//            Map<String, String> headers = request.getHeaders();
//            if (headers == null || !headers.containsKey("Authorization")) {
//                return unauthorizedResponse("Missing Authorization token");
//            }
//
//            String token = headers.get("Authorization").replace("Bearer ", "");
//
//            Map<String, String> userClaims = tokenVerifier.verifyTokenAndExtractClaims(token);
//            if (userClaims == null) {
//                return unauthorizedResponse("Invalid or expired token");
//            }
//           String email=userClaims.get("email") ;
//            Map<String,Object> response=reservationsService.getReservationHistory(email);
//            int statusCode=(Integer)response.get("statusCode");
//            if(statusCode==200){
//                return createSuccessResponse((List<ReservationHistoryResponse>)response.get("body"));
//            }
//            return createResponse(statusCode, (String) response.get("message"));
//        }
//        catch (Exception e){
//            return createResponse(500,"Error retrieving reservation history!");
//        }
//
//    }
//    private APIGatewayProxyResponseEvent unauthorizedResponse(String message) {
//        return new APIGatewayProxyResponseEvent().withStatusCode(401).withBody("{\"message\":\"" + message + "\"}");
//    }
//
//    public APIGatewayProxyResponseEvent createSuccessResponse(List<ReservationHistoryResponse> responses) {
//        JSONArray jsonArray = new JSONArray();
//
//        for (ReservationHistoryResponse reservationHistoryResponse : responses) {
//            JSONObject availableJson = new JSONObject();
//            availableJson.put("id", reservationHistoryResponse.getId());
//            availableJson.put("status",reservationHistoryResponse.getStatus());
//            availableJson.put("locationAddress",reservationHistoryResponse.getLocationAddress());
//            availableJson.put("date",reservationHistoryResponse.getDate());
//            availableJson.put("timeSlot",reservationHistoryResponse.getTimeSlot());
//            availableJson.put("preOrder",reservationHistoryResponse.getPreOrder().toString());
//            availableJson.put("guestNumber",reservationHistoryResponse.getGuestNumber());
//            availableJson.put("feedbackId", reservationHistoryResponse.getFeedbackId());
//
//
//            jsonArray.put(availableJson);
//        }
//
//        return new APIGatewayProxyResponseEvent()
//                .withStatusCode(200)
//                .withBody(jsonArray.toString());
//    }
//
//
//
//    private APIGatewayProxyResponseEvent createResponse(int statusCode, String message) {
//        return new APIGatewayProxyResponseEvent()
//                .withStatusCode(statusCode)
//                .withBody(new JSONObject().put("message", message).toString());
//    }
//}


package com.restaurant.app.handlers.reservation;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.restaurant.app.models.reservation.ReservationHistoryResponse;
import com.restaurant.app.services.auth.TokenVerifier;
import com.restaurant.app.services.booking.ReservationsService;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * AWS Lambda handler to retrieve the reservation history of an authenticated user.
 *
 * <p>This handler expects a valid Authorization token in the request headers.
 * It uses the {@link TokenVerifier} to validate the token and extract user claims,
 * and then fetches the reservation history using {@link ReservationsService}.
 *
 * <p>Returns a JSON array of reservation history records on success,
 * or an error response on failure (unauthorized, missing token, etc).
 */
public class ReservationHistoryHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ReservationsService reservationsService;
    private final TokenVerifier tokenVerifier;

    /**
     * Constructs the handler with dependencies injected.
     *
     * @param reservationsService Service to interact with reservation data.
     * @param tokenVerifier       Service to validate and extract claims from JWT tokens.
     */
    @Inject
    public ReservationHistoryHandler(ReservationsService reservationsService, TokenVerifier tokenVerifier) {
        this.reservationsService = reservationsService;
        this.tokenVerifier = tokenVerifier;
    }

    /**
     * Entry point for AWS Lambda to handle API Gateway requests.
     *
     * @param request Incoming request with headers and body.
     * @param context AWS Lambda context (unused here).
     * @return API Gateway-compatible HTTP response.
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            Map<String, String> headers = request.getHeaders();

            // Check if the Authorization header is present
            if (headers == null || !headers.containsKey("Authorization")) {
                return buildErrorResponse(401, "Missing Authorization token");
            }

            // Extract and clean the Bearer token
            String token = headers.get("Authorization").replace("Bearer ", "");

            // Validate token and extract user claims
            Map<String, String> userClaims = tokenVerifier.verifyTokenAndExtractClaims(token);
            if (userClaims == null) {
                return buildErrorResponse(401, "Invalid or expired token");
            }

            // Extract email from token and fetch reservation history
            String email = userClaims.get("email");
            Map<String, Object> serviceResponse = reservationsService.getReservationHistory(email);

            int statusCode = (Integer) serviceResponse.get("statusCode");

            // Success path: return history data as JSON array
            if (statusCode == 200) {
                @SuppressWarnings("unchecked")
                List<ReservationHistoryResponse> body = (List<ReservationHistoryResponse>) serviceResponse.get("body");
                return buildSuccessResponse(body);
            }

            // Error path: return message from service response
            String message = (String) serviceResponse.get("message");
            return buildErrorResponse(statusCode, message);

        } catch (Exception e) {
            // Catch any unexpected errors and return 500
            return buildErrorResponse(500, "Error retrieving reservation history!");
        }
    }

    /**
     * Builds a 200 OK response with a JSON array of reservation history objects.
     *
     * @param responses List of reservation history entries.
     * @return Formatted API Gateway response containing the reservation history.
     */
    private APIGatewayProxyResponseEvent buildSuccessResponse(List<ReservationHistoryResponse> responses) {
        JSONArray jsonArray = new JSONArray();

        for (ReservationHistoryResponse response : responses) {
            JSONObject json = new JSONObject();
            json.put("id", response.getId());
            json.put("status", response.getStatus());
            json.put("locationAddress", response.getLocationAddress());
            json.put("date", response.getDate());
            json.put("timeSlot", response.getTimeSlot());
            json.put("preOrder", response.getPreOrder().toString());
            json.put("guestNumber", response.getGuestNumber());
            json.put("feedbackId", response.getFeedbackId());

            jsonArray.put(json);
        }

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(jsonArray.toString());
    }

    /**
     * Builds an error response with a specific status code and message.
     *
     * @param statusCode HTTP status code (e.g., 401, 500).
     * @param message    Message to include in the response body.
     * @return Error response wrapped in {@link APIGatewayProxyResponseEvent}.
     */
    private APIGatewayProxyResponseEvent buildErrorResponse(int statusCode, String message) {
        JSONObject errorBody = new JSONObject().put("message", message);
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody(errorBody.toString());
    }
}
