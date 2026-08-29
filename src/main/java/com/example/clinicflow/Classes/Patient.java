package com.example.clinicflow.Classes;

public class Patient extends Person {

    private int age;
    private String address;

    public Patient(String id, String name, int age, String phone, String address) {
        super(id, name, phone);
        this.age = age;
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getRole() {
        return "Patient";
    }

    @Override
    public String toString() {
        return "Patient{id='" + getId() + "', name='" + getName() + "', age=" + age
                + ", phone='" + getPhone() + "', address='" + address + "'}";
    }
}
