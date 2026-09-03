package com.example.clinicflow.Classes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidatorTest {

    // ---------- isValidName ----------

    @Test
    @DisplayName("Valid name returns true")
    void isValidName_validName_returnsTrue() {
        assertTrue(Validator.isValidName("John Doe"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   "})
    @DisplayName("Null, empty, or blank name returns false")
    void isValidName_nullEmptyOrBlank_returnsFalse(String name) {
        assertFalse(Validator.isValidName(name));
    }

    @Test
    @DisplayName("Name with leading/trailing spaces but real content returns true")
    void isValidName_paddedName_returnsTrue() {
        assertTrue(Validator.isValidName("  Jane  "));
    }

    // ---------- isValidAge ----------

    @ParameterizedTest
    @ValueSource(ints = {1, 30, 129})
    @DisplayName("Age within valid range returns true")
    void isValidAge_withinRange_returnsTrue(int age) {
        assertTrue(Validator.isValidAge(age));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 130, 200})
    @DisplayName("Age at or outside boundaries returns false")
    void isValidAge_outOfRange_returnsFalse(int age) {
        assertFalse(Validator.isValidAge(age));
    }

    // ---------- isValidPhone ----------

    @ParameterizedTest
    @ValueSource(strings = {"0771234567", "+94771234567", "1234567", "123456789012345"})
    @DisplayName("Valid phone formats return true")
    void isValidPhone_validFormats_returnsTrue(String phone) {
        assertTrue(Validator.isValidPhone(phone));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
            "",                     // empty
            "12345",                // too short (below 7 digits)
            "1234567890123456",     // too long (above 15 digits)
            "07AB123456",           // contains letters
            "077-123-4567",         // contains dashes
            "++94771234567"         // double plus sign
    })
    @DisplayName("Null, malformed, or out-of-range phone numbers return false")
    void isValidPhone_invalidFormats_returnsFalse(String phone) {
        assertFalse(Validator.isValidPhone(phone));
    }

    @ParameterizedTest
    @CsvSource({
            "0771234567, true",
            "+94771234567, true",
            "123, false",
            "abcdefgh, false"
    })
    @DisplayName("Phone validity matches expected result across mixed inputs")
    void isValidPhone_mixedInputs_matchesExpected(String phone, boolean expected) {
        assertTrue(Validator.isValidPhone(phone) == expected);
    }
}