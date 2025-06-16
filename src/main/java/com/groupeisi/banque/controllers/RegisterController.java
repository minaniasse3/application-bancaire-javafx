package com.groupeisi.banque.controllers;

import com.groupeisi.banque.App;
import com.groupeisi.banque.entities.client;
import com.groupeisi.banque.entities.compte;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import jakarta.persistence.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RegisterController {
    @FXML
    private TextField nomField;
    
    @FXML
    private TextField prenomField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField telephoneField;
    
    @FXML
    private TextArea adresseField;
    
    @FXML
    private RadioButton compteCourantRadio;
    
    @FXML
    private RadioButton compteEpargneRadio;
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private Button registerButton;
    
    @FXML
    private Button cancelButton;
    
    private EntityManagerFactory emf;
    private EntityManager em;
    
    @FXML
    public void initialize() {
        try {
            emf = Persistence.createEntityManagerFactory("banquePU");
            em = emf.createEntityManager();
        } catch (Exception e) {
            System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
            System.out.println("Fonctionnement en mode simulation (sans base de données)");
            // Mode simulation pour le développement/test
        }
    }
    
    @FXML
    public void handleRegister() {
        if (!validateFields()) {
            return;
        }
        
        try {
            // Créer un nouveau client
            client newClient = new client();
            newClient.setNom(nomField.getText().trim());
            newClient.setPrenom(prenomField.getText().trim());
            newClient.setEmail(emailField.getText().trim());
            newClient.setTelephone(telephoneField.getText().trim());
            newClient.setAdresse(adresseField.getText().trim());
            newClient.setDateInscription(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            newClient.setStatut("Actif");
            newClient.setComptes(new ArrayList<>());
            newClient.setCredits(new ArrayList<>());
            
            // Créer un nouveau compte
            compte newCompte = new compte();
            newCompte.setNumero(generateAccountNumber());
            newCompte.setType(compteCourantRadio.isSelected() ? "courant" : "epargne");
            newCompte.setSolde(0.0);
            newCompte.setDateCreation(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            newCompte.setClient(newClient);
            newCompte.setTransactions(new ArrayList<>());
            newCompte.setCartes(new ArrayList<>());
            
            // Ajouter le compte au client
            newClient.getComptes().add(newCompte);
            
            // Simulation de persistance pour le mode test
            if (em != null) {
                try {
                    em.getTransaction().begin();
                    em.persist(newClient);
                    em.getTransaction().commit();
                } catch (Exception e) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    throw e;
                }
            }
            
            showAlert(Alert.AlertType.INFORMATION, "Inscription réussie", 
                    "Votre compte a été créé avec succès.\nNuméro de compte : " + newCompte.getNumero());
                    
            // Naviguer vers la page de connexion
            navigateToLogin();
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            showAlert(Alert.AlertType.ERROR, "Erreur d'inscription", 
                    "Une erreur est survenue lors de l'inscription : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    public void handleCancel() {
        try {
            navigateToLogin();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                    "Impossible de retourner à la page de connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private boolean validateFields() {
        // Vérification des champs obligatoires
        if (nomField.getText().trim().isEmpty() || 
            prenomField.getText().trim().isEmpty() || 
            emailField.getText().trim().isEmpty() || 
            telephoneField.getText().trim().isEmpty() || 
            adresseField.getText().trim().isEmpty() || 
            usernameField.getText().trim().isEmpty() || 
            passwordField.getText().trim().isEmpty() || 
            confirmPasswordField.getText().trim().isEmpty()) {
            
            showAlert(Alert.AlertType.ERROR, "Erreur d'inscription", 
                    "Veuillez remplir tous les champs obligatoires.");
            return false;
        }
        
        // Vérification du format d'email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!emailField.getText().matches(emailRegex)) {
            showAlert(Alert.AlertType.ERROR, "Erreur d'inscription", 
                    "Veuillez entrer une adresse email valide.");
            return false;
        }
        
        // Vérification de correspondance des mots de passe
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Erreur d'inscription", 
                    "Les mots de passe ne correspondent pas.");
            return false;
        }
        
        return true;
    }
    
    private String generateAccountNumber() {
        // Génération d'un numéro de compte au format: FR76-YYYY-XXXX-XXXX
        // où YYYY est l'année courante et X sont des chiffres aléatoires
        String year = String.valueOf(LocalDate.now().getYear());
        String random = String.format("%08d", (int)(Math.random() * 100000000));
        return "FR76-" + year + "-" + random.substring(0, 4) + "-" + random.substring(4, 8);
    }
    
    private void navigateToLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("login-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.setTitle("Connexion - Système Bancaire");
        stage.setScene(scene);
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 