package com.example.clinicflow.Classes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppointmentTest {

    private static final LocalDateTime SAMPLE_DATETIME = LocalDateTime.of(2026, 9, 10, 14, 30);

    @Test
    @DisplayName("Constructor sets all fields, accessible via getters")
    void constructor_setsAllFields() {
        Appointment appt = new Appointment(1, "P001", "D001", SAMPLE_DATETIME, "Checkup", AppointmentStatus.PENDING);

        assertEquals(1, appt.getAppointmentId());
        assertEquals("P001", appt.getPatientId());
        assertEquals("D001", appt.getDoctorId());
        assertEquals(SAMPLE_DATETIME, appt.getDateTime());
        assertEquals("Checkup", appt.getType());
        assertEquals(AppointmentStatus.PENDING, appt.getStatus());
    }

    @Test
    @DisplayName("setAppointmentId updates the appointment id")
    void setAppointmentId_updatesId() {
        Appointment appt = new Appointment(1, "P001", "D001", SAMPLE_DATETIME, "Checkup", AppointmentStatus.PENDING);

        appt.setAppointmentId(2);

        assertEquals(2, appt.getAppointmentId());
    }

    @Test
    @DisplayName("setPatientId updates the patient id")
    void setPatientId_updatesPatientId() {
        Appointment appt = new Appointment(1, "P001", "D001", SAMPLE_DATETIME, "Checkup", AppointmentStatus.PENDING);

        appt.setPatientId("P999");

        assertEquals("P999", appt.getPatientId());
    }

    @Test
    @DisplayName("setDoctorId updates the doctor id")
    void setDoctorId_updatesDoctorId() {
        Appointment appt = new Appointment(1, "P001", "D001", SAMPLE_DATETIME, "Checkup", AppointmentStatus.PENDING);

        appt.setDoctorId("D999");

        assertEquals("D999", appt.getDoctorId());
    }

    @Test
    @DisplayName("setDateTime updates the appointment date and time")
    void setDateTime_updatesDateTime() {
        Appointment appt = new Appointment(1, "P001", "D001", SAMPLE_DATETIME, "Checkup", AppointmentStatus.PENDING);
        LocalDateTime newDateTime = LocalDateTime.of(2026, 12, 25, 9, 0);

        appt.setDateTime(newDateTime);

        assertEquals(newDateTime, appt.getDateTime());
    }

    @Test
    @DisplayName("setType updates the appointment type")
    void setType_updatesType() {
        Appointment appt = new Appointment(1, "P001", "D001", SAMPLE_DATETIME, "Checkup", AppointmentStatus.PENDING);

        appt.setType("Follow-up");

        assertEquals("Follow-up", appt.getType());
    }

    @Test
    @DisplayName("setStatus updates the appointment status")
    void setStatus_updatesStatus() {
        Appointment appt = new Appointment(1, "P001", "D001", SAMPLE_DATETIME, "Checkup", AppointmentStatus.PENDING);

        appt.setStatus(AppointmentStatus.COMPLETED);

        assertEquals(AppointmentStatus.COMPLETED, appt.getStatus());
    }

    @Test
    @DisplayName("toString includes all field values")
    void toString_includesAllFields() {
        Appointment appt = new Appointment(5, "P010", "D020", SAMPLE_DATETIME, "Consultation", AppointmentStatus.CANCELLED);

        String result = appt.toString();

        String expected = "Appointment{id=5, patientId='P010', doctorId='D020', dateTime=" + SAMPLE_DATETIME
                + ", type='Consultation', status=CANCELLED}";
        assertEquals(expected, result);
    }
}