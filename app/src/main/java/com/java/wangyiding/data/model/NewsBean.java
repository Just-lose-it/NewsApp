package com.java.wangyiding.data.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

// newsBean
public class NewsBean{

    @SerializedName("pageSize")
    private String pageSize;

    @SerializedName("total")
    private int total;

    @SerializedName("data")
    private List<News> data;

    public String getPageSize() {
        return pageSize;
    }

    public int getTotal() {
        return total;
    }

    public List<News> getData() {
        return data;
    }
}
