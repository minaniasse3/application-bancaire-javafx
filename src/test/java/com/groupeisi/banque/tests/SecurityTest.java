//package com.groupeisi.banque.tests;
//
//import com.groupeisi.banque.entities.client;
//import com.groupeisi.banque.services.SecurityService;
//import com.groupeisi.banque.utils.ValidationUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class SecurityTest {
//    private client testClient;
//    private String plainPassword;
//
//    @BeforeEach
//    void setUp() {
//        testClient = new client();
//        testClient.setId(1L);
//        testClient.setEmail("test@example.com");
//        plainPassword = "TestPassword123";
//    }
//
//    @Test
//    void testPasswordHashing() {
//        String hashedPassword = SecurityService.hashPassword(plainPassword);
//
//        // Vérifier que le hash n'est pas null ou vide
//        assertNotNull(hashedPassword);
//        assertFalse(hashedPassword.isEmpty());
//
//        // Vérifier que le hash est différent du mot de passe en clair
//        assertNotEquals(plainPassword, hashedPassword);
//
//        // Vérifier que la vérification fonctionne
//        assertTrue(SecurityService.checkPassword(plainPassword, hashedPassword));
//
//        // Vérifier qu'un mauvais mot de passe ne fonctionne pas
//        assertFalse(SecurityService.checkPassword("WrongPassword123", hashedPassword));
//    }
//
//    @Test
//    void testTokenGeneration() {
//        String token = SecurityService.generateToken(testClient);
//
//        // Vérifier que le token n'est pas null ou vide
//        assertNotNull(token);
//        assertFalse(token.isEmpty());
//
//        // Vérifier que le token est valide
//        assertFalse(SecurityService.isTokenExpired(token));
//
//        // Vérifier les claims du token
//        var claims = SecurityService.validateToken(token);
//        assertEquals(testClient.getId(), claims.get("id", Long.class));
//        assertEquals(testClient.getEmail(), claims.get("email", String.class));
//        assertEquals("CLIENT", claims.get("role", String.class));
//    }
//
//    @Test
//    void testInputSanitization() {
//        String dangerousInput = "<script>alert('XSS')</script>";
//        String sanitizedInput = SecurityService.sanitizeInput(dangerousInput);
//
//        // Vérifier que les caractères dangereux sont supprimés
//        assertFalse(sanitizedInput.contains("<"));
//        assertFalse(sanitizedInput.contains(">"));
//        assertFalse(sanitizedInput.contains("'"));
//        assertFalse(sanitizedInput.contains("\""));
//
//        // Vérifier que null reste null
//        assertNull(SecurityService.sanitizeInput(null));
//    }
//
//    @Test
//    void testEmailValidation() {
//        // Emails valides
//        assertTrue(SecurityService.validateEmail("user@domain.com"));
//        assertTrue(SecurityService.validateEmail("user.name@domain.co.uk"));
//        assertTrue(SecurityService.validateEmail("user+tag@domain.com"));
//
//        // Emails invalides
//        assertFalse(SecurityService.validateEmail(null));
//        assertFalse(SecurityService.validateEmail(""));
//        assertFalse(SecurityService.validateEmail("invalid.email"));
//        assertFalse(SecurityService.validateEmail("user@"));
//        assertFalse(SecurityService.validateEmail("@domain.com"));
//    }
//
//    @Test
//    void testPasswordValidation() {
//        // Mots de passe valides
//        assertTrue(SecurityService.validatePassword("Password123"));
//        assertTrue(SecurityService.validatePassword("Complex@Password123"));
//
//        // Mots de passe invalides
//        assertFalse(SecurityService.validatePassword(null));
//        assertFalse(SecurityService.validatePassword(""));
//        assertFalse(SecurityService.validatePassword("short"));
//        assertFalse(SecurityService.validatePassword("onlylowercase"));
//        assertFalse(SecurityService.validatePassword("ONLYUPPERCASE"));
//        assertFalse(SecurityService.validatePassword("12345678"));
//        assertFalse(SecurityService.validatePassword("NoNumbers"));
//    }
//
//    @Test
//    void testAmountValidation() {
//        // Montants valides
//        assertDoesNotThrow(() -> SecurityService.validateAmount(100.0));
//        assertDoesNotThrow(() -> SecurityService.validateAmount(1.0));
//
//        // Montants invalides
//        assertThrows(IllegalArgumentException.class, () -> SecurityService.validateAmount(0.0));
//        assertThrows(IllegalArgumentException.class, () -> SecurityService.validateAmount(-100.0));
//    }
//
//    @Test
//    void testAccountNumberValidation() {
//        // Numéros de compte valides
//        assertDoesNotThrow(() -> SecurityService.validateAccountNumber("1234567890"));
//
//        // Numéros de compte invalides
//        assertThrows(IllegalArgumentException.class, () -> SecurityService.validateAccountNumber(null));
//        assertThrows(IllegalArgumentException.class, () -> SecurityService.validateAccountNumber("123"));
//        assertThrows(IllegalArgumentException.class, () -> SecurityService.validateAccountNumber("abcdefghij"));
//    }
//}