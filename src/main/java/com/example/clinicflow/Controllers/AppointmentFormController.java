package com.example.clinicflow.Controllers;

import com.example.clinicflow.Classes.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AppointmentFormController {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    private Label errorLabel;
    @FXML
    private TextField patientIdField;
    @FXML
    private ComboBox<Doctor> doctorComboBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField timeField;
    @FXML
    private ComboBox<String> typeComboBox;

    private final PatientDAO patientDAO = new PatientDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private boolean saved = false;

    @FXML
    public void initialize() {
        datePicker.setValue(LocalDate.now());
        typeComboBox.setItems(FXCollections.observableArrayList(
                "Consultation", "Follow-up", "Check-up", "Emergency"));

        doctorComboBox.setConverter(new StringConverter<Doctor>() {
            @Override
            public String toString(Doctor doctor) {
                return doctor == null ? "" : doctor.getId() + " - " + doctor.getName();
            }

            @Override
            public Doctor fromString(String string) {
                return null;
            }
        });

        try {
            List<Doctor> doctors = doctorDAO.getAllDoctors();
            doctorComboBox.setItems(FXCollections.observableArrayList(doctors));
        } catch (SQLException e) {
            showError("Could not load doctors: " + e.getMessage());
        }
    }

    public boolean isSaved() {
        return saved;
    }

    @FXML
    private void handleSave() {
        hideError();

        String patientId = patientIdField.getText() == null ? "" : patientIdField.getText().trim();
        Doctor doctor = doctorComboBox.getValue();
        LocalDate date = datePicker.getValue();
        String timeText = timeField.getText() == null ? "" : timeField.getText().trim();
        String type = typeComboBox.getEditor().getText() == null ? "" : typeComboBox.getEditor().getText().trim();

        if (patientId.isEmpty()) {
            showError("Patient ID is required.");
            return;
        }
        if (doctor == null) {
            showError("Please select a doctor.");
            return;
        }
        if (date == null) {
            showError("Please pick a date.");
            return;
        }
        if (type.isEmpty()) {
            showError("Please select or enter an appointment type.");
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
            if (patientDAO.getPatientById(patientId) == null) {
                throw new InvalidPatientException("No patient found with ID " + patientId + ".");
            }

            LocalDateTime dateTime = LocalDateTime.of(date, time);
            Appointment appointment = new Appointment(0, patientId, doctor.getId(), dateTime, type, AppointmentStatus.PENDING);
            appointmentDAO.bookAppointment(appointment);

            saved = true;
            closeWindow();
        } catch (InvalidPatientException e) {
            showError(e.getMessage());
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
        Stage stage = (Stage) patientIdField.getScene().getWindow();
        stage.close();
    }
}