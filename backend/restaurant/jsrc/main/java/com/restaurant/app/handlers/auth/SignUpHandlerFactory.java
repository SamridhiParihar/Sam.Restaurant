//package com.restaurant.app.handlers.auth;
//
//import com.restaurant.app.di.DaggerAppComponent;
//
//public class SignUpHandlerFactory {
//    private static final SignUpHandler handlerInstance =
//            DaggerAppComponent.create().buildSignUpHandler();
//
//    public static SignUpHandler getHandler() {
//        if(handlerInstance==null){
//            throw new IllegalStateException("SingUpHandler is not initialized");
//        }
//        return handlerInstance;
//    }
//}

package com.restaurant.app.handlers.auth;

import com.restaurant.app.di.DaggerAppComponent;

/**
 * Factory class to provide a singleton instance of {@link SignUpHandler}.
 *
 * <p>This ensures that a single instance of {@link SignUpHandler} is created
 * using Dagger dependency injection and can be reused across multiple requests.</p>
 *
 * <p><b>Usage:</b></p>
 * <pre>
 *     SignUpHandler handler = SignUpHandlerFactory.getHandler();
 * </pre>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *     <li>Creates a single instance of {@link SignUpHandler} using Dagger.</li>
 *     <li>Provides access to the instance via the {@code getHandler()} method.</li>
 *     <li>Ensures that an exception is thrown if the instance is not properly initialized.</li>
 * </ul>
 */
public class SignUpHandlerFactory {

    /** Singleton instance of {@link SignUpHandler}. */
    private static final SignUpHandler handlerInstance =
            DaggerAppComponent.create().buildSignUpHandler();

    /**
     * Provides the singleton instance of {@link SignUpHandler}.
     *
     * @return the instance of {@link SignUpHandler}.
     * @throws IllegalStateException if the handler instance is not initialized.
     */
    public static SignUpHandler getHandler() {
        if (handlerInstance == null) {
            throw new IllegalStateException("SignUpHandler is not initialized");
        }
        return handlerInstance;
    }
}
