/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestServices;

import com.si.services.ValidationServices;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author copcopne
 */
public class ValidationServicesTest {

    @Test
    public void testValidateExportQuantity_NotThrow() {
        int stock = 30;
        String[] rawRequests = {"30", "1", "18"};
        int[] requests = {30, 1, 18};

        for (String request : rawRequests) {
            assertDoesNotThrow(() -> ValidationServices.validateExportQuantity(request, stock),
                    "Expected không throw ra ngoại lệ (dữ liệu hợp lệ) với request: " + request + "và stock: " + stock);
        }
        for (int request : requests) {
            assertDoesNotThrow(() -> ValidationServices.validateExportQuantity(request, stock),
                    "Expected không throw ra ngoại lệ (dữ liệu hợp lệ) với request: " + request + "và stock: " + stock);
        }
    }

    @Test
    public void testValidateExportQuantity_Throws() {
        int stock = 30;
        String[] invalidRawRequest = {"", "null", "31", "-1"};
        int[] invalidRequests = {-1, 31, 100, -200};

        for (String request : invalidRawRequest) {
            assertThrows(IllegalArgumentException.class, () -> ValidationServices.validateExportQuantity(request, stock),
                    "Expected sẽ throw ra ngoại lệ (dữ liệu không hợp lệ) với request: " + request + "và stock: " + stock);
        }
        for (int request : invalidRequests) {
            assertThrows(IllegalArgumentException.class, () -> ValidationServices.validateExportQuantity(request, stock),
                    "Expected sẽ throw ra ngoại lệ (dữ liệu không hợp lệ) với request: " + request + "và stock: " + stock);
        }
    }

    @Test
    public void testValidateImportQuantity_NotThrow() {
        String[] rawAmountToImports = {"1", "1000", "432"};
        for (String amount : rawAmountToImports) {
            assertDoesNotThrow(() -> ValidationServices.validateImportQuantity(amount),
                    "Expected không throw ra ngoại lệ (dữ liệu hợp lệ) với số lượng nhập: " + amount);
        }
    }

    @Test
    public void testValidateImportQuantity_Throws() {
        String[] invalidAmountToImports = {"-1", "1001", "343434", "-39854"};
        for (String amount : invalidAmountToImports) {
            assertThrows(IllegalArgumentException.class, () -> ValidationServices.validateImportQuantity(amount),
                    "Expected sẽ throw ra ngoại lệ (dữ liệu không hợp lệ) với số lượng nhập: " + amount);
        }
    }

    @Test
    public void testValidateNewProduct_NotThrow() {
        String[] SKUs = {"P100", "SKU123", "PRODUCT1"};
        String[] names = {"Sản Phẩm 1", "Laptop Dell", "Ipad Pro"};
        String[] rawUnitPrices = {"1000", "1000000", "323123"};
        String[] rawMinStocks = {"1", "10", "3"};

        for (int i = 0; i < 3; i++) {
            final int index = i;
            assertDoesNotThrow(() -> ValidationServices.validateNewProduct(SKUs[index], names[index], rawUnitPrices[index], rawMinStocks[index]),
                    "Expected không throw ra ngoại lệ (dữ liệu hợp lệ) với SKU: "
                    + SKUs[index] + ", name: "
                    + names[index] + ", unitPrice: "
                    + rawUnitPrices[index] + ", minStock: "
                    + rawMinStocks[index]);
        }
    }

    @Test
    public void testValidateNewProduct_Throws() {
        String[] SKUs = {"", "Mã SKU", "1' OR '1'='1"};
        String[] names = {"&", "", "😍"};
        String[] rawUnitPrices = {"-1", "aaa", ""};
        String[] rawMinStocks = {"-1", "101", "100trieu"};

        for (int i = 0; i < 3; i++) {
            final int index = i;
            assertThrows(IllegalArgumentException.class ,() -> ValidationServices.validateNewProduct(SKUs[index], names[index], rawUnitPrices[index], rawMinStocks[index]),
                    "Expected không throw ra ngoại lệ (dữ liệu hợp lệ) với SKU: "
                    + SKUs[index] + ", name: "
                    + names[index] + ", unitPrice: "
                    + rawUnitPrices[index] + ", minStock: "
                    + rawMinStocks[index]);
        }
    }
}
