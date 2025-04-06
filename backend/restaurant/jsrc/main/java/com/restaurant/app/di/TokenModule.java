//package com.restaurant.app.di;
//
//
//import com.restaurant.app.services.auth.TokenVerifier;
//import dagger.Module;
//import dagger.Provides;
//
//import javax.inject.Singleton;
//
//@Module
//public class TokenModule {
//
//    @Provides
//    @Singleton
//    TokenVerifier provideTokenVerifier(){
//        return new TokenVerifier();
//    }
//}


package com.restaurant.app.di;

import com.restaurant.app.services.auth.TokenVerifier;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Dagger module that provides a singleton instance of {@link TokenVerifier}.
 *
 * <p>The TokenVerifier is responsible for verifying and decoding JWT access tokens
 * (e.g., from AWS Cognito) used in authentication and authorization across the application.</p>
 *
 * <p>This module enables Dagger to inject a shared TokenVerifier instance
 * wherever it's needed, ensuring consistent token validation logic.</p>
 */
@Module
public class TokenModule {

    /**
     * Provides a singleton instance of {@link TokenVerifier}.
     *
     * @return a new TokenVerifier instance
     */
    @Provides
    @Singleton
    TokenVerifier provideTokenVerifier() {
        return new TokenVerifier();
    }
}

