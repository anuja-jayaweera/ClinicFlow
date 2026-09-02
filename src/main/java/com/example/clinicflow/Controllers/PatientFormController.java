package com.example.clinicflow.Controllers;

import com.example.clinicflow.Classes.InvalidPatientException;
import com.example.clinicflow.Classes.Patient;
import com.example.clinicflow.Classes.PatientDAO;
import com.example.clinicflow.Classes.Validator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class PatientFormController {

    @FXML
    private Label formTitleLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;

    private final PatientDAO patientDAO = new PatientDAO();
    private Patient editingPatient;
    private boolean saved = false;


    public void setPatient(Patient patient) {
        this.editingPatient = patient;
        formTitleLabel.setText("Edit Patient");
        idField.setText(patient.getId());
        idField.setDisable(true);
        nameField.setText(patient.getName());
        ageField.setText(String.valueOf(patient.getAge()));
        phoneField.setText(patient.getPhone());
        addressField.setText(patient.getAddress());
    }

    public boolean isSaved() {
        return saved;
    }

    @FXML
    private void handleSave() {
        hideError();
        try {
            Patient patient = buildAndValidatePatient();

            if (editingPatient == null) {
                patientDAO.addPatient(patient);
            } else {
                patientDAO.updatePatient(patient);
            }

            saved = true;
            closeWindow();
        } catch (InvalidPatientException e) {
            showError(e.getMessage());
        } catch (NumberFormatException e) {
            showError("Age must be a whole number.");
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private Patient buildAndValidatePatient() throws InvalidPatientException {
        String id = idField.getText() == null ? "" : idField.getText().trim();
        String name = nameField.getText() == null ? "" : nameField.getText().trim();
        String phone = phoneField.getText() == null ? "" : phoneField.getText().trim();
        String address = addressField.getText() == null ? "" : addressField.getText().trim();
        String ageText = ageField.getText() == null ? "" : ageField.getText().trim();

        if (id.isEmpty()) {
            throw new InvalidPatientException("Patient ID is required.");
        }
        if (!Validator.isValidName(name)) {
            throw new InvalidPatientException("Please enter a valid name.");
        }
        if (!Validator.isValidPhone(phone)) {
            throw new InvalidPatientException("Please enter a valid phone number.");
        }
        if (address.isEmpty()) {
            throw new InvalidPatientException("Address is required.");
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            throw new InvalidPatientException("Age must be a whole number.");
        }
        if (!Validator.isValidAge(age)) {
            throw new InvalidPatientException("Age must be between 1 and 129.");
        }

        return new Patient(id, name, age, phone, address);
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