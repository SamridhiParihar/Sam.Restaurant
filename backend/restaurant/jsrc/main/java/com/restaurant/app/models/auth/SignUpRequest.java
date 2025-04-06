package com.restaurant.app.models.auth;

import org.json.JSONObject;

public class SignUpRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public SignUpRequest(String email, String password, String firstName, String lastName) {
        if (email == null || password == null || firstName == null || lastName == null) {
            throw new IllegalArgumentException("Missing or incomplete data.");
        }
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public static SignUpRequest fromJson(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        return new SignUpRequest(
                json.optString("email", null),
                json.optString("password", null),
                json.optString("firstName", null),
                json.optString("lastName", null)
        );
    }
}
