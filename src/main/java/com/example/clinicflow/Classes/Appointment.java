package com.example.clinicflow.Classes;

import java.time.LocalDateTime;

public class Appointment {

    private int appointmentId;
    private String patientId;
    private String doctorId;
    private LocalDateTime dateTime;
    private String type;
    private AppointmentStatus status;

    public Appointment(int appointmentId, String patientId, String doctorId,
                       LocalDateTime dateTime, String type, AppointmentStatus status) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
        this.type = type;
        this.status = status;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Appointment{id=" + appointmentId + ", patientId='" + patientId
                + "', doctorId='" + doctorId + "', dateTime=" + dateTime
                + ", type='" + type + "', status=" + status + "}";
    }
}