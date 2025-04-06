/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.si.pojo;

/**
 *
 * @author ttuan
 */
public enum OrderStatus {
    DANG_XU_LY("Đang xử lý"),
    DANG_GIAO("Đang giao"),
    DA_GIAO("Đã giao");
    
    private final String status;

    private OrderStatus(String status) {
       this.status = status;
    }
    
    @Override
    public String toString() {
       return status;
    }
    
    public static OrderStatus converToEnum(String text){
        for (OrderStatus os:OrderStatus.values()){
            if(os.status.equalsIgnoreCase(text))
                return os;
        }
       throw new IllegalArgumentException("Trạng thái đơn hàng không hợp lệ: " + text);
    }
}
