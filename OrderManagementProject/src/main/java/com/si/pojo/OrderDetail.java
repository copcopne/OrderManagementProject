/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.si.pojo;

/**
 *
 * @author ttuan
 */
public class OrderDetail {
    private int orderID;
    private String SKU;
    private int quantity;
    private Double discount;

    public OrderDetail(int orderID, String SKU, int quantity, Double discount) {
        this.orderID = orderID;
        this.SKU = SKU;
        this.quantity = quantity;
        this.discount = discount;
    }

    /**
     * @return the orderID
     */
    public int getOrderID() {
        return orderID;
    }

    /**
     * @return the SKU
     */
    public String getSKU() {
        return SKU;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @return the discount
     */
    public Double getDiscount() {
        return discount;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(Double discount) {
        this.discount = discount;
    }
    
    
}

