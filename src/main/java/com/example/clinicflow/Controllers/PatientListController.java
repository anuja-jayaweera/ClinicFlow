package com.example.clinicflow.Controllers;

import com.example.clinicflow.Classes.AppointmentDAO;
import com.example.clinicflow.Classes.Patient;
import com.example.clinicflow.Classes.PatientDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class PatientListController {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<Patient> patientTable;
    @FXML
    private TableColumn<Patient, String> idColumn;
    @FXML
    private TableColumn<Patient, String> nameColumn;
    @FXML
    private TableColumn<Patient, Integer> ageColumn;
    @FXML
    private TableColumn<Patient, String> phoneColumn;
    @FXML
    private TableColumn<Patient, String> addressColumn;

    private final PatientDAO patientDAO = new PatientDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final ObservableList<Patient> patientList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        patientTable.setItems(patientList);
        handleRefresh();
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText() == null ? "" : searchField.getText().trim();
        if (keyword.isEmpty()) {
            handleRefresh();
            return;
        }
        try {
            List<Patient> results = patientDAO.searchPatients(keyword);
            patientList.setAll(results);
        } catch (SQLException e) {
            AlertUtils.showError("Database Error", "Search failed:\n" + e.getMessage());
        }
    }

    @FXML
    private void handleClearSearch() {
        searchField.clear();
        handleRefresh();
    }

    @FXML
    private void handleAdd() {
        openForm(null);
    }

    @FXML
    private void handleEdit() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showError("No Selection", "Please select a patient to edit.");
            return;
        }
        openForm(selected);
    }

    @FXML
    private void handleDelete() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showError("No Selection", "Please select a patient to delete.");
            return;
        }

        try {
            int appointmentCount = appointmentDAO.countByPatient(selected.getId());

            String message;
            if (appointmentCount > 0) {
                message = "Delete patient " + selected.getId() + " - " + selected.getName() + "?\n\n"
                        + "This patient has " + appointmentCount
                        + " appointment(s) on record. Deleting this patient will also permanently delete "
                        + "all of their appointment history.";
            } else {
                message = "Delete patient " + selected.getId() + " - " + selected.getName() + "?";
            }

            boolean confirmed = AlertUtils.confirm("Confirm Delete", message);
            if (!confirmed) {
                return;
            }

            if (appointmentCount > 0) {
                appointmentDAO.deleteAppointmentsByPatient(selected.getId());
            }
            patientDAO.deletePatient(selected.getId());
            handleRefresh();
        } catch (SQLException e) {
            AlertUtils.showError("Database Error", "Could not delete patient:\n" + e.getMessage());
        }
    }

    @FXML
    private void handleRefresh() {
        try {
            patientList.setAll(patientDAO.getAllPatients());
        } catch (SQLException e) {
            AlertUtils.showError("Database Error", "Could not load patients:\n" + e.getMessage());
        }
    }

    private void openForm(Patient patientToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/clinicflow/view/PatientFormDialog.fxml"));
            Parent root = loader.load();

            PatientFormController controller = loader.getController();
            if (patientToEdit != null) {
                controller.setPatient(patientToEdit);
            }

            Stage stage = new Stage();
            stage.setTitle(patientToEdit == null ? "Add Patient" : "Edit Patient");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();

            if (controller.isSaved()) {
                handleRefresh();
            }
        } catch (Exception e) {
            AlertUtils.showError("Error", "Could not open patient form:\n" + e.getMessage());
        }
    }
}