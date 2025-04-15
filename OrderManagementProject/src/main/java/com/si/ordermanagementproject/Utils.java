/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.si.ordermanagementproject;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

/**
 *
 * @author copcopne
 */
public class Utils {

    public static Alert getAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, content, ButtonType.OK);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
                "-fx-background-color: #ffffff;"
                + "-fx-background-radius: 10px;"
                + "-fx-font-size: 14px;"
                + "-fx-font-family: 'Segoe UI', sans-serif;"
        );

        dialogPane.lookupButton(ButtonType.OK).setStyle(
                "-fx-background-color: #00BFFF;"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 5px;"
                + "-fx-font-weight: bold;"
                + "-fx-cursor: hand;"
        );

        return alert;
    }

    public static Alert getConfirmAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, content, ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Xác nhận");
        alert.setHeaderText(null);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
                "-fx-background-color: #ffffff;"
                + "-fx-background-radius: 10px;"
                + "-fx-font-size: 14px;"
                + "-fx-font-family: 'Segoe UI', sans-serif;"
        );

        dialogPane.lookupButton(ButtonType.OK).setStyle(
                "-fx-background-color: #FFA500;"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 5px;"
                + "-fx-font-weight: bold;"
                + "-fx-cursor: hand;"
        );

        dialogPane.lookupButton(ButtonType.CANCEL).setStyle(
                "-fx-background-color: #e0e0e0;"
                + "-fx-text-fill: #333;"
                + "-fx-background-radius: 5px;"
                + "-fx-cursor: hand;"
        );

        return alert;
    }

}
