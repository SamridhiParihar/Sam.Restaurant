//package com.restaurant.app.di;
//
//import com.restaurant.app.models.dish.Dish;
//import com.restaurant.app.models.location.Feedback;
//import com.restaurant.app.models.location.Location;
//import com.restaurant.app.models.bookings.ResTable;
//import com.restaurant.app.models.bookings.Reservation;
//import dagger.Module;
//import dagger.Provides;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
//import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
//import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
//import com.restaurant.app.models.auth.User;
//
//import static software.amazon.awssdk.enhanced.dynamodb.TableSchema.fromBean;
//
//import javax.inject.Singleton;
//
//@Module
//public class DynamoDBModule {
//
//    @Provides
//    @Singleton
//    DynamoDbClient provideDynamoDbClient() {
//        return DynamoDbClient.builder()
//                .region(Region.of(System.getenv("REGION")))
//                .build();
//    }
//
//    @Provides
//    @Singleton
//    DynamoDbEnhancedClient provideDynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
//        return DynamoDbEnhancedClient.builder()
//                .dynamoDbClient(dynamoDbClient)
//                .build();
//    }
//
//    @Provides
//    @Singleton
//    DynamoDbTable<User> provideUserTable(DynamoDbEnhancedClient enhancedClient) {
//        String TABLE_NAME = System.getenv("TABLE_NAME");
//        return enhancedClient.table(TABLE_NAME, fromBean(User.class));
//    }
//
//    @Provides
//    @Singleton
//    DynamoDbTable<Dish> provideDishTable(DynamoDbEnhancedClient enhancedClient) {
//        String DISH_TABLE_NAME = System.getenv("DISH_TABLE_NAME");
//        return enhancedClient.table(DISH_TABLE_NAME, fromBean(Dish.class));
//    }
//
//    @Provides
//    @Singleton
//    DynamoDbTable<Location> provideLocationTable(DynamoDbEnhancedClient enhancedClient){
//        String LOCATION_TABLE_NAME= System.getenv("LOCATION_TABLE_NAME");
//        return enhancedClient.table(LOCATION_TABLE_NAME,fromBean(Location.class));
//    }
//
//    @Provides
//    @Singleton
//    DynamoDbTable<Feedback> provideFeedBackTable(DynamoDbEnhancedClient enhancedClient){
//        String FEEDBACK_TABLE = System.getenv("FEEDBACK_TABLE");
//        return enhancedClient.table(FEEDBACK_TABLE,fromBean(Feedback.class));
//    }
//
//
//    @Provides
//    @Singleton
//    DynamoDbTable<ResTable> provideResTableTable(DynamoDbEnhancedClient enhancedClient){
//        String RES_TABLE_TABLE = System.getenv("RES_TABLE_TABLE");
//        return enhancedClient.table(RES_TABLE_TABLE,fromBean(ResTable.class));
//    }
//
//
//    @Provides
//    @Singleton
//    DynamoDbTable<Reservation> provideReservationTable(DynamoDbEnhancedClient enhancedClient){
//        String RESERVATION_TABLE = System.getenv("RESERVATION_TABLE");
//        return enhancedClient.table(RESERVATION_TABLE,fromBean(Reservation.class));
//    }
//
//
//
//
//
//
//
//}


package com.restaurant.app.di;

import com.restaurant.app.models.dish.Dish;
import com.restaurant.app.models.location.Feedback;
import com.restaurant.app.models.location.Location;
import com.restaurant.app.models.bookings.ResTable;
import com.restaurant.app.models.bookings.Reservation;
import com.restaurant.app.models.auth.User;
import dagger.Module;
import dagger.Provides;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import javax.inject.Singleton;

import static software.amazon.awssdk.enhanced.dynamodb.TableSchema.fromBean;

/**
 * Dagger module for setting up and providing DynamoDB-related resources:
 * - DynamoDbClient
 * - DynamoDbEnhancedClient
 * - Table mappings for all model entities
 *
 * <p>This module wires AWS DynamoDB with enhanced client support, mapping each model
 * class to a corresponding DynamoDB table. Table names are read from environment variables.</p>
 */
@Module
public class DynamoDBModule {

    /**
     * Provides the base DynamoDB client configured with region from environment.
     *
     * @return a standard {@link DynamoDbClient} instance
     */
    @Provides
    @Singleton
    DynamoDbClient provideDynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.of(System.getenv("REGION")))
                .build();
    }

    /**
     * Provides the enhanced DynamoDB client used for working with Java objects (POJOs).
     *
     * @param dynamoDbClient the base DynamoDB client
     * @return a {@link DynamoDbEnhancedClient} instance
     */
    @Provides
    @Singleton
    DynamoDbEnhancedClient provideDynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    /**
     * Provides the table mapping for the User model.
     *
     * @param enhancedClient the enhanced client
     * @return {@link DynamoDbTable} for {@link User}
     */
    @Provides
    @Singleton
    DynamoDbTable<User> provideUserTable(DynamoDbEnhancedClient enhancedClient) {
        String TABLE_NAME = System.getenv("TABLE_NAME");
        return enhancedClient.table(TABLE_NAME, fromBean(User.class));
    }

    /**
     * Provides the table mapping for the Dish model.
     *
     * @param enhancedClient the enhanced client
     * @return {@link DynamoDbTable} for {@link Dish}
     */
    @Provides
    @Singleton
    DynamoDbTable<Dish> provideDishTable(DynamoDbEnhancedClient enhancedClient) {
        String DISH_TABLE_NAME = System.getenv("DISH_TABLE_NAME");
        return enhancedClient.table(DISH_TABLE_NAME, fromBean(Dish.class));
    }

    /**
     * Provides the table mapping for the Location model.
     *
     * @param enhancedClient the enhanced client
     * @return {@link DynamoDbTable} for {@link Location}
     */
    @Provides
    @Singleton
    DynamoDbTable<Location> provideLocationTable(DynamoDbEnhancedClient enhancedClient){
        String LOCATION_TABLE_NAME = System.getenv("LOCATION_TABLE_NAME");
        return enhancedClient.table(LOCATION_TABLE_NAME, fromBean(Location.class));
    }

    /**
     * Provides the table mapping for the Feedback model.
     *
     * @param enhancedClient the enhanced client
     * @return {@link DynamoDbTable} for {@link Feedback}
     */
    @Provides
    @Singleton
    DynamoDbTable<Feedback> provideFeedBackTable(DynamoDbEnhancedClient enhancedClient){
        String FEEDBACK_TABLE = System.getenv("FEEDBACK_TABLE");
        return enhancedClient.table(FEEDBACK_TABLE, fromBean(Feedback.class));
    }

    /**
     * Provides the table mapping for the ResTable model (restaurant table entity).
     *
     * @param enhancedClient the enhanced client
     * @return {@link DynamoDbTable} for {@link ResTable}
     */
    @Provides
    @Singleton
    DynamoDbTable<ResTable> provideResTableTable(DynamoDbEnhancedClient enhancedClient){
        String RES_TABLE_TABLE = System.getenv("RES_TABLE_TABLE");
        return enhancedClient.table(RES_TABLE_TABLE, fromBean(ResTable.class));
    }

    /**
     * Provides the table mapping for the Reservation model.
     *
     * @param enhancedClient the enhanced client
     * @return {@link DynamoDbTable} for {@link Reservation}
     */
    @Provides
    @Singleton
    DynamoDbTable<Reservation> provideReservationTable(DynamoDbEnhancedClient enhancedClient){
        String RESERVATION_TABLE = System.getenv("RESERVATION_TABLE");
        return enhancedClient.table(RESERVATION_TABLE, fromBean(Reservation.class));
    }
}

