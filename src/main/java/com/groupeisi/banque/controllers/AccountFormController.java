package com.groupeisi.banque.controllers;

import com.groupeisi.banque.entities.client;
import com.groupeisi.banque.entities.compte;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AccountFormController {
    @FXML
    private Label formTitleLabel;
    
    @FXML
    private TextField numeroField;
    
    @FXML
    private ComboBox<String> typeComboBox;
    
    @FXML
    private TextField soldeField;
    
    @FXML
    private ComboBox<client> clientComboBox;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cancelButton;
    
    private Stage dialogStage;
    private compte compte;
    private boolean newAccount = true;
    private EntityManager em;
    private boolean dbAvailable = false;
    
    /**
     * Initialise le contrôleur.
     */
    @FXML
    private void initialize() {
        // Déjà initialisé dans le FXML (typeComboBox)
    }
    
    /**
     * Charge la liste des clients pour le ComboBox.
     */
    public void loadClients() {
        ObservableList<client> clientsList = FXCollections.observableArrayList();
        
        if (dbAvailable && em != null) {
            try {
                TypedQuery<client> query = em.createQuery("SELECT c FROM client c", client.class);
                List<client> clients = query.getResultList();
                clientsList.addAll(clients);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", 
                        "Impossible de charger la liste des clients : " + e.getMessage());
            }
        } else {
            // Mode simulation - ajouter des clients fictifs
            client client1 = new client();
            client1.setId(1L);
            client1.setNom("Dupont");
            client1.setPrenom("Jean");
            
            client client2 = new client();
            client2.setId(2L);
            client2.setNom("Martin");
            client2.setPrenom("Sophie");
            
            clientsList.addAll(client1, client2);
        }
        
        clientComboBox.setItems(clientsList);
        
        // Configurer l'affichage des clients dans le ComboBox
        clientComboBox.setCellFactory(param -> new ListCell<client>() {
            @Override
            protected void updateItem(client item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNom() + " " + item.getPrenom());
                }
            }
        });
        
        // Configurer l'affichage du client sélectionné
        clientComboBox.setButtonCell(new ListCell<client>() {
            @Override
            protected void updateItem(client item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNom() + " " + item.getPrenom());
                }
            }
        });
    }
    
    /**
     * Configure le contrôleur avec une référence à la fenêtre de dialogue.
     * 
     * @param dialogStage la fenêtre de dialogue
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    /**
     * Configure le compte à éditer ou laisse les champs vides si c'est un nouveau compte.
     * 
     * @param compte le compte à éditer ou null pour un nouveau compte
     */
    public void setCompte(compte compte) {
        this.compte = compte;
        
        if (compte != null) {
            // C'est une édition de compte existant
            formTitleLabel.setText("Modifier le compte");
            
            // Remplir les champs avec les valeurs du compte
            numeroField.setText(compte.getNumero());
            typeComboBox.setValue(compte.getType());
            soldeField.setText(String.valueOf(compte.getSolde()));
            
            // Sélectionner le client
            if (compte.getClient() != null) {
                clientComboBox.setValue(compte.getClient());
            }
        } else {
            // C'est un nouveau compte
            formTitleLabel.setText("Ajouter un compte");
            
            // Générer un numéro IBAN français aléatoire pour le nouveau compte
            // Format: FR76 + 23 chiffres
            StringBuilder ibanBuilder = new StringBuilder("FR76");
            for (int i = 0; i < 23; i++) {
                ibanBuilder.append((int) (Math.random() * 10));
            }
            numeroField.setText(ibanBuilder.toString());
            
            // Valeurs par défaut
            soldeField.setText("0.0");
        }
    }
    
    /**
     * Configure si c'est un nouveau compte ou une édition.
     */
    public void setNewAccount(boolean newAccount) {
        this.newAccount = newAccount;
    }
    
    /**
     * Configure l'EntityManager
     */
    public void setEntityManager(EntityManager em) {
        this.em = em;
        this.dbAvailable = (em != null);
        
        // Charger les clients
        loadClients();
    }
    
    /**
     * Configure si la base de données est disponible
     */
    public void setDbAvailable(boolean dbAvailable) {
        this.dbAvailable = dbAvailable;
    }
    
    /**
     * Gère le clic sur le bouton Enregistrer
     */
    @FXML
    private void handleSave() {
        if (validateInput()) {
            // Récupérer les valeurs des champs
            String numero = numeroField.getText().trim();
            String type = typeComboBox.getValue();
            double solde = Double.parseDouble(soldeField.getText().trim());
            client selectedClient = clientComboBox.getValue();
            
            // Si c'est un nouveau compte, créer une nouvelle instance
            if (compte == null) {
                compte = new compte();
            }
            
            // Mettre à jour les valeurs du compte
            compte.setNumero(numero);
            compte.setType(type);
            compte.setSolde(solde);
            compte.setClient(selectedClient);
            
            // Si c'est un nouveau compte, définir la date de création
            if (newAccount) {
                LocalDate today = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                compte.setDateCreation(today.format(formatter));
            }
            
            // Sauvegarder le compte dans la base de données ou simuler la sauvegarde
            if (dbAvailable && em != null) {
                try {
                    em.getTransaction().begin();
                    
                    if (newAccount) {
                        em.persist(compte);
                    } else {
                        em.merge(compte);
                    }
                    
                    em.getTransaction().commit();
                    
                    showAlert(Alert.AlertType.INFORMATION, "Succès", 
                            "Le compte a été " + (newAccount ? "créé" : "modifié") + " avec succès.");
                    
                    // Fermer la fenêtre
                    dialogStage.close();
                } catch (Exception e) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", 
                            "Erreur lors de la sauvegarde du compte : " + e.getMessage());
                }
            } else {
                // Mode simulation
                showAlert(Alert.AlertType.INFORMATION, "Simulation", 
                        "En mode simulation, le compte a été " + (newAccount ? "créé" : "modifié") + " virtuellement.");
                
                // Fermer la fenêtre
                dialogStage.close();
            }
        }
    }
    
    /**
     * Gère le clic sur le bouton Annuler
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    
    /**
     * Valide les champs du formulaire
     * @return true si les champs sont valides, false sinon
     */
    private boolean validateInput() {
        String errorMessage = "";
        
        if (numeroField.getText() == null || numeroField.getText().trim().isEmpty()) {
            errorMessage += "Numéro de compte invalide\n";
        }
        
        if (typeComboBox.getValue() == null) {
            errorMessage += "Type de compte non sélectionné\n";
        }
        
        if (soldeField.getText() == null || soldeField.getText().trim().isEmpty()) {
            errorMessage += "Solde invalide\n";
        } else {
            try {
                Double.parseDouble(soldeField.getText().trim());
            } catch (NumberFormatException e) {
                errorMessage += "Le solde doit être un nombre\n";
            }
        }
        
        if (clientComboBox.getValue() == null) {
            errorMessage += "Client propriétaire non sélectionné\n";
        }
        
        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur de validation", errorMessage);
            return false;
        }
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 