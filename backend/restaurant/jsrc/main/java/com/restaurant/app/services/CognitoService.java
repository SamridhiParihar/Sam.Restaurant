//package com.restaurant.app.services;
//
//import com.restaurant.app.exceptions.AuthException;
//
//import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
//
//import software.amazon.awssdk.services.cognitoidentityprovider.model.*;
//
//import javax.inject.Inject;
//
//import java.util.Map;
//
//public class CognitoService {
//
//    private final CognitoIdentityProviderClient cognitoClient;
//
//    private final String userPoolId;
//
//    private final String clientId;
//
//    @Inject
//
//    public CognitoService(CognitoIdentityProviderClient cognitoClient) {
//
//        this.cognitoClient = cognitoClient;
//
//        this.userPoolId = System.getenv("COGNITO_ID");
//
//        this.clientId = System.getenv("CLIENT_ID");
//
//    }
//
//
//
//    public String registerUser(String email, String password, String firstName, String lastName) {
//
//        // 1. Sign up the user
//
//        SignUpRequest signUpRequest = SignUpRequest.builder()
//
//                .clientId(clientId)
//
//                .username(email)
//
//                .password(password)
//
//                .userAttributes(
//
//                        AttributeType.builder().name("email").value(email).build(),
//
//                        AttributeType.builder().name("given_name").value(firstName).build(),
//
//                        AttributeType.builder().name("family_name").value(lastName).build()
//
//                )
//
//                .build();
//
//        cognitoClient.signUp(signUpRequest);
//
//        // 2. Confirm the user programmatically (critical fix)
//
//        AdminConfirmSignUpRequest confirmRequest = AdminConfirmSignUpRequest.builder()
//
//                .userPoolId(userPoolId)
//
//                .username(email)
//
//                .build();
//
//        cognitoClient.adminConfirmSignUp(confirmRequest);
//
//        // 3. Authenticate to get the ID token
//
//        return confirmUserSignUp(email, password);
//
//    }
//
//    private String confirmUserSignUp(String email, String password) {
//
//        try {
//
//            AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
//
//                    .userPoolId(userPoolId)
//
//                    .clientId(clientId)
//
//                    .authFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
//
//                    .authParameters(Map.of("USERNAME", email, "PASSWORD", password))
//
//                    .build();
//
//            AdminInitiateAuthResponse authResponse = cognitoClient.adminInitiateAuth(authRequest);
//
//            return authResponse.authenticationResult().accessToken();
//
//        } catch (Exception e) {
//
//            throw new RuntimeException("Error confirming user sign-up: " + e.getMessage(), e);
//
//        }
//
//    }
//
//    public boolean isEmailRegistered(String email) {
//
//        try {
//
//            cognitoClient.adminGetUser(AdminGetUserRequest.builder()
//
//                    .userPoolId(userPoolId)
//
//                    .username(email)
//
//                    .build());
//
//            return true;
//
//        } catch (UserNotFoundException e) {
//
//            return false;
//
//        } catch (Exception e) {
//
//            throw new RuntimeException("Error checking email registration: " + e.getMessage(), e);
//
//        }
//
//    }
//
//
//    public String signInUser(String email, String password) {
//
//        try {
//
//            AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
//
//                    .userPoolId(userPoolId)
//
//                    .clientId(clientId)
//
//                    .authFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
//
//                    .authParameters(Map.of(
//
//                            "USERNAME", email,
//
//                            "PASSWORD", password
//
//                    ))
//
//                    .build();
//
//            AdminInitiateAuthResponse response = cognitoClient.adminInitiateAuth(authRequest);
//
//            if (response.authenticationResult() != null) {
//
//                return response.authenticationResult().idToken();
//
//            }
//
//            throw new AuthException("Authentication challenge required", 400);
//
//        } catch (NotAuthorizedException e) {
//
//            // Check for account lockout message
//
//            if (e.getMessage().contains("attempts exceeded")) {
//
//                throw new AuthException("Account locked. Try again after 15 minutes", 423);
//
//            }
//
//            throw new AuthException("Invalid email or password", 401);
//
//        } catch (UserNotFoundException e) {
//
//            throw new AuthException("User not found", 404);
//
//        } catch (TooManyRequestsException e) {
//
//            throw new AuthException("Too many requests. Please try again later", 429);
//
//        } catch (Exception e) {
//
//            throw new AuthException("Authentication failed: " + e.getMessage(), 500);
//
//        }
//
//    }
//
//}
//


//package com.restaurant.app.services;
//
//import com.restaurant.app.exceptions.AuthException;
//import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
//import software.amazon.awssdk.services.cognitoidentityprovider.model.*;
//
//import javax.inject.Inject;
//import java.util.Map;
//
//public class CognitoService {
//
//    private final CognitoIdentityProviderClient cognitoClient;
//    private final String userPoolId;
//    private final String clientId;
//
//    @Inject
//    public CognitoService(CognitoIdentityProviderClient cognitoClient) {
//        this.cognitoClient = cognitoClient;
//        this.userPoolId = System.getenv("COGNITO_ID");
//        this.clientId = System.getenv("CLIENT_ID");
//    }
//
//    public String registerUser(String email, String password, String firstName, String lastName) {
//        signUp(email, password, firstName, lastName);
//        confirmSignUp(email);
//        return authenticateUser(email, password);
//    }
//
//    private void signUp(String email, String password, String firstName, String lastName) {
//        SignUpRequest signUpRequest = SignUpRequest.builder()
//                .clientId(clientId)
//                .username(email)
//                .password(password)
//                .userAttributes(
//                        AttributeType.builder().name("email").value(email).build(),
//                        AttributeType.builder().name("given_name").value(firstName).build(),
//                        AttributeType.builder().name("family_name").value(lastName).build()
//                )
//                .build();
//        cognitoClient.signUp(signUpRequest);
//    }
//
//    private void confirmSignUp(String email) {
//        AdminConfirmSignUpRequest confirmRequest = AdminConfirmSignUpRequest.builder()
//                .userPoolId(userPoolId)
//                .username(email)
//                .build();
//        cognitoClient.adminConfirmSignUp(confirmRequest);
//    }
//
//    private String authenticateUser(String email, String password) {
//        try {
//            AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
//                    .userPoolId(userPoolId)
//                    .clientId(clientId)
//                    .authFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
//                    .authParameters(Map.of("USERNAME", email, "PASSWORD", password))
//                    .build();
//
//            AdminInitiateAuthResponse authResponse = cognitoClient.adminInitiateAuth(authRequest);
//            return authResponse.authenticationResult().accessToken();
//        } catch (Exception e) {
//            throw new RuntimeException("Error confirming user sign-up: " + e.getMessage(), e);
//        }
//    }
//
//    public boolean isEmailRegistered(String email) {
//        try {
//            cognitoClient.adminGetUser(AdminGetUserRequest.builder()
//                    .userPoolId(userPoolId)
//                    .username(email)
//                    .build());
//            return true;
//        } catch (UserNotFoundException e) {
//            return false;
//        } catch (Exception e) {
//            throw new RuntimeException("Error checking email registration: " + e.getMessage(), e);
//        }
//    }
//
//    public String signInUser(String email, String password) {
//        try {
//            AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
//                    .userPoolId(userPoolId)
//                    .clientId(clientId)
//                    .authFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
//                    .authParameters(Map.of("USERNAME", email, "PASSWORD", password))
//                    .build();
//
//            AdminInitiateAuthResponse response = cognitoClient.adminInitiateAuth(authRequest);
//
//            if (response.authenticationResult() != null) {
//                return response.authenticationResult().idToken();
//            }
//
//            throw new AuthException("Authentication challenge required", 400);
//        } catch (NotAuthorizedException e) {
//            if (e.getMessage().contains("attempts exceeded")) {
//                throw new AuthException("Account locked. Try again after 15 minutes", 423);
//            }
//            throw new AuthException("Invalid email or password", 401);
//        } catch (UserNotFoundException e) {
//            throw new AuthException("User not found", 404);
//        } catch (TooManyRequestsException e) {
//            throw new AuthException("Too many requests. Please try again later", 429);
//        } catch (Exception e) {
//            throw new AuthException("Authentication failed: " + e.getMessage(), 500);
//        }
//    }
//}
//


package com.restaurant.app.services;

import com.restaurant.app.exceptions.AuthException;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import javax.inject.Inject;
import java.util.Map;

/**
 * Service class for handling user authentication and registration with AWS Cognito.
 *
 * <p>This service integrates with AWS Cognito for user management, including:
 * <ul>
 *     <li>User registration and confirmation</li>
 *     <li>User authentication</li>
 *     <li>Checking if an email is registered</li>
 * </ul>
 * </p>
 *
 * <p>Relies on the following environment variables:</p>
 * <ul>
 *     <li><b>COGNITO_ID</b>: The Cognito User Pool ID</li>
 *     <li><b>CLIENT_ID</b>: The Cognito App Client ID</li>
 * </ul>
 */
public class CognitoService {

    private final CognitoIdentityProviderClient cognitoClient;
    private final String userPoolId;
    private final String clientId;

    /**
     * Constructs an instance of {@link CognitoService}.
     *
     * @param cognitoClient the AWS Cognito Identity Provider client
     */
    @Inject
    public CognitoService(CognitoIdentityProviderClient cognitoClient) {
        this.cognitoClient = cognitoClient;
        this.userPoolId = System.getenv("COGNITO_ID");
        this.clientId = System.getenv("CLIENT_ID");
    }

    /**
     * Registers a new user, confirms their registration, and returns an authentication token.
     *
     * @param email     the user's email (username)
     * @param password  the user's password
     * @param firstName the user's first name
     * @param lastName  the user's last name
     * @return an access token if registration is successful
     */
    public String registerUser(String email, String password, String firstName, String lastName) {
        signUp(email, password, firstName, lastName);
        confirmSignUp(email);
        return authenticateUser(email, password);
    }

    /**
     * Registers a new user in Cognito.
     *
     * @param email     the user's email (username)
     * @param password  the user's password
     * @param firstName the user's first name
     * @param lastName  the user's last name
     */
    private void signUp(String email, String password, String firstName, String lastName) {
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .clientId(clientId)
                .username(email)
                .password(password)
                .userAttributes(
                        AttributeType.builder().name("email").value(email).build(),
                        AttributeType.builder().name("given_name").value(firstName).build(),
                        AttributeType.builder().name("family_name").value(lastName).build()
                )
                .build();
        cognitoClient.signUp(signUpRequest);
    }

    /**
     * Confirms a user's registration in Cognito.
     *
     * @param email the user's email (username)
     */
    private void confirmSignUp(String email) {
        AdminConfirmSignUpRequest confirmRequest = AdminConfirmSignUpRequest.builder()
                .userPoolId(userPoolId)
                .username(email)
                .build();
        cognitoClient.adminConfirmSignUp(confirmRequest);
    }

    /**
     * Authenticates a user and retrieves an access token.
     *
     * @param email    the user's email (username)
     * @param password the user's password
     * @return an access token if authentication is successful
     */
    private String authenticateUser(String email, String password) {
        try {
            AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
                    .userPoolId(userPoolId)
                    .clientId(clientId)
                    .authFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                    .authParameters(Map.of("USERNAME", email, "PASSWORD", password))
                    .build();

            AdminInitiateAuthResponse authResponse = cognitoClient.adminInitiateAuth(authRequest);
            return authResponse.authenticationResult().accessToken();
        } catch (Exception e) {
            throw new RuntimeException("Error confirming user sign-up: " + e.getMessage(), e);
        }
    }

    /**
     * Checks if an email is already registered in Cognito.
     *
     * @param email the email to check
     * @return true if the email is registered, false otherwise
     */
    public boolean isEmailRegistered(String email) {
        try {
            cognitoClient.adminGetUser(AdminGetUserRequest.builder()
                    .userPoolId(userPoolId)
                    .username(email)
                    .build());
            return true;
        } catch (UserNotFoundException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error checking email registration: " + e.getMessage(), e);
        }
    }

    /**
     * Authenticates a user and retrieves an ID token.
     *
     * @param email    the user's email (username)
     * @param password the user's password
     * @return an ID token if authentication is successful
     * @throws AuthException for authentication failures (e.g., invalid credentials, locked account)
     */
    public String signInUser(String email, String password) {
        try {
            AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
                    .userPoolId(userPoolId)
                    .clientId(clientId)
                    .authFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                    .authParameters(Map.of("USERNAME", email, "PASSWORD", password))
                    .build();

            AdminInitiateAuthResponse response = cognitoClient.adminInitiateAuth(authRequest);

            if (response.authenticationResult() != null) {
                return response.authenticationResult().idToken();
            }

            throw new AuthException("Authentication challenge required", 400);
        } catch (NotAuthorizedException e) {
            if (e.getMessage().contains("attempts exceeded")) {
                throw new AuthException("Account locked. Try again after 15 minutes", 423);
            }
            throw new AuthException("Invalid email or password", 401);
        } catch (UserNotFoundException e) {
            throw new AuthException("User not found", 404);
        } catch (TooManyRequestsException e) {
            throw new AuthException("Too many requests. Please try again later", 429);
        } catch (Exception e) {
            throw new AuthException("Authentication failed: " + e.getMessage(), 500);
        }
    }
}
