//package com.restaurant.app.handlers.auth;
//
//import com.restaurant.app.di.DaggerAppComponent;
//
//public class SignInHandlerFactory {
//    private static final SignInHandler handlerInstance = DaggerAppComponent.create().buildSignInHandler();
//
//    public static SignInHandler getHandler() {
//        if (handlerInstance == null) {
//            throw new IllegalStateException("SingInHandler is not initialized");
//        }
//        return handlerInstance;
//    }
//}

package com.restaurant.app.handlers.auth;

import com.restaurant.app.di.DaggerAppComponent;

/**
 * Factory class to provide a singleton instance of {@link SignInHandler}.
 *
 * <p>This ensures that a single instance of {@link SignInHandler} is created
 * using Dagger dependency injection and can be reused across multiple requests.</p>
 *
 * <p><b>Usage:</b></p>
 * <pre>
 *     SignInHandler handler = SignInHandlerFactory.getHandler();
 * </pre>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *     <li>Creates a single instance of {@link SignInHandler} using Dagger.</li>
 *     <li>Provides access to the instance via the {@code getHandler()} method.</li>
 *     <li>Ensures that an exception is thrown if the instance is not properly initialized.</li>
 * </ul>
 */
public class SignInHandlerFactory {

    /** Singleton instance of {@link SignInHandler}. */
    private static final SignInHandler handlerInstance = DaggerAppComponent.create().buildSignInHandler();

    /**
     * Provides the singleton instance of {@link SignInHandler}.
     *
     * @return the instance of {@link SignInHandler}.
     * @throws IllegalStateException if the handler instance is not initialized.
     */
    public static SignInHandler getHandler() {
        if (handlerInstance == null) {
            throw new IllegalStateException("SignInHandler is not initialized");
        }
        return handlerInstance;
    }
}

