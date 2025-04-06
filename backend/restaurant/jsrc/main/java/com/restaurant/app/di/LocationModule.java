//package com.restaurant.app.di;
//
//import com.restaurant.app.services.DynamoDBService;
//import com.restaurant.app.services.location.LocationService;
//import dagger.Module;
//import dagger.Provides;
//
//import javax.inject.Singleton;
//
//@Module
//public class LocationModule {
//    @Provides
//    @Singleton
//    LocationService provideLocationService(DynamoDBService dynamoDBService){
//        return new LocationService(dynamoDBService);
//    }
//}


package com.restaurant.app.di;

import com.restaurant.app.services.DynamoDBService;
import com.restaurant.app.services.location.LocationService;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Dagger module that provides a singleton instance of {@link LocationService}.
 *
 * <p>This module is responsible for wiring the LocationService, which handles
 * location-related business logic such as managing restaurant locations, fetching details,
 * and handling location-based queries.</p>
 *
 * <p>LocationService depends on {@link DynamoDBService} for database operations,
 * which is injected automatically by Dagger.</p>
 */
@Module
public class LocationModule {

    /**
     * Provides a singleton instance of {@link LocationService} initialized with
     * the application's {@link DynamoDBService}.
     *
     * @param dynamoDBService the service used to access DynamoDB tables
     * @return a configured instance of {@link LocationService}
     */
    @Provides
    @Singleton
    LocationService provideLocationService(DynamoDBService dynamoDBService) {
        return new LocationService(dynamoDBService);
    }
}
