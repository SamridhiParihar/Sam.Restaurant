package com.restaurant.app.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.json.JSONObject;

/**
 * Fallback Lambda handler that is invoked when no specific implementation
 * exists for a given HTTP method and path.
 *
 * <p>This is typically used as a default route handler in the API Gateway
 * integration to gracefully inform clients that the route is not yet implemented.
 */
public class RouteNotImplementedHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    /**
     * Handles the incoming API Gateway request and responds with HTTP 501 Not Implemented.
     *
     * @param requestEvent The incoming API Gateway proxy request event containing method and path info.
     * @param context The Lambda execution context.
     * @return A response indicating that the route/method combination is not implemented.
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {

        // Construct a 501 Not Implemented response with a descriptive error message.
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(501)
                .withBody(
                        new JSONObject().put(
                                "message",
                                "Handler for the %s method on the %s path is not implemented."
                                        .formatted(requestEvent.getHttpMethod(), requestEvent.getPath())
                        ).toString()
                );
    }
}
