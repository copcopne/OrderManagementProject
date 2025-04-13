module com.si.ordermanagementproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;

    opens com.si.ordermanagementproject to javafx.fxml;
    exports com.si.ordermanagementproject;
    exports com.si.pojo;
    exports com.si.services;
}