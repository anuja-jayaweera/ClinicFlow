package com.example.clinicflow.Controllers;

import com.example.clinicflow.Classes.Doctor;
import com.example.clinicflow.Classes.DoctorDAO;
import com.example.clinicflow.Classes.Validator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class DoctorFormController {

    @FXML
    private Label errorLabel;
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField specialtyField;
    @FXML
    private TextField phoneField;

    private final DoctorDAO doctorDAO = new DoctorDAO();
    private boolean saved = false;

    public boolean isSaved() {
        return saved;
    }

    @FXML
    private void handleSave() {
        hideError();

        String id = idField.getText() == null ? "" : idField.getText().trim();
        String name = nameField.getText() == null ? "" : nameField.getText().trim();
        String specialty = specialtyField.getText() == null ? "" : specialtyField.getText().trim();
        String phone = phoneField.getText() == null ? "" : phoneField.getText().trim();

        if (id.isEmpty()) {
            showError("Doctor ID is required.");
            return;
        }
        if (!Validator.isValidName(name)) {
            showError("Please enter a valid name.");
            return;
        }
        if (specialty.isEmpty()) {
            showError("Specialty is required.");
            return;
        }
        if (!Validator.isValidPhone(phone)) {
            showError("Please enter a valid phone number.");
            return;
        }

        try {
            doctorDAO.addDoctor(new Doctor(id, name, phone, specialty));
            saved = true;
            closeWindow();
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
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.close();
    }
}