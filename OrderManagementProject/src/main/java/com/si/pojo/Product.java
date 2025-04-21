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
    private String productName;
    private int stockQuantity;
    private float unitPrice;
    private int minStockLevel;

    public Product(String SKU, String ProductName, int StockQuanity, float UnitPrice, int MinStockLevel) {
        this.SKU = SKU;
        this.productName = ProductName;
        this.stockQuantity = StockQuanity;
        this.unitPrice = UnitPrice;
        this.minStockLevel = MinStockLevel;
    }

    /**
     * @return the SKU
     */
    public String getSKU() {
        return SKU;
    }

    /**
     * @return the ProductName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @return the StockQuantity
     */
    public int getStockQuantity() {
        return stockQuantity;
    }

    /**
     * @return the UnitPrice
     */
    public float getUnitPrice() {
        return unitPrice;
    }

    /**
     * @return the MinStockLevel
     */
    public int getMinStockLevel() {
        return minStockLevel;
    }

    @Override
    public String toString() {
        return this.SKU+ " - " + this.productName;
    }
    
    
    
}
