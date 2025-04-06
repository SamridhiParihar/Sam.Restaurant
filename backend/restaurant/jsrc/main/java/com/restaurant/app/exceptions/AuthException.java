package com.restaurant.app.exceptions;

/**
 * Custom exception class for handling authentication-related errors.
 *
 * <p>This exception is thrown when an authentication or authorization failure occurs,
 * such as invalid credentials, access denial, or token issues.</p>
 *
 * <p>It includes an HTTP status code to facilitate building appropriate
 * API Gateway responses in AWS Lambda handlers.</p>
 *
 * <p>Example usage:
 * <pre>{@code
 *   throw new AuthException("Invalid credentials", 401);
 * }</pre></p>
 */
public class AuthException extends RuntimeException {

    /**
     * The HTTP status code to be returned with the exception.
     */
    private final int statusCode;

    /**
     * Constructs a new {@code AuthException} with a specified message and HTTP status code.
     *
     * @param message The detail message describing the authentication error.
     * @param statusCode The HTTP status code associated with this error (e.g., 401, 403).
     */
    public AuthException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Retrieves the HTTP status code associated with this exception.
     *
     * @return The HTTP status code.
     */
    public int getStatusCode() {
        return statusCode;
    }
}
