package com.coffeeprogrammer.yummisto.models;

public class Category {
    private String id;
    private String name;

    // Empty constructor (required by Firebase)
    public Category() {
    }

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
