//package com.groupeisi.banque.tests;
//
//import com.groupeisi.banque.services.EmailService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import jakarta.mail.Session;
//import jakarta.mail.Transport;
//import jakarta.mail.Message;
//import jakarta.mail.internet.MimeMessage;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class EmailServiceTest {
//
//    @Mock
//    private Session session;
//
//    @Mock
//    private Transport transport;
//
//    @Mock
//    private MimeMessage mimeMessage;
//
//    private EmailService emailService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        emailService = new EmailService();
//        // Injecter le mock de session dans EmailService
//        EmailService.setSession(session);
//    }
//
//    @Test
//    void testSendEmail() throws Exception {
//        String to = "test@example.com";
//        String subject = "Test Subject";
//        String content = "Test Content";
//
//        // Configurer les mocks
//        when(session.getTransport()).thenReturn(transport);
//        // Utiliser doReturn pour éviter l'appel réel au constructeur MimeMessage
//        doReturn(mimeMessage).when(session).getTransport("smtp");
//
//        // Test l'envoi d'un email simple
//        assertDoesNotThrow(() -> EmailService.sendEmail(to, subject, content));
//
//        // Vérifier que le transport a été utilisé
//        verify(transport, atLeastOnce()).connect(anyString(), anyString(), anyString());
//        verify(transport, atLeastOnce()).sendMessage(any(Message.class), any());
//        verify(transport, atLeastOnce()).close();
//    }
//
//    @Test
//    void testSendWelcomeEmail() throws Exception {
//        String to = "newclient@example.com";
//        String name = "John Doe";
//
//        // Configurer les mocks
//        when(session.getTransport()).thenReturn(transport);
//        doReturn(mimeMessage).when(session).getTransport("smtp");
//
//        assertDoesNotThrow(() -> EmailService.sendWelcomeEmail(to, name));
//
//        verify(transport, atLeastOnce()).connect(anyString(), anyString(), anyString());
//        verify(transport, atLeastOnce()).sendMessage(any(Message.class), any());
//        verify(transport, atLeastOnce()).close();
//    }
//
//    @Test
//    void testSendTransactionNotification() throws Exception {
//        String to = "client@example.com";
//        String type = "VIREMENT";
//        double amount = 1000.0;
//        String accountNumber = "1234567890";
//
//        // Configurer les mocks
//        when(session.getTransport()).thenReturn(transport);
//        doReturn(mimeMessage).when(session).getTransport("smtp");
//
//        assertDoesNotThrow(() -> EmailService.sendTransactionNotification(to, type, amount, accountNumber));
//
//        verify(transport, atLeastOnce()).connect(anyString(), anyString(), anyString());
//        verify(transport, atLeastOnce()).sendMessage(any(Message.class), any());
//        verify(transport, atLeastOnce()).close();
//    }
//
//    @Test
//    void testSendCardRequestConfirmation() throws Exception {
//        String to = "client@example.com";
//        String cardNumber = "4111111111111111";
//
//        // Configurer les mocks
//        when(session.getTransport()).thenReturn(transport);
//        doReturn(mimeMessage).when(session).getTransport("smtp");
//
//        assertDoesNotThrow(() -> EmailService.sendCardRequestConfirmation(to, cardNumber));
//
//        verify(transport, atLeastOnce()).connect(anyString(), anyString(), anyString());
//        verify(transport, atLeastOnce()).sendMessage(any(Message.class), any());
//        verify(transport, atLeastOnce()).close();
//    }
//
//    @Test
//    void testSendCreditRequestConfirmation() throws Exception {
//        String to = "client@example.com";
//        double amount = 10000.0;
//        int duration = 24;
//
//        // Configurer les mocks
//        when(session.getTransport()).thenReturn(transport);
//        doReturn(mimeMessage).when(session).getTransport("smtp");
//
//        assertDoesNotThrow(() -> EmailService.sendCreditRequestConfirmation(to, amount, duration));
//
//        verify(transport, atLeastOnce()).connect(anyString(), anyString(), anyString());
//        verify(transport, atLeastOnce()).sendMessage(any(Message.class), any());
//        verify(transport, atLeastOnce()).close();
//    }
//
//    @Test
//    void testSendSupportTicketConfirmation() throws Exception {
//        String to = "client@example.com";
//        String ticketId = "TICKET-123";
//        String subject = "Technical Issue";
//
//        // Configurer les mocks
//        when(session.getTransport()).thenReturn(transport);
//        doReturn(mimeMessage).when(session).getTransport("smtp");
//
//        assertDoesNotThrow(() -> EmailService.sendSupportTicketConfirmation(to, ticketId, subject));
//
//        verify(transport, atLeastOnce()).connect(anyString(), anyString(), anyString());
//        verify(transport, atLeastOnce()).sendMessage(any(Message.class), any());
//        verify(transport, atLeastOnce()).close();
//    }
//
//    @Test
//    void testInvalidEmailAddress() {
//        String invalidEmail = "invalid.email";
//        String subject = "Test Subject";
//        String content = "Test Content";
//
//        assertThrows(IllegalArgumentException.class,
//            () -> EmailService.sendEmail(invalidEmail, subject, content));
//    }
//
//    @Test
//    void testNullOrEmptyParameters() {
//        // Test avec email null
//        assertThrows(IllegalArgumentException.class,
//            () -> EmailService.sendEmail(null, "subject", "content"));
//
//        // Test avec sujet null
//        assertThrows(IllegalArgumentException.class,
//            () -> EmailService.sendEmail("test@example.com", null, "content"));
//
//        // Test avec contenu null
//        assertThrows(IllegalArgumentException.class,
//            () -> EmailService.sendEmail("test@example.com", "subject", null));
//
//        // Test avec email vide
//        assertThrows(IllegalArgumentException.class,
//            () -> EmailService.sendEmail("", "subject", "content"));
//    }
//}