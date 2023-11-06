package com.coffeeprogrammer.yummisto.models;

public class Cake {
    private String id; // Optional if you want to assign an ID
    private String name;
    private double price;
    private double weight;
    private int availableQuantity;
    private String imageUrl;
    private String categoryId; // ID of the category to which the cake belongs

    public Cake() {
        // Default constructor required for Firebase Realtime Database deserialization.
    }

    public Cake(String id, String name, double price, double weight, int availableQuantity, String imageUrl, String categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.availableQuantity = availableQuantity;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    // Getter and Setter for ID (optional)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
