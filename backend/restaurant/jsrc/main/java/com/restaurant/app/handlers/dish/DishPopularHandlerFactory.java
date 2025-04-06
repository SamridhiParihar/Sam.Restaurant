package com.restaurant.app.handlers.dish;

import com.restaurant.app.di.DaggerAppComponent;

/**
 * Factory class to provide a singleton instance of {@link DishPopularHandler}.
 * <p>
 * This is used to instantiate the handler with its dependencies via Dagger's dependency injection.
 * Ensures the handler is properly constructed and reused during AWS Lambda invocations.
 */
public class DishPopularHandlerFactory {

    /**
     * Singleton instance of {@link DishPopularHandler}, initialized using Dagger's AppComponent.
     */
    private static final DishPopularHandler handlerInstance =
            DaggerAppComponent.create().buildDishPopularHandler();

    /**
     * Returns the singleton instance of {@link DishPopularHandler}.
     *
     * @return A fully-initialized handler instance.
     * @throws IllegalStateException if the handler is not initialized (should not occur unless Dagger fails).
     */
    public static DishPopularHandler getHandler() {
        if (handlerInstance == null) {
            throw new IllegalStateException("DishPopularHandler is not initialized");
        }
        return handlerInstance;
    }
}
