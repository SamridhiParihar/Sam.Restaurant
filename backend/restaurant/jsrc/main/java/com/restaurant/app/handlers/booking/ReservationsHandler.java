package com.restaurant.app.handlers.booking;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.restaurant.app.Utils.TimeSlotUtil;
import com.restaurant.app.models.bookings.AvailableTableResponse;
import com.restaurant.app.services.booking.ReservationsService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * AWS Lambda handler that processes reservation availability requests.
 * <p>
 * This handler accepts query parameters such as `locationId`, `date`, and `guests`,
 * invokes the {@link ReservationsService} to fetch available tables,
 * and returns the list of available tables along with their time slots.
 * </p>
 *
 * Expected query parameters:
 * <ul>
 *     <li><b>locationId</b> - ID of the restaurant location</li>
 *     <li><b>date</b> - Requested reservation date (format: yyyy-MM-dd)</li>
 *     <li><b>guests</b> - Number of guests for the reservation</li>
 * </ul>
 *
 * Optional query parameter:
 * <ul>
 *     <li><b>time</b> - Requested time (currently unused but passed through)</li>
 * </ul>
 */
public class ReservationsHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ReservationsService reservationsService;

    /**
     * Constructs a ReservationsHandler with the provided ReservationsService instance.
     *
     * @param reservationsService the service responsible for reservation business logic
     */
    @Inject
    public ReservationsHandler(ReservationsService reservationsService) {
        this.reservationsService = reservationsService;
    }

    /**
     * Handles the incoming API Gateway request to fetch available tables.
     *
     * @param request the incoming API Gateway request containing query parameters
     * @param context the Lambda execution context
     * @return an API Gateway response containing available table data or an error message
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            Map<String, String> queryParams = request.getQueryStringParameters() != null
                    ? request.getQueryStringParameters()
                    : Collections.emptyMap();

            String locationId = queryParams.get("locationId");
            String date = queryParams.get("date");
            String time = queryParams.get("time"); // optional and unused
            String capacityStr = queryParams.get("guests");

            // Validate required parameters
            if (locationId == null || date == null || capacityStr == null) {
                return createResponse(400, "Missing required query parameters: locationId, date, or guests");
            }

            int capacity;
            try {
                capacity = Integer.parseInt(capacityStr);
            } catch (NumberFormatException e) {
                return createResponse(400, "Invalid number format for 'guests'");
            }

            // Call service to get available tables
            Map<String, Object> response = reservationsService.getAvailableTables(locationId, date, capacity);
            int statusCode = (Integer) response.get("statusCode");

            if (statusCode == 200) {
                List<AvailableTableResponse> availableTableResponses = (List<AvailableTableResponse>) response.get("body");
                return createSuccessResponse(availableTableResponses);
            } else {
                return createResponse(statusCode, (String) response.get("message"));
            }

        } catch (RuntimeException e) {
            return createResponse(500, "Internal Server Error: " + e.getMessage());
        }
    }

    /**
     * Constructs a 200 OK JSON response with the list of available tables and their time slots.
     *
     * @param responses list of {@link AvailableTableResponse} objects returned by the service
     * @return formatted JSON response with HTTP 200 status
     */
    private APIGatewayProxyResponseEvent createSuccessResponse(List<AvailableTableResponse> responses) {
        JSONArray jsonArray = new JSONArray();

        for (AvailableTableResponse availableTableResponse : responses) {
            JSONObject json = new JSONObject();
            json.put("locationId", availableTableResponse.getLocationId());
            json.put("locationAddress", availableTableResponse.getLocationAddress());
            json.put("tableNumber", availableTableResponse.getTableNumber());
            json.put("capacity", availableTableResponse.getCapacity());

            List<String> slots = TimeSlotUtil.getSlotTimes(availableTableResponse.getAvailableSlots());
            json.put("availableSlots", slots);

            jsonArray.put(json);
        }

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(jsonArray.toString());
    }

    /**
     * Constructs a generic JSON response with the given status code and message.
     *
     * @param statusCode the HTTP status code to return
     * @param message    the error or informational message to include in the response
     * @return API Gateway response with the specified status code and message
     */
    private APIGatewayProxyResponseEvent createResponse(int statusCode, String message) {
        JSONObject body = new JSONObject().put("message", message);
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody(body.toString());
    }
}
