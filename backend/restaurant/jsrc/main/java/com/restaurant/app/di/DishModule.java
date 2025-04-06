//package com.restaurant.app.di;
//
//import com.restaurant.app.services.dish.DishService;
//import com.restaurant.app.services.DynamoDBService;
//import dagger.Module;
//import dagger.Provides;
//
//import javax.inject.Singleton;
//
//@Module
//public class DishModule {
//    @Provides
//    @Singleton
//    DishService provideDishService(DynamoDBService dynamoDBService) {
//        return new DishService(dynamoDBService);
//    }
//}


package com.restaurant.app.di;

import com.restaurant.app.services.dish.DishService;
import com.restaurant.app.services.DynamoDBService;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Dagger module for providing a singleton instance of {@link DishService}.
 *
 * <p>This module helps inject the DishService class wherever it is needed,
 * primarily in handlers related to dishes (like DishHandler, DishPopularHandler, etc.).</p>
 *
 * <p>The DishService depends on {@link DynamoDBService}, which is automatically injected
 * by Dagger when this service is constructed.</p>
 */
@Module
public class DishModule {

    /**
     * Provides a singleton instance of {@link DishService}, initialized with
     * the application's {@link DynamoDBService}.
     *
     * @param dynamoDBService the service used to interact with DynamoDB
     * @return a configured instance of {@link DishService}
     */
    @Provides
    @Singleton
    DishService provideDishService(DynamoDBService dynamoDBService) {
        return new DishService(dynamoDBService);
    }
}
