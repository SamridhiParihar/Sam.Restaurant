package com.restaurant.app.handlers.location;

import com.restaurant.app.di.DaggerAppComponent;

/**
 * Factory class for providing a singleton instance of {@link ShortLocationHandler}.
 * <p>
 * This class uses Dagger dependency injection to initialize the handler and
 * ensures it is only created once. It serves as a central access point for the
 * {@link ShortLocationHandler} instance within the Lambda deployment.
 */
public class ShortLocationHandlerFactory {

    // Singleton instance of ShortLocationHandler, initialized via Dagger
    private static final ShortLocationHandler instance = DaggerAppComponent
            .create()
            .buildShortLocationHandler();

    /**
     * Returns the singleton instance of {@link ShortLocationHandler}.
     * <p>
     * If the instance is somehow not initialized (which shouldn't happen in this setup),
     * it throws an exception to indicate a serious internal error.
     *
     * @return the initialized ShortLocationHandler instance
     * @throws IllegalStateException if the handler instance is null
     */
    public static ShortLocationHandler getHandler() {
        if (instance == null) {
            // This should never occur since instance is initialized statically.
            throw new IllegalStateException("ShortLocationHandler is not initialized");
        }
        return instance;
    }
}
