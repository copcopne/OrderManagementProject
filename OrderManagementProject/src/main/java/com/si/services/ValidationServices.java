package com.si.services;

public class ValidationServices {

    private static final String SKU_REGEX = "^[a-zA-Z0-9._]+$";
    private static final String PRODUCTNAME_REGEX = "^[a-zA-Z0-9ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠƯàáâãèéêìíòóôõùúăđĩũơưẠ-ỹ\\s]+$";

    public static void validateExportQuantity(String rawRequested, int stock) {
        if (rawRequested == null || rawRequested.isBlank()) {
            throw new IllegalArgumentException("Số lượng xuất không được để trống.");
        }
        int requested;
        try {
            requested = Integer.parseInt(rawRequested.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Số lượng xuất phải là số nguyên hợp lệ.");
        }
        if (requested <= 0) {
            throw new IllegalArgumentException("Số lượng xuất phải lớn hơn 0.");
        }
        if (requested > stock) {
            throw new IllegalArgumentException("Số lượng xuất vượt quá tồn kho (" + stock + ").");
        }
    }

    public static void validateExportQuantity(int requested, int stock) {
        if (requested <= 0) {
            throw new IllegalArgumentException("Số lượng xuất phải lớn hơn 0.");
        }
        if (requested > stock) {
            throw new IllegalArgumentException("Số lượng xuất vượt quá tồn kho (" + stock + ").");
        }
    }

    public static int validateImportQuantity(String rawRequested) {
        if (rawRequested == null || rawRequested.isBlank()) {
            throw new IllegalArgumentException("Số lượng nhập không được để trống.");
        }
        int requested;
        try {
            requested = Integer.parseInt(rawRequested.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Số lượng nhập phải là số nguyên hợp lệ.");
        }
        if (requested <= 0 || requested > 1000) {
            throw new IllegalArgumentException("Số lượng nhập phải nằm trong khoảng cho phép (1-1000).");
        }
        return requested;
    }

    public static void validateNewProduct(String sku, String name, String rawUnitPrice, String rawMinStock) {
        // Validate text fields
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("Mã SKU không được để trống.");
        }
        if(!sku.matches(SKU_REGEX)){
            throw new IllegalArgumentException("Mã SKU chỉ chấp nhận tên không dấu và ký tự '.','_'.");
        }
        if(sku.length() > 50) {
            throw new IllegalArgumentException("Mã SKU không được quá 50 ký tự.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống.");
        }
        if(!name.matches(PRODUCTNAME_REGEX)) {
            throw new IllegalArgumentException("Tên sản phẩm không được chứa ký tự đặc biệt.");
        }
        if(name.length() > 100) {
            throw new IllegalArgumentException("Tên sản phẩm không được quá 100 ký tự.");
        }
        if (rawUnitPrice == null || rawUnitPrice.isBlank()) {
            throw new IllegalArgumentException("Đơn giá không được để trống.");
        }
        if (rawMinStock == null || rawMinStock.isBlank()) {
            throw new IllegalArgumentException("Tồn kho tối thiểu không được để trống.");
        }

        // Parse đơn giá
        float unitPrice;
        try {
            unitPrice = Float.parseFloat(rawUnitPrice.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Đơn giá phải là số thực hợp lệ.");
        }
        if (unitPrice <= 0) {
            throw new IllegalArgumentException("Đơn giá phải lớn hơn 0.");
        }

        // Parse tồn kho tối thiểu
        int minStock;
        try {
            minStock = Integer.parseInt(rawMinStock.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Tồn kho tối thiểu phải là số nguyên hợp lệ.");
        }
        if (minStock <= 0 || minStock > 100) {
            throw new IllegalArgumentException("Tồn kho tối thiểu phải trong khoảng 1–100.");
        }
    }
}
