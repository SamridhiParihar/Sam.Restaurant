//package com.restaurant.signup.models;
//
//import java.util.List;
//
//public class PageableObject {
//    private String nextToken;
//    private List<SortObject> sort;
//    private int pageSize;
//    private boolean hasNext;
//
//    // Simplified constructor
//    public PageableObject(String nextToken, List<SortObject> sort, int pageSize, boolean hasNext) {
//        this.nextToken = nextToken;
//        this.sort = sort;
//        this.pageSize = pageSize;
//        this.hasNext = hasNext;
//    }
//
//    // Getters
//    public String getNextToken() { return nextToken; }
//    public List<SortObject> getSort() { return sort; }
//    public int getPageSize() { return pageSize; }
//    public boolean isHasNext() { return hasNext; }
//}




package com.restaurant.app.models.location;

import java.util.List;

public class PageableObject {
    private long offset;
    private List<SortObject> sort;
    private boolean paged;
    private int pageSize;
    private int pageNumber;
    private boolean unpaged;

    public PageableObject(long offset, List<SortObject> sort, boolean paged, int pageSize, int pageNumber, boolean unpaged) {
        this.offset = offset;
        this.sort = sort;
        this.paged = paged;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.unpaged = unpaged;
    }

    // Getters and Setters (omitted for brevity)


    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public List<SortObject> getSort() {
        return sort;
    }

    public void setSort(List<SortObject> sort) {
        this.sort = sort;
    }

    public boolean isPaged() {
        return paged;
    }

    public void setPaged(boolean paged) {
        this.paged = paged;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public boolean isUnpaged() {
        return unpaged;
    }

    public void setUnpaged(boolean unpaged) {
        this.unpaged = unpaged;
    }
}