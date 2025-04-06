package com.restaurant.app.handlers.reservation;

import com.restaurant.app.di.DaggerAppComponent;

/**
 * Factory class for providing a singleton instance of {@link DeleteReservationHandler}.
 * <p>
 * This class uses Dagger's dependency injection via {@code DaggerAppComponent} to
 * instantiate the handler with all required dependencies.
 * </p>
 *
 * Usage:
 * <pre>
 *     DeleteReservationHandler handler = DeleteReservationHandlerFactory.getHandler();
 * </pre>
 */
public class DeleteReservationHandlerFactory {

    /**
     * Singleton instance of DeleteReservationHandler.
     * Initialized eagerly using Dagger DI.
     */
    private static final DeleteReservationHandler handlerInstance =
            DaggerAppComponent.create().buildDeleteReservationHandler();

    /**
     * Returns the singleton instance of {@link DeleteReservationHandler}.
     *
     * @return a fully initialized DeleteReservationHandler
     * @throws IllegalStateException if the handler instance is null (should never happen in normal flow)
     */
    public static DeleteReservationHandler getHandler() {
        if (handlerInstance == null) {
            throw new IllegalStateException("ReservationsHandler is not initialized");
        }

        return handlerInstance;
    }
}
