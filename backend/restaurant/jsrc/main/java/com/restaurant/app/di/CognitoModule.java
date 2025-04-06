//package com.restaurant.app.di;
//
//import dagger.Module;
//import dagger.Provides;
//import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
//
//import javax.inject.Singleton;
//
//@Module
//public class CognitoModule {
//
//    @Provides
//    @Singleton
//    CognitoIdentityProviderClient provideCognitoClient() {
//        return CognitoIdentityProviderClient.builder()
//                .region(Region.of(System.getenv("REGION")))
//                .credentialsProvider(DefaultCredentialsProvider.create())
//                .build();
//    }
//}


package com.restaurant.app.di;

import dagger.Module;
import dagger.Provides;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

import javax.inject.Singleton;

/**
 * Dagger module for providing a singleton instance of AWS CognitoIdentityProviderClient.
 *
 * <p>This module sets up the Cognito client using environment variables to dynamically configure
 * the AWS region, and uses the default credentials provider chain to authenticate.</p>
 *
 * <p>It is used by the Dagger AppComponent to inject the Cognito client wherever required.</p>
 */
@Module
public class CognitoModule {

    /**
     * Provides a singleton instance of {@link CognitoIdentityProviderClient}.
     *
     * <p>The client is built using:
     * <ul>
     *   <li>Region: Fetched from the "REGION" environment variable</li>
     *   <li>Credentials: DefaultCredentialsProvider (which searches in environment, AWS config files, IAM roles, etc.)</li>
     * </ul>
     * </p>
     *
     * @return A configured instance of {@link CognitoIdentityProviderClient}
     */
    @Provides
    @Singleton
    CognitoIdentityProviderClient provideCognitoClient() {
        return CognitoIdentityProviderClient.builder()
                .region(Region.of(System.getenv("REGION")))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
