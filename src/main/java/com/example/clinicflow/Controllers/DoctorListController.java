package com.example.clinicflow.Controllers;

import com.example.clinicflow.Classes.Doctor;
import com.example.clinicflow.Classes.DoctorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;

public class DoctorListController {

    @FXML
    private TableView<Doctor> doctorTable;
    @FXML
    private TableColumn<Doctor, String> idColumn;
    @FXML
    private TableColumn<Doctor, String> nameColumn;
    @FXML
    private TableColumn<Doctor, String> specialtyColumn;
    @FXML
    private TableColumn<Doctor, String> phoneColumn;

    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final ObservableList<Doctor> doctorList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        specialtyColumn.setCellValueFactory(new PropertyValueFactory<>("specialty"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        doctorTable.setItems(doctorList);
        handleRefresh();
    }

    @FXML
    private void handleAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/clinicflow/view/DoctorFormDialog.fxml"));
            Parent root = loader.load();
            DoctorFormController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Add Doctor");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            if (controller.isSaved()) {
                handleRefresh();
            }
        } catch (Exception e) {
            AlertUtils.showError("Error", "Could not open doctor form:\n" + e.getMessage());
        }
    }

    @FXML
    private void handleRefresh() {
        try {
            doctorList.setAll(doctorDAO.getAllDoctors());
        } catch (SQLException e) {
            AlertUtils.showError("Database Error", "Could not load doctors:\n" + e.getMessage());
        }
    }
}