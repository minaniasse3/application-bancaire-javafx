package com.groupeisi.banque.controllers;

import com.groupeisi.banque.entities.carteBancaire;
import com.groupeisi.banque.entities.compte;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import jakarta.persistence.EntityManager;

public class CarteFormController {
    @FXML
    private Label formTitleLabel;

    @FXML
    private TextField numeroField;

    @FXML
    private TextField cvvField;

    @FXML
    private TextField dateExpirationField;

    @FXML
    private TextField soldeField;

    @FXML
    private ComboBox<compte> compteComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private Stage dialogStage;
    private carteBancaire carte;
    private boolean newCard = true;
    private EntityManager em;
    private boolean dbAvailable = false;

    @FXML
    private void initialize() {
        // Initialize your ComboBox or other UI elements if needed
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCarte(carteBancaire carte) {
        this.carte = carte;

        if (carte != null) {
            formTitleLabel.setText("Modifier la carte bancaire");
            numeroField.setText(carte.getNumero());
            cvvField.setText(carte.getCvv());
            dateExpirationField.setText(carte.getDateExpiration());
            soldeField.setText(String.valueOf(carte.getSolde()));
            // Set compte selection
            if (carte.getCompte() != null) {
                compteComboBox.setValue(carte.getCompte());
            }
        } else {
            formTitleLabel.setText("Ajouter une carte bancaire");
        }
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
        this.dbAvailable = (em != null);
    }

    public void setDbAvailable(boolean dbAvailable) {
        this.dbAvailable = dbAvailable;
    }

    @FXML
    private void handleSave() {
        if (validateInput()) {
            // Retrieve values from the fields
            String numero = numeroField.getText().trim();
            String cvv = cvvField.getText().trim();
            String dateExpiration = dateExpirationField.getText().trim();
            double solde = Double.parseDouble(soldeField.getText().trim());
            compte selectedCompte = compteComboBox.getValue();

            // If it's a new card, create a new carteBancaire instance
            if (carte == null) {
                carte = new carteBancaire();
            }

            // Set values to the carte object
            carte.setNumero(numero);
            carte.setCvv(cvv);
            carte.setDateExpiration(dateExpiration);
            carte.setSolde(solde);
            carte.setCompte(selectedCompte);

            // Save the card to the database
            if (dbAvailable && em != null) {
                try {
                    em.getTransaction().begin();

                    if (newCard) {
                        em.persist(carte);
                    } else {
                        em.merge(carte);
                    }

                    em.getTransaction().commit();

                    showAlert(Alert.AlertType.INFORMATION, "Succès",
                            "La carte a été " + (newCard ? "créée" : "modifiée") + " avec succès.");

                    dialogStage.close();
                } catch (Exception e) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur",
                            "Erreur lors de la sauvegarde de la carte bancaire : " + e.getMessage());
                }
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Simulation",
                        "En mode simulation, la carte a été " + (newCard ? "créée" : "modifiée") + " virtuellement.");
                dialogStage.close();
            }
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean validateInput() {
        String errorMessage = "";

        if (numeroField.getText() == null || numeroField.getText().trim().isEmpty()) {
            errorMessage += "Numéro de carte invalide\n";
        }

        if (cvvField.getText() == null || cvvField.getText().trim().isEmpty()) {
            errorMessage += "CVV invalide\n";
        }

        if (dateExpirationField.getText() == null || dateExpirationField.getText().trim().isEmpty()) {
            errorMessage += "Date d'expiration invalide\n";
        }

        if (soldeField.getText() == null || soldeField.getText().trim().isEmpty()) {
            errorMessage += "Solde invalide\n";
        }

        if (compteComboBox.getValue() == null) {
            errorMessage += "Compte non sélectionné\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur de validation", errorMessage);
            return false;
        }
    }
    public void setNewCard(boolean newCredit) {
        this.newCard = newCredit;
        // Si c'est un crédit existant, vous pouvez effectuer des actions spécifiques ici si nécessaire
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}