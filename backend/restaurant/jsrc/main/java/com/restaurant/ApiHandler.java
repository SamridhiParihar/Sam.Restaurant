//package com.restaurant;
//
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//import com.restaurant.app.handlers.*;
//import com.restaurant.app.handlers.auth.SignInHandlerFactory;
//import com.restaurant.app.handlers.auth.SignUpHandlerFactory;
//import com.restaurant.app.handlers.booking.ClientHandlerFactory;
//import com.restaurant.app.handlers.booking.ReservationHandlerFactory;
//import com.restaurant.app.handlers.dish.DishHandlerFactory;
//import com.restaurant.app.handlers.dish.DishPopularHandlerFactory;
//import com.restaurant.app.handlers.location.LocationFeedbackHandlerFactory;
//import com.restaurant.app.handlers.location.LocationHandlerFactory;
//import com.restaurant.app.handlers.location.ShortLocationHandlerFactory;
//import com.restaurant.app.handlers.location.SpecialDishHandlerFactory;
//import com.restaurant.app.handlers.reservation.DeleteReservationHandlerFactory;
//import com.restaurant.app.handlers.reservation.ReservationHistoryHandler;
//import com.restaurant.app.handlers.reservation.ReservationHistoryHandlerFactory;
//import com.syndicate.deployment.annotations.lambda.LambdaHandler;
//import com.syndicate.deployment.model.RetentionSetting;
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
//import com.restaurant.app.models.RouteKey;
//import com.syndicate.deployment.annotations.environment.EnvironmentVariable;
//import com.syndicate.deployment.annotations.environment.EnvironmentVariables;
//import com.syndicate.deployment.annotations.resources.DependsOn;
//import com.syndicate.deployment.model.DeploymentRuntime;
//import com.syndicate.deployment.model.ResourceType;
//import java.util.Map;
//import static com.syndicate.deployment.model.environment.ValueTransformer.USER_POOL_NAME_TO_CLIENT_ID;
//import static com.syndicate.deployment.model.environment.ValueTransformer.USER_POOL_NAME_TO_USER_POOL_ID;
//import java.util.HashMap;
//
//
//@DependsOn(resourceType = ResourceType.COGNITO_USER_POOL, name = "${pool_name}")
//@LambdaHandler(lambdaName = "api-handler",
//		roleName = "api-handler-role",
//		runtime = DeploymentRuntime.JAVA17,
//		isPublishVersion = true,
//		aliasName = "${lambdas_alias_name}",
//		logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
//)
//
//@EnvironmentVariables(value = {
//		@EnvironmentVariable(key = "REGION", value = "${region}"),
//		@EnvironmentVariable(key = "COGNITO_ID", value = "${pool_name}", valueTransformer = USER_POOL_NAME_TO_USER_POOL_ID),
//		@EnvironmentVariable(key = "CLIENT_ID", value = "${pool_name}", valueTransformer = USER_POOL_NAME_TO_CLIENT_ID),
//		@EnvironmentVariable(key = "CLIENT_DB", value = "${client_db}"),
//		@EnvironmentVariable(key = "TABLE_NAME",value = "${table_name}"),
//		@EnvironmentVariable(key = "DISH_TABLE_NAME",value = "${dish_table_name}"),
//		@EnvironmentVariable(key = "LOCATION_TABLE_NAME",value ="${location_table_name}"),
//		@EnvironmentVariable(key = "FEEDBACK_TABLE",value = "${feedback_table_name}"),
//		@EnvironmentVariable(key="RES_TABLE_TABLE",value = "${res_table}"),
//		@EnvironmentVariable(key = "RESERVATION_TABLE",value="${reservation_table}")
//})
//
//public class ApiHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
//
//
//	private final Map<RouteKey, RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>> handlersByRouteKey;
//
//	private final Map<String, String> headersForCORS;
//
//	private final RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> routeNotImplementedHandler;
//
//	public ApiHandler() {
//		this.handlersByRouteKey = initHandlers();
//		this.headersForCORS = initHeadersForCORS();
//		this.routeNotImplementedHandler = new RouteNotImplementedHandler();
//	}
//
//	@Override
//
//	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {
//		return getHandler(requestEvent)
//				.handleRequest(requestEvent, context)
//				.withHeaders(headersForCORS);
//
//	}
//
//	private RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> getHandler(APIGatewayProxyRequestEvent requestEvent) {
//		return handlersByRouteKey.getOrDefault(getRouteKey(requestEvent), routeNotImplementedHandler);
//
//	}
//
//	private RouteKey getRouteKey(APIGatewayProxyRequestEvent requestEvent) {
//		String resourcePath = requestEvent.getResource();
//		return new RouteKey(requestEvent.getHttpMethod(), resourcePath);
//
//	}
//
//	private Map<RouteKey, RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>> initHandlers() {
//		return Map.ofEntries(
//				Map.entry(new RouteKey("POST", "/auth/sign-up"), SignUpHandlerFactory.getHandler()),
//				Map.entry(new RouteKey("POST", "/auth/sign-in"), SignInHandlerFactory.getHandler()),
//				Map.entry(new RouteKey("GET", "/dishes/{id}"), DishHandlerFactory.getHandler()),
//				Map.entry(new RouteKey("GET", "/locations"), LocationHandlerFactory.getHandler()),
//				Map.entry(new RouteKey("GET", "/locations/{id}/speciality-dishes"), SpecialDishHandlerFactory.getHandler()),
//				Map.entry(new RouteKey("GET", "/locations/select-options"), ShortLocationHandlerFactory.getHandler()),
//				Map.entry(new RouteKey("GET", "/locations/{id}/feedbacks"), LocationFeedbackHandlerFactory.getHandler()),
//				Map.entry(new RouteKey("GET", "/bookings/tables"), ReservationHandlerFactory.getHandler()),
//				Map.entry(new RouteKey("POST","/bookings/client"), ClientHandlerFactory.getHandler()),
//				Map.entry(new RouteKey("GET","/reservations"), ReservationHistoryHandlerFactory.getHandler()),
//				Map.entry(new RouteKey("DELETE","/reservations/{id}"), DeleteReservationHandlerFactory.getHandler()),
//				Map.entry(new RouteKey("GET","/dishes/popular"), DishPopularHandlerFactory.getHandler())
//		);
//	}
//
//	private Map<String, String> initHeadersForCORS() {
//		Map<String, String> headers = new HashMap<>();
//		headers.put("Access-Control-Allow-Origin", "*");
//		headers.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
//		headers.put("Access-Control-Allow-Methods", "*");
//		return headers;
//	}
//
//}
//


package com.restaurant;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.restaurant.app.handlers.RouteNotImplementedHandler;
import com.restaurant.app.handlers.auth.SignInHandlerFactory;
import com.restaurant.app.handlers.auth.SignUpHandlerFactory;
import com.restaurant.app.handlers.booking.ClientHandlerFactory;
import com.restaurant.app.handlers.booking.ReservationHandlerFactory;
import com.restaurant.app.handlers.dish.DishHandlerFactory;
import com.restaurant.app.handlers.dish.DishPopularHandlerFactory;
import com.restaurant.app.handlers.location.LocationFeedbackHandlerFactory;
import com.restaurant.app.handlers.location.LocationHandlerFactory;
import com.restaurant.app.handlers.location.ShortLocationHandlerFactory;
import com.restaurant.app.handlers.location.SpecialDishHandlerFactory;
import com.restaurant.app.handlers.reservation.DeleteReservationHandlerFactory;
import com.restaurant.app.handlers.reservation.ReservationHistoryHandlerFactory;
import com.restaurant.app.models.RouteKey;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.RetentionSetting;
import com.syndicate.deployment.annotations.resources.DependsOn;
import com.syndicate.deployment.model.DeploymentRuntime;
import com.syndicate.deployment.model.ResourceType;
import com.syndicate.deployment.annotations.environment.EnvironmentVariable;
import com.syndicate.deployment.annotations.environment.EnvironmentVariables;

import java.util.HashMap;
import java.util.Map;

import static com.syndicate.deployment.model.environment.ValueTransformer.USER_POOL_NAME_TO_CLIENT_ID;
import static com.syndicate.deployment.model.environment.ValueTransformer.USER_POOL_NAME_TO_USER_POOL_ID;

/**
 * Main Lambda entry point for API Gateway HTTP requests.
 * <p>
 * Routes incoming HTTP requests to specific handlers based on method and resource path.
 * Uses Syndicate framework annotations for deployment, dependency management, and environment configuration.
 */
@DependsOn(resourceType = ResourceType.COGNITO_USER_POOL, name = "${pool_name}")
@LambdaHandler(
		lambdaName = "api-handler",
		roleName = "api-handler-role",
		runtime = DeploymentRuntime.JAVA17,
		isPublishVersion = true,
		aliasName = "${lambdas_alias_name}",
		logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@EnvironmentVariables(value = {
		@EnvironmentVariable(key = "REGION", value = "${region}"),
		@EnvironmentVariable(key = "COGNITO_ID", value = "${pool_name}", valueTransformer = USER_POOL_NAME_TO_USER_POOL_ID),
		@EnvironmentVariable(key = "CLIENT_ID", value = "${pool_name}", valueTransformer = USER_POOL_NAME_TO_CLIENT_ID),
		@EnvironmentVariable(key = "CLIENT_DB", value = "${client_db}"),
		@EnvironmentVariable(key = "TABLE_NAME", value = "${table_name}"),
		@EnvironmentVariable(key = "DISH_TABLE_NAME", value = "${dish_table_name}"),
		@EnvironmentVariable(key = "LOCATION_TABLE_NAME", value = "${location_table_name}"),
		@EnvironmentVariable(key = "FEEDBACK_TABLE", value = "${feedback_table_name}"),
		@EnvironmentVariable(key = "RES_TABLE_TABLE", value = "${res_table}"),
		@EnvironmentVariable(key = "RESERVATION_TABLE", value = "${reservation_table}")
})
public class ApiHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	/**
	 * Map holding route key to handler mapping.
	 */
	private final Map<RouteKey, RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>> handlersByRouteKey;

	/**
	 * CORS headers to be added to every response.
	 */
	private final Map<String, String> headersForCORS;

	/**
	 * Default fallback handler if route is not implemented.
	 */
	private final RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> routeNotImplementedHandler;

	/**
	 * Constructor initializes route mappings and CORS headers.
	 */
	public ApiHandler() {
		this.handlersByRouteKey = initHandlers();
		this.headersForCORS = initHeadersForCORS();
		this.routeNotImplementedHandler = new RouteNotImplementedHandler();
	}

	/**
	 * Handles the incoming request from API Gateway and dispatches to appropriate route handler.
	 *
	 * @param requestEvent API Gateway request object
	 * @param context      Lambda context
	 * @return API Gateway response object
	 */
	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {
		return getHandler(requestEvent)
				.handleRequest(requestEvent, context)
				.withHeaders(headersForCORS);
	}

	/**
	 * Returns the appropriate handler for a given request.
	 *
	 * @param requestEvent API Gateway request
	 * @return Corresponding request handler or default fallback
	 */
	private RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> getHandler(APIGatewayProxyRequestEvent requestEvent) {
		return handlersByRouteKey.getOrDefault(getRouteKey(requestEvent), routeNotImplementedHandler);
	}

	/**
	 * Constructs a {@link RouteKey} from the request's method and path.
	 *
	 * @param requestEvent The incoming request
	 * @return RouteKey for handler lookup
	 */
	private RouteKey getRouteKey(APIGatewayProxyRequestEvent requestEvent) {
		String resourcePath = requestEvent.getResource();
		return new RouteKey(requestEvent.getHttpMethod(), resourcePath);
	}

	/**
	 * Initializes supported API routes and maps them to their handler factories.
	 *
	 * @return Map of RouteKey to handler
	 */
	private Map<RouteKey, RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>> initHandlers() {
		return Map.ofEntries(
				Map.entry(new RouteKey("POST", "/auth/sign-up"), SignUpHandlerFactory.getHandler()),
				Map.entry(new RouteKey("POST", "/auth/sign-in"), SignInHandlerFactory.getHandler()),
				Map.entry(new RouteKey("GET", "/dishes/{id}"), DishHandlerFactory.getHandler()),
				Map.entry(new RouteKey("GET", "/locations"), LocationHandlerFactory.getHandler()),
				Map.entry(new RouteKey("GET", "/locations/{id}/speciality-dishes"), SpecialDishHandlerFactory.getHandler()),
				Map.entry(new RouteKey("GET", "/locations/select-options"), ShortLocationHandlerFactory.getHandler()),
				Map.entry(new RouteKey("GET", "/locations/{id}/feedbacks"), LocationFeedbackHandlerFactory.getHandler()),
				Map.entry(new RouteKey("GET", "/bookings/tables"), ReservationHandlerFactory.getHandler()),
				Map.entry(new RouteKey("POST", "/bookings/client"), ClientHandlerFactory.getHandler()),
				Map.entry(new RouteKey("GET", "/reservations"), ReservationHistoryHandlerFactory.getHandler()),
				Map.entry(new RouteKey("DELETE", "/reservations/{id}"), DeleteReservationHandlerFactory.getHandler()),
				Map.entry(new RouteKey("GET", "/dishes/popular"), DishPopularHandlerFactory.getHandler())
		);
	}

	/**
	 * Sets standard CORS headers required for browser access.
	 *
	 * @return Map of CORS headers
	 */
	private Map<String, String> initHeadersForCORS() {
		Map<String, String> headers = new HashMap<>();
		headers.put("Access-Control-Allow-Origin", "*");
		headers.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
		headers.put("Access-Control-Allow-Methods", "*");
		return headers;
	}
}
