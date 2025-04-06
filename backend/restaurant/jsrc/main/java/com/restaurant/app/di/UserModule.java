//package com.restaurant.app.di;
//
//import dagger.Module;
//import dagger.Provides;
//import com.restaurant.app.services.CognitoService;
//import com.restaurant.app.services.DynamoDBService;
//import com.restaurant.app.services.auth.UserService;
//
//import javax.inject.Singleton;
//@Module
//public class UserModule {
//
//    @Provides
//    @Singleton
//    UserService provideUserService(CognitoService cognitoService, DynamoDBService dynamoDBService) {
//        return new UserService(cognitoService, dynamoDBService);
//    }
//}

package com.restaurant.app.di;

import dagger.Module;
import dagger.Provides;
import com.restaurant.app.services.CognitoService;
import com.restaurant.app.services.DynamoDBService;
import com.restaurant.app.services.auth.UserService;

import javax.inject.Singleton;

/**
 * Dagger module that provides a singleton instance of {@link UserService}.
 *
 * <p>The UserService handles core user-related operations such as registration,
 * profile management, and interaction with authentication and database layers.</p>
 *
 * <p>It relies on:
 * <ul>
 *     <li>{@link CognitoService} for authentication and user identity management via AWS Cognito</li>
 *     <li>{@link DynamoDBService} for storing and retrieving user-specific data in DynamoDB</li>
 * </ul>
 * </p>
 */
@Module
public class UserModule {

    /**
     * Provides a singleton instance of {@link UserService}.
     *
     * @param cognitoService   service to interact with AWS Cognito for auth operations
     * @param dynamoDBService  service to handle user-related DB operations in DynamoDB
     * @return a configured instance of UserService
     */
    @Provides
    @Singleton
    UserService provideUserService(CognitoService cognitoService, DynamoDBService dynamoDBService) {
        return new UserService(cognitoService, dynamoDBService);
    }
}
