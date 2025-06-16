//package com.groupeisi.banque.tests;
//
//import com.groupeisi.banque.services.NotificationService;
//import com.groupeisi.banque.services.EmailService;
//import com.groupeisi.banque.entities.transaction;
//import com.groupeisi.banque.entities.carteBancaire;
//import com.groupeisi.banque.entities.credit;
//import com.groupeisi.banque.entities.ticketSupport;
//import com.groupeisi.banque.entities.client;
//import com.groupeisi.banque.entities.compte;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Date;
//import java.util.concurrent.ExecutorService;
//
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.anyDouble;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class NotificationServiceTest {
//
//    @Mock
//    private EmailService emailService;
//
//    @Mock
//    private ExecutorService executorService;
//
//    private NotificationService notificationService;
//    private client testClient;
//    private compte testCompte;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        notificationService = new NotificationService();
//        notificationService.setEmailService(emailService);
//        notificationService.setExecutorService(executorService);
//
//        // Initialiser le client de test
//        testClient = new client();
//        testClient.setId(1L);
//        testClient.setNom("Doe");
//        testClient.setPrenom("John");
//        testClient.setEmail("john.doe@example.com");
//
//        // Initialiser le compte de test
//        testCompte = new compte();
//        testCompte.setId(1L);
//        testCompte.setNumero("1234567890");
//        testCompte.setSolde(5000.0);
//        testCompte.setClient(testClient);
//    }
//
//    @Test
//    void testNotifyTransactionCreated() {
//        transaction testTransaction = new transaction();
//        testTransaction.setId(1L);
//        testTransaction.setType("VIREMENT");
//        testTransaction.setMontant(1000.0);
//        testTransaction.setDate(new Date().toString());
//        testTransaction.setStatut("EFFECTUE");
//        testTransaction.setCompteSource(testCompte);
//
//        doNothing().when(executorService).execute(any(Runnable.class));
//
//        assertDoesNotThrow(() -> notificationService.notifyTransactionCreated(testTransaction));
//
//        verify(executorService).execute(any(Runnable.class));
//    }
//
//    @Test
//    void testNotifyCardRequestCreated() {
//        carteBancaire testCard = new carteBancaire();
//        testCard.setId(1L);
//        testCard.setNumero("4111111111111111");
//        testCard.setDateExpiration(new Date().toString());
//        testCard.setCompte(testCompte);
//
//        doNothing().when(executorService).execute(any(Runnable.class));
//
//        assertDoesNotThrow(() -> notificationService.notifyCardRequestCreated(testCard));
//
//        verify(executorService).execute(any(Runnable.class));
//    }
//
//    @Test
//    void testNotifyCreditRequestCreated() {
//        credit testCredit = new credit();
//        testCredit.setId(1L);
//        testCredit.setMontant(10000.0);
//        testCredit.setDureeMois(24);
//        testCredit.setTauxInteret(5.5);
//        testCredit.setClient(testClient);
//
//        doNothing().when(executorService).execute(any(Runnable.class));
//
//        assertDoesNotThrow(() -> notificationService.notifyCreditRequestCreated(testCredit));
//
//        verify(executorService).execute(any(Runnable.class));
//    }
//
//    @Test
//    void testNotifyTicketCreated() {
//        ticketSupport testTicket = new ticketSupport();
//        testTicket.setId(1L);
//        testTicket.setNumero("TICKET-123");
//        testTicket.setSujet("Technical Issue");
//        testTicket.setClient(testClient);
//
//        doNothing().when(executorService).execute(any(Runnable.class));
//
//        assertDoesNotThrow(() -> notificationService.notifyTicketCreated(testTicket));
//
//        verify(executorService).execute(any(Runnable.class));
//    }
//
//    @Test
//    void testNotifyNewClientCreated() {
//        doNothing().when(executorService).execute(any(Runnable.class));
//
//        assertDoesNotThrow(() -> notificationService.notifyNewClientCreated(testClient));
//
//        verify(executorService).execute(any(Runnable.class));
//    }
//
//    @Test
//    void testShutdown() {
//        when(executorService.isShutdown()).thenReturn(false);
//        doNothing().when(executorService).shutdown();
//
//        notificationService.shutdown();
//
//        verify(executorService).shutdown();
//    }
//
//    @Test
//    void testHandleNullParameters() {
//        assertThrows(IllegalArgumentException.class,
//            () -> notificationService.notifyTransactionCreated(null));
//
//        assertThrows(IllegalArgumentException.class,
//            () -> notificationService.notifyCardRequestCreated(null));
//
//        assertThrows(IllegalArgumentException.class,
//            () -> notificationService.notifyCreditRequestCreated(null));
//
//        assertThrows(IllegalArgumentException.class,
//            () -> notificationService.notifyTicketCreated(null));
//
//        assertThrows(IllegalArgumentException.class,
//            () -> notificationService.notifyNewClientCreated(null));
//    }
//
//    @Test
//    void testHandleInvalidClientEmail() {
//        testClient.setEmail("invalid.email");
//
//        assertThrows(IllegalArgumentException.class,
//            () -> notificationService.notifyNewClientCreated(testClient));
//    }
//}