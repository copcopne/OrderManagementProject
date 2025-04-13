/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.si.pojo;

import java.util.Date;

/**
 *
 * @author ttuan
 */
public class Order {
    private int orderId;
    private int customerId;
    private Date orderDate;
    private String shipAddress;
    private OrderStatus orderStatus;

    public Order(int orderId, int customerId, Date orderDate, String shipAddress, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.shipAddress = shipAddress;
        this.orderStatus = orderStatus;
    }

    /**
     * @return the orderId
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * @return the customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @return the orderDate
     */
    public Date getOrderDate() {
        return orderDate;
    }

    /**
     * @return the shipAddress
     */
    public String getShipAddress() {
        return shipAddress;
    }

    /**
     * @return the orderStatus
     */
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    /**
     * @param shipAddress the shipAddress to set
     */
    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }
    
    
}
