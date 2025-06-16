package com.groupeisi.banque.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@(.+)$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[0-9]{10}$"
    );
    
    private static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile(
        "^[0-9]{10}$"
    );
    
    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile(
        "^[0-9]{16}$"
    );
    
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
    
    public static boolean isValidAccountNumber(String accountNumber) {
        return accountNumber != null && ACCOUNT_NUMBER_PATTERN.matcher(accountNumber).matches();
    }
    
    public static boolean isValidCardNumber(String cardNumber) {
        return cardNumber != null && CARD_NUMBER_PATTERN.matcher(cardNumber).matches();
    }
    
    public static boolean isValidAmount(double amount) {
        return amount > 0 && amount <= 1000000; // Limite à 1 million
    }
    
    public static boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpperCase = true;
            if (Character.isLowerCase(c)) hasLowerCase = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        
        return hasUpperCase && hasLowerCase && hasDigit;
    }
    
    public static boolean isValidName(String name) {
        return name != null && name.length() >= 2 && name.matches("^[A-Za-zÀ-ÿ\\s-]+$");
    }
    
    public static boolean isValidCreditAmount(double amount) {
        return amount >= 1000 && amount <= 100000; // Entre 1000XOF et 100000XOF
    }
    
    public static boolean isValidCreditDuration(int months) {
        return months >= 6 && months <= 120; // Entre 6 mois et 10 ans
    }
    
    public static boolean isValidInterestRate(double rate) {
        return rate > 0 && rate <= 20; // Taux entre 0% et 20%
    }
    
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        // Supprimer les caractères dangereux
        return input.replaceAll("[<>\"'&]", "");
    }
    
    /**
     * Formate un montant en chaîne de caractères avec le symbole monétaire
     * @param amount Le montant à formater
     * @return Le montant formaté
     */
    public static String formatAmount(double amount) {
        return String.format("%.2f XOF", amount);
    }
    
    public static String formatDate(String isoDate) {
        try {
            LocalDate date = LocalDate.parse(isoDate);
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            return isoDate;
        }
    }
    
    public static String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 16) {
            return cardNumber;
        }
        return cardNumber.substring(0, 4) + " XXXX XXXX " + cardNumber.substring(12);
    }

    /**
     * Vérifie si un montant est valide pour un transfert
     * @param amount Le montant à vérifier
     * @return vrai si le montant est valide
     */
    public static boolean isValidTransferAmount(double amount) {
        // Entre 1000XOF et 100000XOF
        return amount >= 1000 && amount <= 100000;
    }
} 