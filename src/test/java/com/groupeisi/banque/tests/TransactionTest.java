//package com.groupeisi.banque.tests;
//
//import com.groupeisi.banque.entities.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.time.LocalDate;
//
//public class TransactionTest {
//    private transaction testTransaction;
//    private compte sourceAccount;
//    private compte destAccount;
//    private client testClient;
//
//    @BeforeEach
//    void setUp() {
//        // Créer un client de test
//        testClient = new client();
//        testClient.setId(1L);
//        testClient.setNom("Dupont");
//        testClient.setPrenom("Jean");
//        testClient.setEmail("jean.dupont@test.com");
//
//        // Créer un compte source
//        sourceAccount = new compte();
//        sourceAccount.setId(1L);
//        sourceAccount.setNumero("1234567890");
//        sourceAccount.setSolde(1000.0);
//        sourceAccount.setClient(testClient);
//
//        // Créer un compte destination
//        destAccount = new compte();
//        destAccount.setId(2L);
//        destAccount.setNumero("0987654321");
//        destAccount.setSolde(500.0);
//        destAccount.setClient(testClient);
//
//        // Créer une transaction de test
//        testTransaction = new transaction();
//        testTransaction.setId(1L);
//        testTransaction.setType("virement");
//        testTransaction.setMontant(100.0);
//        testTransaction.setDate(LocalDate.now().toString());
//        testTransaction.setStatut("en_attente");
//        testTransaction.setMotif("Test de virement");
//        testTransaction.setCompteSource(sourceAccount);
//        testTransaction.setCompteDest(destAccount);
//    }
//
//    @Test
//    void testTransactionCreation() {
//        assertNotNull(testTransaction);
//        assertEquals(1L, testTransaction.getId());
//        assertEquals("virement", testTransaction.getType());
//        assertEquals(100.0, testTransaction.getMontant());
//        assertEquals("en_attente", testTransaction.getStatut());
//        assertEquals("Test de virement", testTransaction.getMotif());
//    }
//
//    @Test
//    void testTransactionAccounts() {
//        assertNotNull(testTransaction.getCompteSource());
//        assertNotNull(testTransaction.getCompteDest());
//        assertEquals("1234567890", testTransaction.getCompteSource().getNumero());
//        assertEquals("0987654321", testTransaction.getCompteDest().getNumero());
//    }
//
//    @Test
//    void testTransactionValidation() {
//        // Vérifier que le montant est positif
//        assertTrue(testTransaction.getMontant() > 0);
//
//        // Vérifier que le compte source a suffisamment de fonds
//        assertTrue(testTransaction.getCompteSource().getSolde() >= testTransaction.getMontant());
//
//        // Vérifier que les comptes source et destination sont différents
//        assertNotEquals(
//            testTransaction.getCompteSource().getNumero(),
//            testTransaction.getCompteDest().getNumero()
//        );
//    }
//
//    @Test
//    void testTransactionExecution() {
//        double sourceInitialBalance = sourceAccount.getSolde();
//        double destInitialBalance = destAccount.getSolde();
//        double amount = testTransaction.getMontant();
//
//        // Simuler l'exécution de la transaction
//        sourceAccount.setSolde(sourceInitialBalance - amount);
//        destAccount.setSolde(destInitialBalance + amount);
//        testTransaction.setStatut("validé");
//
//        // Vérifier les nouveaux soldes
//        assertEquals(sourceInitialBalance - amount, sourceAccount.getSolde());
//        assertEquals(destInitialBalance + amount, destAccount.getSolde());
//        assertEquals("validé", testTransaction.getStatut());
//    }
//
//    @Test
//    void testTransactionMotif() {
//        // Test avec un motif valide
//        testTransaction.setMotif("Remboursement");
//        assertEquals("Remboursement", testTransaction.getMotif());
//
//        // Test avec un motif null
//        testTransaction.setMotif(null);
//        assertNull(testTransaction.getMotif());
//
//        // Test avec un motif vide
//        testTransaction.setMotif("");
//        assertEquals("", testTransaction.getMotif());
//    }
//
//    @Test
//    void testTransactionDate() {
//        String today = LocalDate.now().toString();
//        assertEquals(today, testTransaction.getDate());
//    }
//}