package com.restaurant.app.handlers.dish;

import com.restaurant.app.di.DaggerAppComponent;

/**
 * Factory class responsible for providing a singleton instance of {@link DishHandler}.
 * <p>
 * This is used to initialize the handler using Dagger's dependency injection framework,
 * ensuring that all dependencies are properly wired before the Lambda function runs.
 */
public class DishHandlerFactory {

    /**
     * A singleton instance of DishHandler initialized using Dagger's AppComponent.
     * This ensures the handler is injected with all required dependencies (like DishService).
     */
    private static final DishHandler handlerInstance =
            DaggerAppComponent.create().buildDishHandler();

    /**
     * Returns the singleton instance of {@link DishHandler}.
     * <p>
     * This method ensures the handler is not null before returning. If it is null (which
     * shouldn't happen under normal operation), an exception is thrown.
     *
     * @return The initialized {@link DishHandler} instance.
     * @throws IllegalStateException if the handler was not initialized properly.
     */
    public static DishHandler getHandler() {
        if (handlerInstance == null) {
            throw new IllegalStateException("DishHandler is not initialized");
        }
        return handlerInstance;
    }
}
