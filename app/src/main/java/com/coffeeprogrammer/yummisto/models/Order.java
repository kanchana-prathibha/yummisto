package com.coffeeprogrammer.yummisto.models;

public class Order {
    private String customerEmail; // Add customer email field
    private String cakeId; // Add cake ID field
    private String customerName;
    private String customerAddress;
    private String customerContact;
    private String cakeName;
    private int selectedQuantity;
    private double finalPrice;

    public Order() {
        // Default constructor required for Firebase
    }

    public Order(String customerEmail, String cakeId, String customerName, String customerAddress,
                 String customerContact, String cakeName, int selectedQuantity, double finalPrice) {
        this.customerEmail = customerEmail;
        this.cakeId = cakeId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerContact = customerContact;
        this.cakeName = cakeName;
        this.selectedQuantity = selectedQuantity;
        this.finalPrice = finalPrice;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCakeId() {
        return cakeId;
    }

    public void setCakeId(String cakeId) {
        this.cakeId = cakeId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getCakeName() {
        return cakeName;
    }

    public void setCakeName(String cakeName) {
        this.cakeName = cakeName;
    }

    public int getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }
}
