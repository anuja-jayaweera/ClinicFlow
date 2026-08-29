package com.example.clinicflow.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        showDashboard();
    }

    @FXML
    private void showDashboard() {
        loadView("/com/example/clinicflow/view/DashboardView.fxml");
    }

    @FXML
    private void showPatients() {
        loadView("/com/example/clinicflow/view/PatientListView.fxml");
    }

    @FXML
    private void showDoctors() {
        loadView("/com/example/clinicflow/view/DoctorListView.fxml");
    }

    @FXML
    private void showAppointments() {
        loadView("/com/example/clinicflow/view/AppointmentListView.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            AlertUtils.showError("Navigation Error", "Could not load view: " + fxmlPath + "\n" + e.getMessage());
        }
    }
}