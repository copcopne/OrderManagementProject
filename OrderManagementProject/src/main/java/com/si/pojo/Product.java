/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.si.pojo;

/**
 *
 * @author ttuan
 */
public class Product {
    private String SKU;
    private String ProductName;
    private int StockQuanity;
    private float UnitPrice;
    private int MinStockLevel;

    public Product(String SKU, String ProductName, int StockQuanity, float UnitPrice, int MinStockLevel) {
        this.SKU = SKU;
        this.ProductName = ProductName;
        this.StockQuanity = StockQuanity;
        this.UnitPrice = UnitPrice;
        this.MinStockLevel = MinStockLevel;
    }
    
    
}
