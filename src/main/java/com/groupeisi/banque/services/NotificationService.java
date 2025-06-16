package com.groupeisi.banque.services;

import com.groupeisi.banque.entities.*;
import com.groupeisi.banque.utils.ValidationUtil;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationService {
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);
    private static EmailService emailService = new EmailService();
    
    // Pour les tests
    public static void setExecutorService(ExecutorService testExecutorService) {
        executorService = testExecutorService;
    }
    
    public static void setEmailService(EmailService testEmailService) {
        emailService = testEmailService;
    }

    public static void notifyTransactionCreated(transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("La transaction ne peut pas être null");
        }
        
        executorService.submit(() -> {
            try {
                client client = transaction.getCompte().getClient();
                String accountNumber = transaction.getCompte().getNumero();
                
                if (client == null || client.getEmail() == null || !ValidationUtil.isValidEmail(client.getEmail())) {
                    throw new IllegalArgumentException("L'email du client est invalide");
                }
                
                EmailService.sendTransactionNotification(
                    client.getEmail(),
                    transaction.getType(),
                    transaction.getMontant(),
                    accountNumber
                );
            } catch (Exception e) {
                e.printStackTrace();
                // Log l'erreur mais ne la propage pas pour ne pas bloquer l'application
            }
        });
    }

    public static void notifyCardRequestCreated(carteBancaire card) {
        if (card == null) {
            throw new IllegalArgumentException("La carte ne peut pas être null");
        }
        
        executorService.submit(() -> {
            try {
                client client = card.getCompte().getClient();
                String maskedCardNumber = maskCardNumber(card.getNumero());
                
                if (client == null || client.getEmail() == null || !ValidationUtil.isValidEmail(client.getEmail())) {
                    throw new IllegalArgumentException("L'email du client est invalide");
                }
                
                EmailService.sendCardRequestConfirmation(
                    client.getEmail(),
                    maskedCardNumber
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void notifyCreditRequestCreated(credit credit) {
        if (credit == null) {
            throw new IllegalArgumentException("Le crédit ne peut pas être null");
        }
        
        executorService.submit(() -> {
            try {
                client client = credit.getClient();
                
                if (client == null || client.getEmail() == null || !ValidationUtil.isValidEmail(client.getEmail())) {
                    throw new IllegalArgumentException("L'email du client est invalide");
                }
                
                EmailService.sendCreditRequestConfirmation(
                    client.getEmail(),
                    credit.getMontant(),
                    credit.getDuree_mois()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void notifyTicketCreated(ticketSupport ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Le ticket ne peut pas être null");
        }
        
        executorService.submit(() -> {
            try {
                client client = ticket.getClient();
                
                if (client == null || client.getEmail() == null || !ValidationUtil.isValidEmail(client.getEmail())) {
                    throw new IllegalArgumentException("L'email du client est invalide");
                }
                
                EmailService.sendSupportTicketConfirmation(
                    client.getEmail(),
                    ticket.getId().toString(),
                    ticket.getSujet()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void notifyNewClientCreated(client client) {
        if (client == null) {
            throw new IllegalArgumentException("Le client ne peut pas être null");
        }
        
        if (client.getEmail() == null || !ValidationUtil.isValidEmail(client.getEmail())) {
            throw new IllegalArgumentException("L'email du client est invalide");
        }
        
        executorService.submit(() -> {
            try {
                EmailService.sendWelcomeEmail(
                    client.getEmail(),
                    client.getPrenom() + " " + client.getNom()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 16) {
            return cardNumber;
        }
        return cardNumber.substring(0, 4) + " XXXX XXXX " + cardNumber.substring(12);
    }

    // Méthode à appeler lors de l'arrêt de l'application
    public static void shutdown() {
        executorService.shutdown();
    }
} 