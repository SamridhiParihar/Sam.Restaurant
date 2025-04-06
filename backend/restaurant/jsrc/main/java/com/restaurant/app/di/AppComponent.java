//package com.restaurant.app.di;
//
////import com.restaurant.signup.handlers.booking.ClientBookingHandler;
//import com.restaurant.app.handlers.auth.SignInHandler;
//import com.restaurant.app.handlers.auth.SignUpHandler;
//import com.restaurant.app.handlers.booking.ClientBookingHandler;
//import com.restaurant.app.handlers.booking.ReservationsHandler;
//import com.restaurant.app.handlers.dish.DishHandler;
//import com.restaurant.app.handlers.dish.DishPopularHandler;
//import com.restaurant.app.handlers.location.LocationFeedbackHandler;
//import com.restaurant.app.handlers.location.LocationHandler;
//import com.restaurant.app.handlers.location.ShortLocationHandler;
//import com.restaurant.app.handlers.location.SpecialDishHandler;
//import com.restaurant.app.handlers.reservation.DeleteReservationHandler;
//import com.restaurant.app.handlers.reservation.ReservationHistoryHandler;
//import dagger.Component;
//
//import javax.inject.Singleton;
//
//
//@Singleton
//@Component(modules = {CognitoModule.class, DynamoDBModule.class, UserModule.class,DishModule.class,LocationModule.class, TokenModule.class})
//public interface AppComponent {
//    SignUpHandler buildSignUpHandler();
//    SignInHandler buildSignInHandler();
//    DishHandler buildDishHandler();
//    LocationHandler buildLocationHandler();
//    SpecialDishHandler buildSpecialDishHandler();
//    ShortLocationHandler buildShortLocationHandler();
//    ReservationsHandler buildReservationsHandler();
//    ClientBookingHandler buildClientBookingHandler();
//    ReservationHistoryHandler buildReservationHistoryHandler();
//    DeleteReservationHandler buildDeleteReservationHandler();
//    DishPopularHandler buildDishPopularHandler();
//    LocationFeedbackHandler buildLocationFeedbackHandler();
//}
//
//
//


package com.restaurant.app.di;

import com.restaurant.app.handlers.auth.SignInHandler;
import com.restaurant.app.handlers.auth.SignUpHandler;
import com.restaurant.app.handlers.booking.ClientBookingHandler;
import com.restaurant.app.handlers.booking.ReservationsHandler;
import com.restaurant.app.handlers.dish.DishHandler;
import com.restaurant.app.handlers.dish.DishPopularHandler;
import com.restaurant.app.handlers.location.LocationFeedbackHandler;
import com.restaurant.app.handlers.location.LocationHandler;
import com.restaurant.app.handlers.location.ShortLocationHandler;
import com.restaurant.app.handlers.location.SpecialDishHandler;
import com.restaurant.app.handlers.reservation.DeleteReservationHandler;
import com.restaurant.app.handlers.reservation.ReservationHistoryHandler;
import dagger.Component;

import javax.inject.Singleton;

/**
 * Dagger AppComponent interface that serves as the main dependency injection component
 * for the Restaurant application.
 *
 * <p>It connects all modules like Cognito, DynamoDB, and custom service modules, and
 * allows the injection of dependencies into AWS Lambda handlers.</p>
 *
 * <p>Each method here builds and provides a fully injected instance of a handler class.</p>
 */
@Singleton
@Component(modules = {
        CognitoModule.class,
        DynamoDBModule.class,
        UserModule.class,
        DishModule.class,
        LocationModule.class,
        TokenModule.class
})
public interface AppComponent {

    /**
     * Provides an instance of SignUpHandler with all dependencies injected.
     */
    SignUpHandler buildSignUpHandler();

    /**
     * Provides an instance of SignInHandler with all dependencies injected.
     */
    SignInHandler buildSignInHandler();

    /**
     * Provides an instance of DishHandler to manage dish-related operations.
     */
    DishHandler buildDishHandler();

    /**
     * Provides an instance of LocationHandler to manage restaurant location-related operations.
     */
    LocationHandler buildLocationHandler();

    /**
     * Provides an instance of SpecialDishHandler to fetch or manage location-specific special dishes.
     */
    SpecialDishHandler buildSpecialDishHandler();

    /**
     * Provides an instance of ShortLocationHandler to retrieve summarized location data.
     */
    ShortLocationHandler buildShortLocationHandler();

    /**
     * Provides an instance of ReservationsHandler for managing available reservations.
     */
    ReservationsHandler buildReservationsHandler();

    /**
     * Provides an instance of ClientBookingHandler for handling client booking requests.
     */
    ClientBookingHandler buildClientBookingHandler();

    /**
     * Provides an instance of ReservationHistoryHandler to retrieve user's past reservations.
     */
    ReservationHistoryHandler buildReservationHistoryHandler();

    /**
     * Provides an instance of DeleteReservationHandler to allow reservation deletion.
     */
    DeleteReservationHandler buildDeleteReservationHandler();

    /**
     * Provides an instance of DishPopularHandler to fetch popular dishes.
     */
    DishPopularHandler buildDishPopularHandler();

    /**
     * Provides an instance of LocationFeedbackHandler to manage user feedback on locations.
     */
    LocationFeedbackHandler buildLocationFeedbackHandler();
}

