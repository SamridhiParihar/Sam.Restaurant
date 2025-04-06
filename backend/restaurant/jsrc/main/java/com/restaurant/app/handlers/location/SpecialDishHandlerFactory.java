package com.restaurant.app.handlers.location;

import com.restaurant.app.di.DaggerAppComponent;

/**
 * Factory class for creating and providing a singleton instance of {@link SpecialDishHandler}.
 *
 * <p>
 * This class uses Dagger's dependency injection framework to instantiate the handler
 * with all required dependencies. The instance is created eagerly and reused across Lambda invocations.
 * </p>
 *
 * <p>
 * Note: Although the singleton is initialized eagerly, a null check is added as a safeguard,
 * though it will never be null unless modified manually.
 * </p>
 */
public class SpecialDishHandlerFactory {

    // Singleton instance of SpecialDishHandler, created via Dagger dependency graph.
    private static final SpecialDishHandler instance =
            DaggerAppComponent.create().buildSpecialDishHandler();

    /**
     * Returns the singleton instance of {@link SpecialDishHandler}.
     *
     * @return the initialized handler instance.
     * @throws IllegalStateException if the handler is somehow uninitialized (should not happen).
     */
    public static SpecialDishHandler getHandler() {
        if (instance == null) {
            // Defensive programming: this block should never be reached
            throw new IllegalStateException("SpecialDishHandler is not initialized");
        }
        return instance;
    }
}
