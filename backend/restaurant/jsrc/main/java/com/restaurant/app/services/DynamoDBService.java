package com.restaurant.app.services;

import com.restaurant.app.models.dish.Dish;
import com.restaurant.app.models.location.Feedback;
import com.restaurant.app.models.location.Location;
import com.restaurant.app.models.auth.User;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;


import javax.inject.Inject;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class DynamoDBService {

    private final DynamoDbTable<User> userTable;
    private final DynamoDbTable<Dish> dishTable;
    private final DynamoDbTable<Location> locationTable;
    private final DynamoDbTable<Feedback> feedbackTable;


    @Inject
    public DynamoDBService(DynamoDbTable<User> userTable, DynamoDbTable<Dish> dishTable, DynamoDbTable<Location> locationTable, DynamoDbTable<Feedback> feedbackTable) {
        this.userTable = userTable;
        this.dishTable = dishTable;
        this.locationTable = locationTable;
        this.feedbackTable = feedbackTable;
    }

    public void saveUser(User user, String cognitoSub, String role) {

        user.setCognitoSub(cognitoSub);

        user.setRole(role);

        user.setCreatedAt(Instant.now().toString());

        userTable.putItem(PutItemEnhancedRequest.builder(User.class).item(user).build());

    }

    public boolean checkIfWaiter(String email) {
        User user = getUser(email);
        return user != null && "Waiter".equals(user.getRole());

    }

    public User getUser(String email) {
        Key key = Key.builder().partitionValue(email).build();
        return userTable.getItem(key);

    }

    public Dish getDish(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return dishTable.getItem(key);

    }

    public List<Location> getAllLocations() {
        return locationTable.scan().items().stream().toList();
    }

    public Location getLocation(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return locationTable.getItem(key);
    }


    public boolean locationExists(String locationId) {
        return getLocation(locationId) != null;
    }

    public List<Dish> getAllDishesSortedByPopularity() {
        try {
            return dishTable.scan(ScanEnhancedRequest.builder().build()).items()
                    .stream().
                    sorted((d1, d2) -> Integer.compare(d2.getOrder(), d1.getOrder()))
                    .limit(4) // ui have four popular dishes only
                    .collect(Collectors.toList());

        } catch (DynamoDbException e) {
            throw new RuntimeException("Failed to retrieve dishes", e);
        }
    }

    public Feedback getFeedback(String id){
        Key key = Key.builder().partitionValue(id).build();
        return feedbackTable.getItem(key);
    }

}

