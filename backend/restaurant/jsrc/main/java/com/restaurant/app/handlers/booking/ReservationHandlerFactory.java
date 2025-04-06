package com.restaurant.app.handlers.booking;

import com.restaurant.app.di.DaggerAppComponent;

/**
 * Factory class for creating and providing a singleton instance of {@link ReservationsHandler}.
 * <p>
 * This factory leverages Dagger's dependency injection to build the handler instance
 * using the {@code DaggerAppComponent}. The handler is created once and reused to
 * improve performance during AWS Lambda invocations.
 * </p>
 *
 * <pre>
 * Usage:
 * ReservationsHandler handler = ReservationHandlerFactory.getHandler();
 * </pre>
 */
public class ReservationHandlerFactory {

    /**
     * Singleton instance of {@link ReservationsHandler}, built using Dagger dependency injection.
     */
    private static final ReservationsHandler handlerInstance =
            DaggerAppComponent.create().buildReservationsHandler();

    /**
     * Returns the singleton instance of {@link ReservationsHandler}.
     * <p>
     * If the instance is not initialized for some reason (unlikely due to static init),
     * this method throws an {@link IllegalStateException}.
     * </p>
     *
     * @return ReservationsHandler singleton instance
     * @throws IllegalStateException if the handler instance is not initialized
     */
    public static ReservationsHandler getHandler() {
        if (handlerInstance == null) {
            throw new IllegalStateException("ReservationsHandler is not initialized");
        }
        return handlerInstance;
    }
}
