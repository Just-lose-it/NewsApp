package com.java.wangyiding.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class News {

    @SerializedName("newsID")
    private String newsId;

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("publishTime")
    private String publishTime;

    @SerializedName("category")
    private String category;

    @SerializedName("publisher")
    private String publisher;

    @SerializedName("video")
    private String videoUrl;

    @SerializedName("image")
    private String imageJson; // 原始为字符串，建议后处理为 List<String>


    @SerializedName("keywords")
    private List<Keyword> keywords;

    @SerializedName("persons")
    private List<Person> persons;

    @SerializedName("organizations")
    private List<Organization> organizations;

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setImageJson(String imageJson) {
        this.imageJson = imageJson;
    }

    // === 内部类 ===
    public static class Keyword {
        private double score;
        private String word;

        // Getters/Setters
    }

    public static class Person {
        private int count;
        private String mention;
        private String linkedURL;

        // Getters/Setters
    }

    public static class Organization {
        private int count;
        private String mention;
        private String linkedURL;

        // Getters/Setters
    }

    // === Getters ===
    public String getNewsId() {
        return newsId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public String getCategory() {
        return category;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getImageJson() {
        return imageJson;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }
}