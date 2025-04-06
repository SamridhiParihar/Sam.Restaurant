package com.restaurant.app.models.auth;

import org.json.JSONObject;

public class SignInRequest {

    private String email;

    private String password;

    public SignInRequest(String email, String password){

        if(password==null || email==null){

            throw new IllegalArgumentException("Missing , incomplete data ");

        }

        this.email = email;

        this.password = password;

    }

    public String getPassword() {

        return password;

    }

    public void setPassword(String password) {

        this.password = password;

    }

    public String getEmail() {

        return email;

    }

    public void setEmail(String email) {

        this.email = email;

    }

    public static SignInRequest fromJson(String jsonString) {

        JSONObject json = new JSONObject(jsonString);

        return new SignInRequest(

                json.optString("email", null),

                json.optString("password", null)

        );

    }

}

