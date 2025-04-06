//package com.restaurant.signup.models;
//
//public class SortObject {
//    private String direction;
//    private String nullHandling;
//    private boolean ascending;
//    private String property;
//    private boolean ignoreCase;
//
//    public SortObject(String direction, String nullHandling, boolean ascending, String property, boolean ignoreCase) {
//        this.direction = direction;
//        this.nullHandling = nullHandling;
//        this.ascending = ascending;
//        this.property = property;
//        this.ignoreCase = ignoreCase;
//    }
//
//    // Getters and setters ...
//
//    public String getDirection() {
//        return direction;
//    }
//
//    public String getNullHandling() {
//        return nullHandling;
//    }
//
//    public boolean isAscending() {
//        return ascending;
//    }
//
//    public String getProperty() {
//        return property;
//    }
//
//    public boolean isIgnoreCase() {
//        return ignoreCase;
//    }
//}


package com.restaurant.app.models.location;

public class SortObject {
    private String direction;
    private String nullHandling;
    private boolean ascending;
    private String property;
    private boolean ignoreCase;

    public SortObject(String direction, String nullHandling, boolean ascending, String property, boolean ignoreCase) {
        this.direction = direction;
        this.nullHandling = nullHandling;
        this.ascending = ascending;
        this.property = property;
        this.ignoreCase = ignoreCase;
    }

    // Getters and Setters (omitted for brevity)

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getNullHandling() {
        return nullHandling;
    }

    public void setNullHandling(String nullHandling) {
        this.nullHandling = nullHandling;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }
}