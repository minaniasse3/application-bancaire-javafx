package com.groupeisi.banque.services;

import com.groupeisi.banque.entities.client;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SecurityService {
    private static final String SECRET_KEY = "votre_clé_secrète_très_longue_et_complexe";
    private static final long EXPIRATION_TIME = 86400000; // 24 heures en millisecondes

    public static String hashPassword(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide");
        }
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        if (plainTextPassword == null || plainTextPassword.isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide");
        }
        if (hashedPassword == null || hashedPassword.isEmpty()) {
            throw new IllegalArgumentException("Le hash du mot de passe ne peut pas être vide");
        }
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

    public static String generateToken(client user) {
        if (user == null) {
            throw new IllegalArgumentException("L'utilisateur ne peut pas être null");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("L'email de l'utilisateur ne peut pas être vide");
        }
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", "CLIENT");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static Claims validateToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Le token ne peut pas être vide");
        }
        
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean isTokenExpired(String token) {
        if (token == null || token.isEmpty()) {
            return true;
        }
        
        try {
            Claims claims = validateToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        // Supprimer les caractères dangereux
        return input.replaceAll("[<>\"'&]", "");
    }

    public static boolean validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(emailRegex);
    }

    public static boolean validatePassword(String password) {
        // Au moins 8 caractères, une majuscule, une minuscule, un chiffre
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        return password != null && password.matches(passwordRegex);
    }

    public static void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0");
        }
    }

    public static void validateAccountNumber(String accountNumber) {
        if (accountNumber == null || !accountNumber.matches("^[0-9]{10}$")) {
            throw new IllegalArgumentException("Numéro de compte invalide");
        }
    }
} 