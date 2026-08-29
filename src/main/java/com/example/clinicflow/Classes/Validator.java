package com.example.clinicflow.Classes;

public class Validator {
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean isValidAge(int age) {
        return age > 0 && age < 130;
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null) {
            return false;
        }

        return phone.matches("^\\+?[0-9]{7,15}$");
    }
}