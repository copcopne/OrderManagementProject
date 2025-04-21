/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.si.services;

/**
 *
 * @author copcopne
 */
public class ValidationServices {

    private static final String SKU_REGEX = "^[a-zA-Z0-9._]+$";
    private static final String PRODUCTNAME_REGEX = "^[0-9aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTuUùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴzZ\\s]+$";

    private static final String USERNAME_REGEX = "^[a-zA-Z0-9._]+$";
    private static final String FULLNAME_REGEX = "^[aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTuUùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴzZ\\s]+$";

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
        if (!sku.matches(SKU_REGEX)) {
            throw new IllegalArgumentException("Mã SKU chỉ chấp nhận chữ không dấu và ký tự '.','_'.");
        }
        if (sku.length() > 8) {
            throw new IllegalArgumentException("Mã SKU không được quá 8 ký tự.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống.");
        }
        if (!name.matches(PRODUCTNAME_REGEX)) {
            throw new IllegalArgumentException("Tên sản phẩm không được chứa ký tự đặc biệt.");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("Tên sản phẩm không được quá 50 ký tự.");
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

    public static void validatePassword(String password) throws IllegalArgumentException {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Mật khẩu phải từ 8 ký tự trở lên.");
        }
        if (password.length() > 255) {
            throw new IllegalArgumentException("Mật khẩu không được quá 255 ký tự.");
        }
    }

    public static void validateUserData(String username, String fullName) throws IllegalArgumentException {
        if (username != null) {
            if (username.isBlank()) {
                throw new IllegalArgumentException("Tên người dùng không được để trống!");
            }

            if (username.length() < 6 || username.length() > 20) {
                throw new IllegalArgumentException("Tên người dùng phải nằm trong khoảng (6-20) ký tự!");
            }

            if (!username.matches(USERNAME_REGEX)) {
                throw new IllegalArgumentException("Tên người dùng không được chứa ký tự đặc biệt!");
            }
        }
        if (fullName != null) {
            if (fullName.isBlank()) {
                throw new IllegalArgumentException("Tên không được để trống!");
            }

            if (fullName.length() > 50) {
                throw new IllegalArgumentException("Tên không được quá 50 ký tự!");
            }

            if (!fullName.trim().matches(FULLNAME_REGEX)) {
                throw new IllegalArgumentException("Tên không được chứa ký tự đặc biệt!");
            }
        }
    }
}
