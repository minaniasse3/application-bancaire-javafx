package com.groupeisi.banque.services;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import com.groupeisi.banque.utils.ValidationUtil;

public class EmailService {
    private static final String FROM_EMAIL = "votre-email@gmail.com";
    private static final String EMAIL_PASSWORD = "votre-mot-de-passe-application";
    
    // Pour permettre l'injection dans les tests
    private static Session session;
    
    public static void setSession(Session testSession) {
        session = testSession;
    }

    private static Properties getEmailProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        return props;
    }

    private static Session getEmailSession() {
        if (session != null) {
            return session;
        }
        
        return Session.getInstance(getEmailProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        });
    }

    public static void sendEmail(String to, String subject, String content) {
        // Validation des paramètres
        if (to == null || to.isEmpty()) {
            throw new IllegalArgumentException("L'adresse email du destinataire ne peut pas être vide");
        }
        
        if (!ValidationUtil.isValidEmail(to)) {
            throw new IllegalArgumentException("L'adresse email du destinataire est invalide");
        }
        
        if (subject == null || subject.isEmpty()) {
            throw new IllegalArgumentException("Le sujet de l'email ne peut pas être vide");
        }
        
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Le contenu de l'email ne peut pas être vide");
        }
        
        try {
            // Créer une nouvelle instance de MimeMessage en utilisant le constructeur
            MimeMessage message = new MimeMessage(getEmailSession());
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }

    public static void sendWelcomeEmail(String to, String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Le nom du client ne peut pas être vide");
        }
        
        String subject = "Bienvenue sur notre système bancaire";
        String content = "Cher(e) " + name + ",\n\n" +
                "Nous vous souhaitons la bienvenue sur notre système bancaire. " +
                "Votre compte a été créé avec succès.\n\n" +
                "Vous pouvez maintenant vous connecter et profiter de nos services.\n\n" +
                "Cordialement,\n" +
                "L'équipe de votre banque";
        
        sendEmail(to, subject, content);
    }

    public static void sendTransactionNotification(String to, String type, double amount, String accountNumber) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Le type de transaction ne peut pas être vide");
        }
        
        if (amount <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0");
        }
        
        if (accountNumber == null || accountNumber.isEmpty()) {
            throw new IllegalArgumentException("Le numéro de compte ne peut pas être vide");
        }
        
        String subject = "Notification de transaction";
        String content = "Une transaction de type " + type + " d'un montant de " + amount + 
                " € a été effectuée sur votre compte " + accountNumber + ".\n\n" +
                "Si vous n'êtes pas à l'origine de cette transaction, veuillez nous contacter immédiatement.\n\n" +
                "Cordialement,\n" +
                "L'équipe de votre banque";
        
        sendEmail(to, subject, content);
    }

    public static void sendCardRequestConfirmation(String to, String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            throw new IllegalArgumentException("Le numéro de carte ne peut pas être vide");
        }
        
        String subject = "Confirmation de demande de carte bancaire";
        String content = "Votre demande de carte bancaire a été enregistrée.\n\n" +
                "Numéro de carte (masqué) : " + cardNumber + "\n\n" +
                "Vous recevrez votre carte et votre code PIN par courrier séparé.\n\n" +
                "Cordialement,\n" +
                "L'équipe de votre banque";
        
        sendEmail(to, subject, content);
    }

    public static void sendCreditRequestConfirmation(String to, double amount, int duration) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0");
        }
        
        if (duration <= 0) {
            throw new IllegalArgumentException("La durée doit être supérieure à 0");
        }
        
        String subject = "Confirmation de demande de crédit";
        String content = "Votre demande de crédit a été enregistrée.\n\n" +
                "Montant : " + amount + " €\n" +
                "Durée : " + duration + " mois\n\n" +
                "Nous étudions votre dossier et vous recontacterons dans les plus brefs délais.\n\n" +
                "Cordialement,\n" +
                "L'équipe de votre banque";
        
        sendEmail(to, subject, content);
    }

    public static void sendSupportTicketConfirmation(String to, String ticketId, String subject) {
        if (ticketId == null || ticketId.isEmpty()) {
            throw new IllegalArgumentException("L'identifiant du ticket ne peut pas être vide");
        }
        
        if (subject == null || subject.isEmpty()) {
            throw new IllegalArgumentException("Le sujet du ticket ne peut pas être vide");
        }
        
        String content = "Votre ticket de support a été créé avec succès.\n\n" +
                "Référence : " + ticketId + "\n" +
                "Sujet : " + subject + "\n\n" +
                "Un conseiller vous répondra dans les plus brefs délais.\n\n" +
                "Cordialement,\n" +
                "L'équipe de votre banque";
        
        sendEmail(to, "Confirmation de votre ticket de support", content);
    }
} 