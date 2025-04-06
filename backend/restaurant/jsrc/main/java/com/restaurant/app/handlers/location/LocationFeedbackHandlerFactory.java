package com.restaurant.app.handlers.location;

import com.restaurant.app.di.DaggerAppComponent;

/**
 * Factory class for creating and providing a singleton instance of {@link LocationFeedbackHandler}.
 * <p>
 * This class uses Dagger's dependency injection to construct the handler with all necessary dependencies.
 * It ensures that only a single instance is created and reused for performance and resource efficiency.
 */
public class LocationFeedbackHandlerFactory {

    // Singleton instance of LocationFeedbackHandler, created via Dagger DI
    private static LocationFeedbackHandler instance =
            DaggerAppComponent.create().buildLocationFeedbackHandler();

    /**
     * Returns the singleton instance of {@link LocationFeedbackHandler}.
     * <p>
     * If the handler was not properly initialized (should never happen with current static init),
     * it throws an {@link IllegalArgumentException}.
     *
     * @return Initialized LocationFeedbackHandler instance.
     * @throws IllegalArgumentException if the handler is not initialized.
     */
    public static LocationFeedbackHandler getHandler() {
        if (instance == null) {
            throw new IllegalArgumentException("LocationFeedbackHandler is not initialized");
        }
        return instance;
    }
}
