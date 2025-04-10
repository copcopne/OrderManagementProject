module com.si.ordermanagementproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.si.ordermanagementproject to javafx.fxml;
    exports com.si.ordermanagementproject;
    exports com.si.services;
    exports com.si.pojo;
}