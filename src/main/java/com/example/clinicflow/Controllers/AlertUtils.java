package com.example.clinicflow.Controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

public class AlertUtils {

    private AlertUtils() {
    }

    public static void showError(String title, String message) {
        showAndBringToFront(build(Alert.AlertType.ERROR, title, message));
    }

    public static void showInfo(String title, String message) {
        showAndBringToFront(build(Alert.AlertType.INFORMATION, title, message));
    }

    public static boolean confirm(String title, String message) {
        Alert alert = build(Alert.AlertType.CONFIRMATION, title, message);
        Optional<ButtonType> result = showAndBringToFront(alert);
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private static Alert build(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert;
    }

    private static Optional<ButtonType> showAndBringToFront(Alert alert) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.toFront();
        return alert.showAndWait();
    }
}