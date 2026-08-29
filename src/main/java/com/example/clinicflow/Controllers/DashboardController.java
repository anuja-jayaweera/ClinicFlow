package com.example.clinicflow.Controllers;

import com.example.clinicflow.Classes.AppointmentDAO;
import com.example.clinicflow.Classes.AppointmentStatus;
import com.example.clinicflow.Classes.PatientDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.SQLException;

public class DashboardController {

    @FXML
    private Label totalPatientsLabel;
    @FXML
    private Label pendingTodayLabel;
    @FXML
    private Label completedTodayLabel;
    @FXML
    private Label cancelledTodayLabel;

    private final PatientDAO patientDAO = new PatientDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    @FXML
    public void initialize() {
        refreshStats();
    }

    @FXML
    private void handleRefresh() {
        refreshStats();
    }

    private void refreshStats() {
        try {
            totalPatientsLabel.setText(String.valueOf(patientDAO.countAllPatients()));
            pendingTodayLabel.setText(String.valueOf(appointmentDAO.countTodayByStatus(AppointmentStatus.PENDING)));
            completedTodayLabel.setText(String.valueOf(appointmentDAO.countTodayByStatus(AppointmentStatus.COMPLETED)));
            cancelledTodayLabel.setText(String.valueOf(appointmentDAO.countTodayByStatus(AppointmentStatus.CANCELLED)));
        } catch (SQLException e) {
            AlertUtils.showError("Database Error", "Could not load dashboard stats:\n" + e.getMessage());
        }
    }
}