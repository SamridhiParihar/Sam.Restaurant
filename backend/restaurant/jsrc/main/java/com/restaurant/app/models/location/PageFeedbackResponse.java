//package com.restaurant.signup.models;
//
//import java.util.List;
//
//public class PageFeedbackResponse {
//    private List<FeedbackResponse> content;
//    private PageableObject pageable;
//    private boolean last;
//
//    // Simplified constructor
//    public PageFeedbackResponse(List<FeedbackResponse> content, PageableObject pageable, boolean last) {
//        this.content = content;
//        this.pageable = pageable;
//        this.last = last;
//    }
//
//    // Getters
//    public List<FeedbackResponse> getContent() { return content; }
//    public PageableObject getPageable() { return pageable; }
//    public boolean isLast() { return last; }
//}


package com.restaurant.app.models.location;

import java.util.List;

public class PageFeedbackResponse {
    private int totalPages;
    private long totalElements;
    private int size;
    private List<FeedbackResponse> content;
    private int number;
    private List<SortObject> sort;
    private boolean first;
    private boolean last;
    private int numberOfElements;
    private PageableObject pageable;
    private boolean empty;

    public PageFeedbackResponse(int totalPages, long totalElements, int size, List<FeedbackResponse> content, int number, List<SortObject> sort, boolean first, boolean last, int numberOfElements, PageableObject pageable, boolean empty) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
        this.content = content;
        this.number = number;
        this.sort = sort;
        this.first = first;
        this.last = last;
        this.numberOfElements = numberOfElements;
        this.pageable = pageable;
        this.empty = empty;
    }

    // Getters and Setters (omitted for brevity)

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<FeedbackResponse> getContent() {
        return content;
    }

    public void setContent(List<FeedbackResponse> content) {
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<SortObject> getSort() {
        return sort;
    }

    public void setSort(List<SortObject> sort) {
        this.sort = sort;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public PageableObject getPageable() {
        return pageable;
    }

    public void setPageable(PageableObject pageable) {
        this.pageable = pageable;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}