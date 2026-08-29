package com.example.clinicflow.Controllers;

import com.example.clinicflow.Classes.Appointment;
import com.example.clinicflow.Classes.AppointmentDAO;
import com.example.clinicflow.Classes.AppointmentStatus;
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
import java.time.format.DateTimeFormatter;

public class AppointmentListController {

    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, Integer> idColumn;
    @FXML
    private TableColumn<Appointment, String> patientIdColumn;
    @FXML
    private TableColumn<Appointment, String> doctorIdColumn;
    @FXML
    private TableColumn<Appointment, String> dateTimeColumn;
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    @FXML
    private TableColumn<Appointment, AppointmentStatus> statusColumn;

    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        doctorIdColumn.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        dateTimeColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getDateTime().format(DISPLAY_FORMAT)));

        appointmentTable.setItems(appointmentList);
        handleRefresh();
    }

    @FXML
    private void handleBook() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/clinicflow/view/AppointmentFormDialog.fxml"));
            Parent root = loader.load();
            AppointmentFormController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Book Appointment");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            if (controller.isSaved()) {
                handleRefresh();
            }
        } catch (Exception e) {
            AlertUtils.showError("Error", "Could not open booking form:\n" + e.getMessage());
        }
    }

    @FXML
    private void handleReschedule() {
        Appointment selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showError("No Selection", "Please select an appointment to reschedule.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/clinicflow/view/RescheduleDialog.fxml"));
            Parent root = loader.load();
            RescheduleController controller = loader.getController();
            controller.setAppointmentId(selected.getAppointmentId());

            Stage stage = new Stage();
            stage.setTitle("Reschedule Appointment");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            if (controller.isSaved()) {
                handleRefresh();
            }
        } catch (Exception e) {
            AlertUtils.showError("Error", "Could not open reschedule form:\n" + e.getMessage());
        }
    }

    @FXML
    private void handleComplete() {
        Appointment selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showError("No Selection", "Please select an appointment to mark completed.");
            return;
        }
        try {
            appointmentDAO.completeAppointment(selected.getAppointmentId());
            handleRefresh();
        } catch (SQLException e) {
            AlertUtils.showError("Database Error", "Could not update appointment:\n" + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        Appointment selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showError("No Selection", "Please select an appointment to cancel.");
            return;
        }
        boolean confirmed = AlertUtils.confirm("Confirm Cancel",
                "Cancel appointment #" + selected.getAppointmentId() + "?");
        if (!confirmed) {
            return;
        }
        try {
            appointmentDAO.cancelAppointment(selected.getAppointmentId());
            handleRefresh();
        } catch (SQLException e) {
            AlertUtils.showError("Database Error", "Could not cancel appointment:\n" + e.getMessage());
        }
    }

    @FXML
    private void handleRefresh() {
        try {
            appointmentList.setAll(appointmentDAO.getTodaysAppointments());
        } catch (SQLException e) {
            AlertUtils.showError("Database Error", "Could not load appointments:\n" + e.getMessage());
        }
    }
}