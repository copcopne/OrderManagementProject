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
    private double unitPrice;
    private int quantity;
    private Double discount;

    public OrderDetail(int orderID, String SKU, double unitPrice, int quantity, Double discount) {
        this.orderID = orderID;
        this.SKU = SKU;
        this.unitPrice = unitPrice;
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
     * @return the unitPrice
     */
    public double getUnitPrice() {
        return unitPrice;
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
    
    
}