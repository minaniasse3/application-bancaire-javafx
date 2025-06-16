package com.groupeisi.banque.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.groupeisi.banque.App;
import com.groupeisi.banque.entities.client;

import jakarta.persistence.*;
import java.io.IOException;
import java.util.List;

public class LoginController {
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Button registerButton;
    
    private EntityManagerFactory emf;
    private EntityManager em;
    private boolean dbAvailable = false;
    
    @FXML
    public void initialize() {
        // Initialiser l'EntityManager
        try {
            emf = Persistence.createEntityManagerFactory("banquePU");
            em = emf.createEntityManager();
            dbAvailable = true;
            System.out.println("Connexion à la base de données établie avec succès.");
        } catch (Exception e) {
            System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
            System.out.println("Fonctionnement en mode simulation (sans base de données)");
            dbAvailable = false;
        }
    }
    
    @FXML
    public void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        
        System.out.println("Tentative de connexion - Username: " + username);
        
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion", 
                    "Veuillez remplir tous les champs.");
            return;
        }
        
        try {
            boolean loginSuccess = false;
            
            if (dbAvailable) {
                System.out.println("Mode avec base de données activé");
                // Mode avec base de données
                try {
                    // Vérifier d'abord si c'est un administrateur
                    TypedQuery<client> queryAdmin = em.createQuery(
                        "SELECT c FROM client c WHERE c.email = :email AND UPPER(c.statut) = 'ADMIN'", 
                        client.class);
                    queryAdmin.setParameter("email", username);
                    List<client> admins = queryAdmin.getResultList();
                    
                    if (!admins.isEmpty()) {
                        // C'est un administrateur
                        loginSuccess = true;
                        System.out.println("Connexion administrateur réussie depuis la BDD : " + username);
                        showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", 
                                "Vous êtes maintenant connecté en tant qu'administrateur.");
                        loadAdminDashboard();
                        return;
                    } else {
                        System.out.println("Pas un administrateur, vérification si client...");
                        // Si ce n'est pas un administrateur, vérifier si c'est un client
                        TypedQuery<client> queryClient = em.createQuery(
                            "SELECT c FROM client c WHERE c.email = :email AND UPPER(c.statut) = 'ACTIF'", 
                            client.class);
                        queryClient.setParameter("email", username);
                        List<client> clients = queryClient.getResultList();
                        
                        if (!clients.isEmpty()) {
                            // C'est un client
                            loginSuccess = true;
                            System.out.println("Connexion client réussie depuis la BDD : " + username);
                            showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", 
                                    "Vous êtes maintenant connecté en tant que client.");
                            loadClientDashboard();
                            return;
                        } else {
                            System.out.println("Aucun utilisateur trouvé avec cet email : " + username);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Erreur lors de la requête à la base de données : " + e.getMessage());
                    e.printStackTrace();
                    // Passer en mode simulation en cas d'erreur
                    dbAvailable = false;
                }
            }
            
            // Si la base de données n'est pas disponible ou si la connexion a échoué, essayer le mode simulation
            if (!dbAvailable && !loginSuccess) {
                // Mode test sans base de données
                if (username.equals("admin") && 
                    (password.equals("admin") || password.equals("admin123"))) {
                    // C'est un administrateur en mode simulation
                    loginSuccess = true;
                    
                    System.out.println("Connexion administrateur réussie en mode simulation : " + username);
                    showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", 
                            "Vous êtes maintenant connecté en tant qu'administrateur (mode simulation).");
                            
                    // Rediriger vers le dashboard admin
                    loadAdminDashboard();
                } else {
                    // Si ce n'est pas un administrateur, on considère que c'est un client en mode simulation
                    loginSuccess = true;
                    
                    System.out.println("Connexion client réussie en mode simulation : " + username);
                    showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", 
                            "Vous êtes maintenant connecté en tant que client (mode simulation).");
                    
                    // Rediriger vers le dashboard client
                    loadClientDashboard();
                }
            }
            
            if (!loginSuccess) {
                showAlert(Alert.AlertType.ERROR, "Erreur de connexion", 
                        "Identifiants incorrects. Veuillez réessayer.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur système", 
                    "Une erreur est survenue : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    public void handleRegister() {
        try {
            // Charger la vue d'inscription
            loadRegistrationView();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                    "Impossible de charger la page d'inscription : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadAdminDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("admin-dashboard.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setTitle("Administration - Système Bancaire");
        stage.setScene(scene);
    }
    
    private void loadClientDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("client-dashboard.fxml"));
        Scene scene = new Scene(loader.load());
        
        // Récupérer le contrôleur
        ClientDashboardController controller = loader.getController();
        
        // Si nous sommes en mode base de données, récupérer le client connecté
        if (dbAvailable) {
            try {
                TypedQuery<client> query = em.createQuery(
                    "SELECT c FROM client c WHERE c.email = :email", 
                    client.class);
                query.setParameter("email", usernameField.getText().trim());
                List<client> clients = query.getResultList();
                
                if (!clients.isEmpty()) {
                    // Définir le client connecté
                    controller.setCurrentClient(clients.get(0));
                    controller.setDbAvailable(true);
                }
            } catch (Exception e) {
                System.out.println("Erreur lors de la récupération du client : " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            // En mode simulation, créer un client de test
            client testClient = new client();
            testClient.setId(1L);
            testClient.setNom("Dupont");
            testClient.setPrenom("Jean");
            testClient.setEmail(usernameField.getText().trim());
            testClient.setStatut("ACTIF");
            testClient.setTelephone("0123456789");
            testClient.setAdresse("123 Rue de Test");
            testClient.setDateInscription(java.time.LocalDate.now().toString());
            
            // Définir le client connecté
            controller.setCurrentClient(testClient);
            controller.setDbAvailable(false);
        }
        
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setTitle("Espace Client - Système Bancaire");
        stage.setScene(scene);
    }
    
    private void loadRegistrationView() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("register-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.setTitle("Inscription - Système Bancaire");
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