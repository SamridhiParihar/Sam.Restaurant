////package com.restaurant.signup.services;
//
////
//
////import com.restaurant.signup.models.User;
//
////import com.restaurant.signup.models.SignUpRequest;
//
////import com.restaurant.signup.validators.SignUpValidator;
//
////
//
////import javax.inject.Inject;
//
////import java.util.List;
//
////import java.util.Map;
//
////
//
////public class UserService {
//
////    private final CognitoService cognitoService;
//
////    private final DynamoDBService dynamoDBService;
//
////
//
////    @Inject
//
////    public UserService(CognitoService cognitoService, DynamoDBService dynamoDBService) {
//
////        this.cognitoService = cognitoService;
//
////        this.dynamoDBService = dynamoDBService;
//
////    }
//
////
//
////    public Map<String, Object> registerUser(SignUpRequest request) {
//
////        List<String> errors = SignUpValidator.validate(request);
//
////        if (!errors.isEmpty()) {
//
////            throw new IllegalArgumentException(String.join(", ", errors));
//
////        }
//
////
//
////        if (cognitoService.isEmailRegistered(request.getEmail())) {
//
////            throw new RuntimeException("A user with this email address already exists.");
//
////        }
//
////
//
////        String accessToken = cognitoService.registerUser(
//
////                request.getEmail(),
//
////                request.getPassword(),
//
////                request.getFirstName(),
//
////                request.getLastName()
//
////        );
//
////
//
////        String role = "Customer";
//
////        User user = new User();
//
////        user.setEmail(request.getEmail());
//
////        user.setFirstName(request.getFirstName());
//
////        user.setLastName(request.getLastName());
//
////        dynamoDBService.saveUser(user, accessToken, role);
//
////
//
////        return Map.of(
//
////                "message", "User registered successfully",
//
////                "accessToken", accessToken
//
////        );
//
////    }
//
////}
//
//package com.restaurant.app.services.auth;
//
//import com.restaurant.app.exceptions.AuthException;
//
//import com.restaurant.app.models.auth.SignInRequest;
//
//import com.restaurant.app.models.auth.User;
//
//import com.restaurant.app.models.auth.SignUpRequest;
//
//import com.restaurant.app.services.CognitoService;
//import com.restaurant.app.services.DynamoDBService;
//import com.restaurant.app.validators.SignUpValidator;
//
//import javax.inject.Inject;
//
//import java.util.List;
//
//import java.util.Map;
//
//public class UserService {
//
//    private final CognitoService cognitoService;
//
//    private final DynamoDBService dynamoDBService;
//
//    @Inject
//
//    public UserService(CognitoService cognitoService, DynamoDBService dynamoDBService) {
//
//        this.cognitoService = cognitoService;
//
//        this.dynamoDBService = dynamoDBService;
//
//    }
//
//    public Map<String, Object> registerUser(SignUpRequest request) {
//
//        List<String> errors = SignUpValidator.validate(request);
//
//        if (!errors.isEmpty()) {
//
//            return Map.of(
//
//                    "statusCode", 400,
//
//                    "body", Map.of("message", String.join(", ", errors))
//
//            );
//
//        }
//
//        if (cognitoService.isEmailRegistered(request.getEmail())) {
//
//            return Map.of(
//
//                    "statusCode", 409,
//
//                    "body", Map.of("message", "A user with this email address already exists.")
//
//            );
//
//        }
//
//
//        String role = "Customer";
//
//        if(dynamoDBService.checkIfWaiter(request.getEmail())){
//
//            role = "Waiter";
//
//        }
//
//        String accessToken = cognitoService.registerUser(
//
//                request.getEmail(),
//
//                request.getPassword(),
//
//                request.getFirstName(),
//
//                request.getLastName()
//
//        );
//
//        User user = new User();
//
//        user.setEmail(request.getEmail());
//
//        user.setFirstName(request.getFirstName());
//
//        user.setLastName(request.getLastName());
//
//        dynamoDBService.saveUser(user, accessToken, role);
//
//        return Map.of(
//
//                "statusCode", 201,
//
//                "body", Map.of(
//
//                        "message", "User registered successfully"
//
////                        "accessToken", accessToken
//
//                )
//
//        );
//
//    }
//
//    public Map<String, Object> signInUser(SignInRequest request) {
//
//        try {
//
//            String accessToken = cognitoService.signInUser(request.getEmail(), request.getPassword());
//
//            User user = dynamoDBService.getUser(request.getEmail());
//
//            return Map.of(
//
//                    "statusCode", 200,
//
//                    "body", Map.of(
//
//                            "username", user.getFirstName() + " " + user.getLastName(),
//
//                            "accessToken", accessToken,
//
//                            "role", user.getRole()
//
//                    )
//
//            );
//
//        } catch (AuthException e) {
//
//            return Map.of(
//
//                    "statusCode", e.getStatusCode(),
//
//                    "body", Map.of("message", e.getMessage())
//
//            );
//
//        }
//
//    }
//
//
//}
//

//package com.restaurant.app.services.auth;
//
//import com.restaurant.app.exceptions.AuthException;
//import com.restaurant.app.models.auth.SignInRequest;
//import com.restaurant.app.models.auth.User;
//import com.restaurant.app.models.auth.SignUpRequest;
//import com.restaurant.app.services.CognitoService;
//import com.restaurant.app.services.DynamoDBService;
//import com.restaurant.app.validators.SignUpValidator;
//
//import javax.inject.Inject;
//import java.util.List;
//import java.util.Map;
//
//public class UserService {
//
//    private static final int STATUS_SUCCESS = 200;
//    private static final int STATUS_CREATED = 201;
//    private static final int STATUS_BAD_REQUEST = 400;
//    private static final int STATUS_CONFLICT = 409;
//
//    private static final String MESSAGE_KEY = "message";
//    private static final String BODY_KEY = "body";
//    private static final String STATUS_CODE_KEY = "statusCode";
//
//    private final CognitoService cognitoService;
//    private final DynamoDBService dynamoDBService;
//
//    @Inject
//    public UserService(CognitoService cognitoService, DynamoDBService dynamoDBService) {
//        this.cognitoService = cognitoService;
//        this.dynamoDBService = dynamoDBService;
//    }
//
//    public Map<String, Object> registerUser(SignUpRequest request) {
//        List<String> errors = SignUpValidator.validate(request);
//        if (!errors.isEmpty()) {
//            return errorResponse(STATUS_BAD_REQUEST, String.join(", ", errors));
//        }
//
//        if (cognitoService.isEmailRegistered(request.getEmail())) {
//            return errorResponse(STATUS_CONFLICT, "A user with this email address already exists.");
//        }
//
//        String role = determineUserRole(request.getEmail());
//        String accessToken = cognitoService.registerUser(
//                request.getEmail(),
//                request.getPassword(),
//                request.getFirstName(),
//                request.getLastName()
//        );
//
//        User user = new User();
//        user.setRole(role);
//        user.setEmail(request.getEmail());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        dynamoDBService.saveUser(user, accessToken, role);
//
//        return successResponse(STATUS_CREATED, "User registered successfully");
//    }
//
//    public Map<String, Object> signInUser(SignInRequest request) {
//        try {
//            String accessToken = cognitoService.signInUser(request.getEmail(), request.getPassword());
//            User user = dynamoDBService.getUser(request.getEmail());
//
//            return Map.of(
//                    STATUS_CODE_KEY, STATUS_SUCCESS,
//                    BODY_KEY, Map.of(
//                            "username", user.getFirstName() + " " + user.getLastName(),
//                            "accessToken", accessToken,
//                            "role", user.getRole()
//                    )
//            );
//        } catch (AuthException e) {
//            return errorResponse(e.getStatusCode(), e.getMessage());
//        }
//    }
//
//    private String determineUserRole(String email) {
//        return dynamoDBService.checkIfWaiter(email) ? "Waiter" : "Customer";
//    }
//
//    private Map<String, Object> successResponse(int statusCode, String message) {
//        return Map.of(
//                STATUS_CODE_KEY, statusCode,
//                BODY_KEY, Map.of(MESSAGE_KEY, message)
//        );
//    }
//
//    private Map<String, Object> errorResponse(int statusCode, String message) {
//        return Map.of(
//                STATUS_CODE_KEY, statusCode,
//                BODY_KEY, Map.of(MESSAGE_KEY, message)
//        );
//    }
//}
//


package com.restaurant.app.services.auth;

import com.restaurant.app.exceptions.AuthException;
import com.restaurant.app.models.auth.SignInRequest;
import com.restaurant.app.models.auth.SignUpRequest;
import com.restaurant.app.models.auth.User;
import com.restaurant.app.services.CognitoService;
import com.restaurant.app.services.DynamoDBService;
import com.restaurant.app.validators.SignUpValidator;

import javax.inject.Inject;
import java.util.Map;

/**
 * Service class responsible for user registration and authentication.
 * Handles interaction with AWS Cognito for auth and DynamoDB for user storage.
 */
public class UserService {

    // HTTP status code constants for consistent API responses
    private static final int STATUS_SUCCESS = 200;
    private static final int STATUS_CREATED = 201;
    private static final int STATUS_BAD_REQUEST = 400;
    private static final int STATUS_CONFLICT = 409;

    // Keys used in API response maps
    private static final String MESSAGE_KEY = "message";
    private static final String BODY_KEY = "body";
    private static final String STATUS_CODE_KEY = "statusCode";

    private final CognitoService cognitoService;
    private final DynamoDBService dynamoDBService;

    /**
     * Constructor with dependencies injected.
     *
     * @param cognitoService   Service for interacting with AWS Cognito.
     * @param dynamoDBService  Service for interacting with DynamoDB.
     */
    @Inject
    public UserService(CognitoService cognitoService, DynamoDBService dynamoDBService) {
        this.cognitoService = cognitoService;
        this.dynamoDBService = dynamoDBService;
    }

    /**
     * Registers a new user with the system.
     * Performs validation, checks for existing user, assigns role, and saves the user.
     *
     * @param request Sign-up request containing user details.
     * @return Map containing status code and response body.
     */
    public Map<String, Object> registerUser(SignUpRequest request) {
        String validationError = validateSignUpRequest(request);
        if (validationError != null) {
            return errorResponse(STATUS_BAD_REQUEST, validationError);
        }

        // Check if the email is already registered
        if (cognitoService.isEmailRegistered(request.getEmail())) {
            return errorResponse(STATUS_CONFLICT, "A user with this email address already exists.");
        }

        // Determine if the user is a customer or waiter
        String role = determineUserRole(request.getEmail());

        // Register the user with Cognito
        String accessToken = cognitoService.registerUser(
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName()
        );

        // Create and save user in DynamoDB
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(role);

        dynamoDBService.saveUser(user, accessToken, role);

        return successResponse(STATUS_CREATED, "User registered successfully");
    }

    /**
     * Authenticates a user and returns access token and user info.
     *
     * @param request Sign-in request containing email and password.
     * @return Map with status code and user info on success, or error on failure.
     */
    public Map<String, Object> signInUser(SignInRequest request) {
        try {
            String validationError = validateSignInRequest(request);
            if (validationError != null) {
                return errorResponse(STATUS_BAD_REQUEST, validationError);
            }

            // Authenticate via Cognito
            String accessToken = cognitoService.signInUser(request.getEmail(), request.getPassword());

            // Fetch user data from DynamoDB
            User user = dynamoDBService.getUser(request.getEmail());

            return Map.of(
                    STATUS_CODE_KEY, STATUS_SUCCESS,
                    BODY_KEY, Map.of(
                            "username", user.getFirstName() + " " + user.getLastName(),
                            "accessToken", accessToken,
                            "role", user.getRole()
                    )
            );

        } catch (AuthException e) {
            return errorResponse(e.getStatusCode(), e.getMessage());
        }
    }

    /**
     * Validates the fields of a SignUpRequest object.
     *
     * @param request The signup request object to validate.
     * @return Error message string if validation fails, or null if valid.
     */
    private String validateSignUpRequest(SignUpRequest request) {
        if (!SignUpValidator.isValidName(request.getFirstName())) {
            return "First name must be up to 50 characters. Only Latin letters, hyphens, and apostrophes are allowed.";
        }

        if (!SignUpValidator.isValidName(request.getLastName())) {
            return "Last name must be up to 50 characters. Only Latin letters, hyphens, and apostrophes are allowed.";
        }

        if (!SignUpValidator.isValidEmail(request.getEmail())) {
            return "Invalid email format. Please ensure it follows the format username@domain.com.";
        }

        if (!SignUpValidator.isValidPassword(request.getPassword())) {
            return "Password must be 8-16 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character.";
        }

        return null; // No errors
    }

    /**
     * Validates the fields of a SignInRequest object.
     *
     * @param request The signin request object to validate.
     * @return Error message string if validation fails, or null if valid.
     */
    private String validateSignInRequest(SignInRequest request) {
        if (!SignUpValidator.isValidEmail(request.getEmail())) {
            return "Email address format is incorrect.";
        }

        if (request.getEmail().isEmpty()) {
            return "Email address is required. Please enter your email to continue.";
        }

        if (request.getPassword().isEmpty()) {
            return "Password is required. Please enter your password to continue.";
        }

        return null; // No errors
    }

    /**
     * Determines the role of the user based on email.
     * Checks DynamoDB if the user is a waiter; otherwise, defaults to "Customer".
     *
     * @param email User's email.
     * @return Role as a string.
     */
    private String determineUserRole(String email) {
        return dynamoDBService.checkIfWaiter(email) ? "Waiter" : "Customer";
    }

    /**
     * Builds a successful API response with message.
     *
     * @param statusCode HTTP status code.
     * @param message    Success message.
     * @return Response map.
     */
    private Map<String, Object> successResponse(int statusCode, String message) {
        return Map.of(
                STATUS_CODE_KEY, statusCode,
                BODY_KEY, Map.of(MESSAGE_KEY, message)
        );
    }

    /**
     * Builds an error API response with message.
     *
     * @param statusCode HTTP status code.
     * @param message    Error message.
     * @return Response map.
     */
    private Map<String, Object> errorResponse(int statusCode, String message) {
        return Map.of(
                STATUS_CODE_KEY, statusCode,
                BODY_KEY, Map.of(MESSAGE_KEY, message)
        );
    }
}
