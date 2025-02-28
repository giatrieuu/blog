package com.example.blog.model;

public class Blog {
    private String id;
    private String title;
    private String shortDesc;
    private String longDesc;
    private String imageUrl;

    // Constructor rỗng cần cho Firestore
    public Blog() {}

    public Blog(String id, String title, String shortDesc, String longDesc, String imageUrl) {
        this.id = id;
        this.title = title;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.imageUrl = imageUrl;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getShortDesc() { return shortDesc; }
    public String getLongDesc() { return longDesc; }
    public String getImageUrl() { return imageUrl; }
}
