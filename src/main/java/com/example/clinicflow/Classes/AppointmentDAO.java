package com.example.clinicflow.Classes;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    /** Books an appointment after checking the doctor isn't already booked at that time. */
    public void bookAppointment(Appointment appt) throws SQLException, DoubleBookingException {
        if (isDoctorBooked(appt.getDoctorId(), appt.getDateTime())) {
            throw new DoubleBookingException(
                    "Doctor " + appt.getDoctorId() + " already has an appointment at " + appt.getDateTime());
        }

        String sql = "INSERT INTO appointments (patient_id, doctor_id, appt_datetime, appt_type, status) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, appt.getPatientId());
            stmt.setString(2, appt.getDoctorId());
            stmt.setTimestamp(3, Timestamp.valueOf(appt.getDateTime()));
            stmt.setString(4, appt.getType());
            stmt.setString(5, appt.getStatus().name());
            stmt.executeUpdate();
        }
    }

    private boolean isDoctorBooked(String doctorId, LocalDateTime dateTime) throws SQLException {
        String sql = "SELECT COUNT(*) FROM appointments "
                + "WHERE doctor_id = ? AND appt_datetime = ? AND status = 'PENDING'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doctorId);
            stmt.setTimestamp(2, Timestamp.valueOf(dateTime));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public List<Appointment> getTodaysAppointments() throws SQLException {
        List<Appointment> results = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE DATE(appt_datetime) = ? ORDER BY appt_datetime";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        return results;
    }

    public void cancelAppointment(int appointmentId) throws SQLException {
        updateStatus(appointmentId, AppointmentStatus.CANCELLED);
    }

    public void completeAppointment(int appointmentId) throws SQLException {
        updateStatus(appointmentId, AppointmentStatus.COMPLETED);
    }

    public void rescheduleAppointment(int appointmentId, LocalDateTime newDateTime)
            throws SQLException, DoubleBookingException {
        String getDoctorSql = "SELECT doctor_id FROM appointments WHERE appointment_id = ?";
        String doctorId;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getDoctorSql)) {
            stmt.setInt(1, appointmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Appointment not found: " + appointmentId);
                }
                doctorId = rs.getString("doctor_id");
            }
        }

        if (isDoctorBooked(doctorId, newDateTime)) {
            throw new DoubleBookingException(
                    "Doctor " + doctorId + " already has an appointment at " + newDateTime);
        }

        String sql = "UPDATE appointments SET appt_datetime = ? WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(newDateTime));
            stmt.setInt(2, appointmentId);
            stmt.executeUpdate();
        }
    }

    private void updateStatus(int appointmentId, AppointmentStatus status) throws SQLException {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setInt(2, appointmentId);
            stmt.executeUpdate();
        }
    }

    /** Dashboard helper: counts today's appointments grouped by status. */
    public int countTodayByStatus(AppointmentStatus status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM appointments WHERE DATE(appt_datetime) = ? AND status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setString(2, status.name());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    private Appointment mapRow(ResultSet rs) throws SQLException {
        return new Appointment(
                rs.getInt("appointment_id"),
                rs.getString("patient_id"),
                rs.getString("doctor_id"),
                rs.getTimestamp("appt_datetime").toLocalDateTime(),
                rs.getString("appt_type"),
                AppointmentStatus.valueOf(rs.getString("status"))
        );
    }
}