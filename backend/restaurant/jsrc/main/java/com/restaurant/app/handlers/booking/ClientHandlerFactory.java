package com.restaurant.app.handlers.booking;

import com.restaurant.app.di.DaggerAppComponent;

/**
 * Factory class for providing a singleton instance of {@link ClientBookingHandler}.
 * <p>
 * This class uses Dagger's dependency injection to construct and provide
 * a fully-initialized {@code ClientBookingHandler} instance with all required
 * services and dependencies injected.
 * </p>
 *
 * <p>Usage:</p>
 * <pre>
 *     ClientBookingHandler handler = ClientHandlerFactory.getHandler();
 * </pre>
 *
 * <p>Ensures thread-safe lazy initialization and prevents repeated object construction.</p>
 */
public class ClientHandlerFactory {

    /**
     * Static, singleton instance of the ClientBookingHandler.
     * This is initialized using Dagger's AppComponent at class load time.
     */
    private static final ClientBookingHandler handlerInstance =
            DaggerAppComponent.create().buildClientBookingHandler();

    /**
     * Returns the singleton {@link ClientBookingHandler} instance.
     *
     * @return ClientBookingHandler instance with injected dependencies.
     * @throws IllegalStateException if the handler instance is unexpectedly null.
     */
    public static ClientBookingHandler getHandler() {
        if (handlerInstance == null) {
            throw new IllegalStateException("ClientBookingHandler is not initialized");
        }
        return handlerInstance;
    }
}
