package com.si.pojo;

public enum OrderStatus {
    ALL(""),
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

