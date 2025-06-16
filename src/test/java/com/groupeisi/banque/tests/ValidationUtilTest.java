//package com.groupeisi.banque.tests;
//
//import com.groupeisi.banque.utils.ValidationUtil;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class ValidationUtilTest {
//
//    @Test
//    void testEmailValidation() {
//        // Emails valides
//        assertTrue(ValidationUtil.isValidEmail("user@domain.com"));
//        assertTrue(ValidationUtil.isValidEmail("user.name@domain.co.uk"));
//        assertTrue(ValidationUtil.isValidEmail("user+tag@domain.com"));
//        assertTrue(ValidationUtil.isValidEmail("123@domain.com"));
//
//        // Emails invalides
//        assertFalse(ValidationUtil.isValidEmail(null));
//        assertFalse(ValidationUtil.isValidEmail(""));
//        assertFalse(ValidationUtil.isValidEmail("invalid.email"));
//        assertFalse(ValidationUtil.isValidEmail("user@"));
//        assertFalse(ValidationUtil.isValidEmail("@domain.com"));
//        assertFalse(ValidationUtil.isValidEmail("user@domain"));
//    }
//
//    @Test
//    void testPhoneValidation() {
//        // Numéros valides
//        assertTrue(ValidationUtil.isValidPhone("+33612345678"));
//        assertTrue(ValidationUtil.isValidPhone("0612345678"));
//        assertTrue(ValidationUtil.isValidPhone("06-12-34-56-78"));
//
//        // Numéros invalides
//        assertFalse(ValidationUtil.isValidPhone(null));
//        assertFalse(ValidationUtil.isValidPhone(""));
//        assertFalse(ValidationUtil.isValidPhone("123"));
//        assertFalse(ValidationUtil.isValidPhone("abcdefghij"));
//        assertFalse(ValidationUtil.isValidPhone("+331234"));
//    }
//
//    @Test
//    void testAccountNumberValidation() {
//        // Numéros de compte valides
//        assertTrue(ValidationUtil.isValidAccountNumber("1234567890"));
//        assertTrue(ValidationUtil.isValidAccountNumber("FR7630001007941234567890185"));
//
//        // Numéros de compte invalides
//        assertFalse(ValidationUtil.isValidAccountNumber(null));
//        assertFalse(ValidationUtil.isValidAccountNumber(""));
//        assertFalse(ValidationUtil.isValidAccountNumber("123"));
//        assertFalse(ValidationUtil.isValidAccountNumber("abcdefghij"));
//    }
//
//    @Test
//    void testCardNumberValidation() {
//        // Numéros de carte valides
//        assertTrue(ValidationUtil.isValidCardNumber("4111111111111111")); // Visa
//        assertTrue(ValidationUtil.isValidCardNumber("5555555555554444")); // MasterCard
//        assertTrue(ValidationUtil.isValidCardNumber("378282246310005")); // American Express
//
//        // Numéros de carte invalides
//        assertFalse(ValidationUtil.isValidCardNumber(null));
//        assertFalse(ValidationUtil.isValidCardNumber(""));
//        assertFalse(ValidationUtil.isValidCardNumber("1234"));
//        assertFalse(ValidationUtil.isValidCardNumber("abcdefghijklmnop"));
//        assertFalse(ValidationUtil.isValidCardNumber("1111111111111111")); // Format valide mais numéro invalide
//    }
//
//    @Test
//    void testAmountValidation() {
//        // Montants valides
//        assertTrue(ValidationUtil.isValidAmount(100.0));
//        assertTrue(ValidationUtil.isValidAmount(1.0));
//        assertTrue(ValidationUtil.isValidAmount(999999.99));
//
//        // Montants invalides
//        assertFalse(ValidationUtil.isValidAmount(0.0));
//        assertFalse(ValidationUtil.isValidAmount(-100.0));
//        assertFalse(ValidationUtil.isValidAmount(-0.01));
//    }
//
//    @Test
//    void testDateValidation() {
//        // Dates valides
//        assertTrue(ValidationUtil.isValidDate("2024-03-20"));
//        assertTrue(ValidationUtil.isValidDate("2025-12-31"));
//
//        // Dates invalides
//        assertFalse(ValidationUtil.isValidDate(null));
//        assertFalse(ValidationUtil.isValidDate(""));
//        assertFalse(ValidationUtil.isValidDate("2024/03/20"));
//        assertFalse(ValidationUtil.isValidDate("20-03-2024"));
//        assertFalse(ValidationUtil.isValidDate("2024-13-01")); // Mois invalide
//        assertFalse(ValidationUtil.isValidDate("2024-04-31")); // Jour invalide
//    }
//
//    @Test
//    void testPasswordValidation() {
//        // Mots de passe valides
//        assertTrue(ValidationUtil.isValidPassword("Password123"));
//        assertTrue(ValidationUtil.isValidPassword("Complex@Password123"));
//        assertTrue(ValidationUtil.isValidPassword("Abcd1234!"));
//
//        // Mots de passe invalides
//        assertFalse(ValidationUtil.isValidPassword(null));
//        assertFalse(ValidationUtil.isValidPassword(""));
//        assertFalse(ValidationUtil.isValidPassword("short"));
//        assertFalse(ValidationUtil.isValidPassword("onlylowercase"));
//        assertFalse(ValidationUtil.isValidPassword("ONLYUPPERCASE"));
//        assertFalse(ValidationUtil.isValidPassword("12345678"));
//        assertFalse(ValidationUtil.isValidPassword("NoNumbers"));
//    }
//
//    @Test
//    void testNameValidation() {
//        // Noms valides
//        assertTrue(ValidationUtil.isValidName("John"));
//        assertTrue(ValidationUtil.isValidName("Jean-Pierre"));
//        assertTrue(ValidationUtil.isValidName("O'Connor"));
//        assertTrue(ValidationUtil.isValidName("María José"));
//
//        // Noms invalides
//        assertFalse(ValidationUtil.isValidName(null));
//        assertFalse(ValidationUtil.isValidName(""));
//        assertFalse(ValidationUtil.isValidName("a"));
//        assertFalse(ValidationUtil.isValidName("123"));
//        assertFalse(ValidationUtil.isValidName("John123"));
//    }
//
//    @Test
//    void testCreditAmountValidation() {
//        // Montants de crédit valides
//        assertTrue(ValidationUtil.isValidCreditAmount(1000.0));
//        assertTrue(ValidationUtil.isValidCreditAmount(50000.0));
//        assertTrue(ValidationUtil.isValidCreditAmount(999999.99));
//
//        // Montants de crédit invalides
//        assertFalse(ValidationUtil.isValidCreditAmount(0.0));
//        assertFalse(ValidationUtil.isValidCreditAmount(-1000.0));
//        assertFalse(ValidationUtil.isValidCreditAmount(999.99)); // En dessous du minimum
//        assertFalse(ValidationUtil.isValidCreditAmount(1000000.0)); // Au-dessus du maximum
//    }
//
//    @Test
//    void testCreditDurationValidation() {
//        // Durées valides
//        assertTrue(ValidationUtil.isValidCreditDuration(12));
//        assertTrue(ValidationUtil.isValidCreditDuration(24));
//        assertTrue(ValidationUtil.isValidCreditDuration(36));
//        assertTrue(ValidationUtil.isValidCreditDuration(48));
//        assertTrue(ValidationUtil.isValidCreditDuration(60));
//
//        // Durées invalides
//        assertFalse(ValidationUtil.isValidCreditDuration(0));
//        assertFalse(ValidationUtil.isValidCreditDuration(-12));
//        assertFalse(ValidationUtil.isValidCreditDuration(6)); // En dessous du minimum
//        assertFalse(ValidationUtil.isValidCreditDuration(72)); // Au-dessus du maximum
//    }
//
//    @Test
//    void testInterestRateValidation() {
//        // Taux d'intérêt valides
//        assertTrue(ValidationUtil.isValidInterestRate(2.5));
//        assertTrue(ValidationUtil.isValidInterestRate(5.0));
//        assertTrue(ValidationUtil.isValidInterestRate(10.0));
//
//        // Taux d'intérêt invalides
//        assertFalse(ValidationUtil.isValidInterestRate(0.0));
//        assertFalse(ValidationUtil.isValidInterestRate(-2.5));
//        assertFalse(ValidationUtil.isValidInterestRate(25.0)); // Au-dessus du maximum
//    }
//
//    @Test
//    void testInputSanitization() {
//        // Test de nettoyage des caractères dangereux
//        assertEquals("Hello World",
//            ValidationUtil.sanitizeInput("Hello World"));
//        assertEquals("Hello World",
//            ValidationUtil.sanitizeInput("Hello <script>World</script>"));
//        assertEquals("Hello World",
//            ValidationUtil.sanitizeInput("Hello \"World\""));
//        assertEquals("Hello World",
//            ValidationUtil.sanitizeInput("Hello 'World'"));
//
//        // Test avec null
//        assertNull(ValidationUtil.sanitizeInput(null));
//
//        // Test avec chaîne vide
//        assertEquals("", ValidationUtil.sanitizeInput(""));
//    }
//}