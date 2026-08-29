package com.example.clinicflow.Controllers;

import com.example.clinicflow.Classes.AppointmentDAO;
import com.example.clinicflow.Classes.DoubleBookingException;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class RescheduleController {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    private Label errorLabel;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField timeField;

    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private int appointmentId;
    private boolean saved = false;

    /** Call before showing the dialog. */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
        datePicker.setValue(LocalDate.now());
    }

    public boolean isSaved() {
        return saved;
    }

    @FXML
    private void handleSave() {
        hideError();

        LocalDate date = datePicker.getValue();
        String timeText = timeField.getText() == null ? "" : timeField.getText().trim();

        if (date == null) {
            showError("Please pick a date.");
            return;
        }

        LocalTime time;
        try {
            time = LocalTime.parse(timeText, TIME_FORMAT);
        } catch (DateTimeParseException e) {
            showError("Time must be in HH:mm format, e.g. 14:30.");
            return;
        }

        try {
            LocalDateTime newDateTime = LocalDateTime.of(date, time);
            appointmentDAO.rescheduleAppointment(appointmentId, newDateTime);
            saved = true;
            closeWindow();
        } catch (DoubleBookingException e) {
            showError(e.getMessage());
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void hideError() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    private void closeWindow() {
        Stage stage = (Stage) timeField.getScene().getWindow();
        stage.close();
    }
}