package com.example.clinicflow.Classes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonTest {


    @Test
    @DisplayName("Patient constructor sets fields accessible via Person getters")
    void patient_constructor_setsPersonFields() {
        Patient patient = new Patient("P001", "Alice Perera", 30, "0771234567", "123 Main St");

        assertEquals("P001", patient.getId());
        assertEquals("Alice Perera", patient.getName());
        assertEquals("0771234567", patient.getPhone());
    }

    @Test
    @DisplayName("Doctor constructor sets fields accessible via Person getters")
    void doctor_constructor_setsPersonFields() {
        Doctor doctor = new Doctor("D001", "Dr. Silva", "0779876543", "Cardiology");

        assertEquals("D001", doctor.getId());
        assertEquals("Dr. Silva", doctor.getName());
        assertEquals("0779876543", doctor.getPhone());
    }

    @Test
    @DisplayName("Setters update Person fields for a Patient instance")
    void personSetters_updateFields_onPatient() {
        Patient patient = new Patient("P002", "Bob", 40, "0770000000", "Old Address");

        patient.setId("P002-X");
        patient.setName("Bobby");
        patient.setPhone("0771111111");

        assertEquals("P002-X", patient.getId());
        assertEquals("Bobby", patient.getName());
        assertEquals("0771111111", patient.getPhone());
    }

    @Test
    @DisplayName("getRole returns 'Patient' for a Patient instance, even via a Person reference")
    void getRole_returnsPatient_forPatientInstance() {
        Person person = new Patient("P003", "Cara", 25, "0772222222", "Some Address");

        assertEquals("Patient", person.getRole());
    }

    @Test
    @DisplayName("getRole returns 'Doctor' for a Doctor instance, even via a Person reference")
    void getRole_returnsDoctor_forDoctorInstance() {
        Person person = new Doctor("D002", "Dr. Fernando", "0773333333", "Neurology");

        assertEquals("Doctor", person.getRole());
    }

    @Test
    @DisplayName("Person.toString includes role, id, name, and phone")
    void toString_includesRoleIdNameAndPhone() {
        Person person = new TestPerson("P004", "Dilani", "0774444444");

        String result = person.toString();

        assertEquals("TestRole{id='P004', name='Dilani', phone='0774444444'}", result);
    }

    private static class TestPerson extends Person {
        TestPerson(String id, String name, String phone) {
            super(id, name, phone);
        }

        @Override
        public String getRole() {
            return "TestRole";
        }
    }

    @Test
    @DisplayName("Doctor.toString overrides Person.toString and includes specialty")
    void doctorToString_overridesPersonToString_includesSpecialty() {
        Doctor doctor = new Doctor("D003", "Dr. Kumar", "0775555555", "Pediatrics");

        String result = doctor.toString();

        assertEquals("Doctor{id='D003', name='Dr. Kumar', specialty='Pediatrics', phone='0775555555'}", result);
    }
}