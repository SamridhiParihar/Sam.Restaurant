package com.restaurant.app.handlers.location;

import com.restaurant.app.di.DaggerAppComponent;

/**
 * Factory class responsible for providing a singleton instance of {@link LocationHandler}.
 * <p>
 * This factory uses Dagger's dependency injection to build and provide the {@link LocationHandler}
 * instance used in AWS Lambda runtime environments.
 */
public class LocationHandlerFactory {

    /**
     * Singleton instance of {@link LocationHandler}, initialized via Dagger dependency injection.
     */
    private static final LocationHandler handlerInstance = DaggerAppComponent.create().buildLocationHandler();

    /**
     * Returns the singleton instance of {@link LocationHandler}.
     * <p>
     * Throws an exception if the handler instance has not been initialized properly.
     *
     * @return the initialized {@link LocationHandler} instance.
     * @throws IllegalStateException if the handler is null (unexpected).
     */
    public static LocationHandler getHandler() {
        if (handlerInstance == null) {
            throw new IllegalStateException("LocationHandler is not initialized");
        }
        return handlerInstance;
    }
}
