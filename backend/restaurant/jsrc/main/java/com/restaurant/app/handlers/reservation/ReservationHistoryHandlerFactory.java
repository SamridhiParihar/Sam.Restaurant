package com.restaurant.app.handlers.reservation;

import com.restaurant.app.di.DaggerAppComponent;

/**
 * Factory class responsible for providing a singleton instance of {@link ReservationHistoryHandler}.
 *
 * <p>This factory leverages Dagger's dependency injection to build and initialize the handler instance.
 * The handler is created once and reused across Lambda invocations, ensuring efficient use of resources.
 */
public class ReservationHistoryHandlerFactory {

    // Singleton instance of ReservationHistoryHandler initialized via Dagger
    private static final ReservationHistoryHandler handlerInstance =
            DaggerAppComponent.create().buildReservationHistoryHandler();

    /**
     * Returns the singleton instance of {@link ReservationHistoryHandler}.
     *
     * @return Initialized handler instance.
     * @throws IllegalStateException if the handler instance is not initialized properly.
     */
    public static ReservationHistoryHandler getHandler() {
        if (handlerInstance == null) {
            throw new IllegalStateException("ReservationHistoryHandler is not initialized");
        }

        return handlerInstance;
    }
}
