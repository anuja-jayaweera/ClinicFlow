package com.example.clinicflow.Classes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    public void addPatient(Patient patient) throws SQLException {
        String sql = "INSERT INTO patients (patient_id, name, age, phone, address) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patient.getId());
            stmt.setString(2, patient.getName());
            stmt.setInt(3, patient.getAge());
            stmt.setString(4, patient.getPhone());
            stmt.setString(5, patient.getAddress());
            stmt.executeUpdate();
        }
    }

    public Patient getPatientById(String patientId) throws SQLException {
        String sql = "SELECT * FROM patients WHERE patient_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    /** Search by name (partial match) or exact patient ID. */
    public List<Patient> searchPatients(String keyword) throws SQLException {
        List<Patient> results = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE patient_id = ? OR name LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, keyword);
            stmt.setString(2, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        return results;
    }

    public List<Patient> getAllPatients() throws SQLException {
        List<Patient> results = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY name";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        }
        return results;
    }

    public void updatePatient(Patient patient) throws SQLException {
        String sql = "UPDATE patients SET name = ?, age = ?, phone = ?, address = ? WHERE patient_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patient.getName());
            stmt.setInt(2, patient.getAge());
            stmt.setString(3, patient.getPhone());
            stmt.setString(4, patient.getAddress());
            stmt.setString(5, patient.getId());
            stmt.executeUpdate();
        }
    }

    public void deletePatient(String patientId) throws SQLException {
        String sql = "DELETE FROM patients WHERE patient_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patientId);
            stmt.executeUpdate();
        }
    }

    public int countAllPatients() throws SQLException {
        String sql = "SELECT COUNT(*) FROM patients";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private Patient mapRow(ResultSet rs) throws SQLException {
        return new Patient(
                rs.getString("patient_id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("phone"),
                rs.getString("address")
        );
    }
}