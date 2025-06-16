package com.groupeisi.banque.controllers;

import com.groupeisi.banque.entities.client;
import com.groupeisi.banque.entities.credit;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import jakarta.persistence.EntityManager;

public class CreditFormController {
    @FXML
    private Label formTitleLabel;
    @FXML
    private TextField montantField;
    @FXML
    private TextField tauxField;
    @FXML
    private TextField dureeField;
    @FXML
    private TextField mensualiteField;
    @FXML
    private ComboBox<client> clientComboBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private Stage dialogStage;
    private credit credit;
    private boolean newCredit = true;
    private EntityManager em;
    private boolean dbAvailable = false;

    // Ajout des méthodes manquantes
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public void setDbAvailable(boolean dbAvailable) {
        this.dbAvailable = dbAvailable;
    }

    public void setNewCredit(boolean newCredit) {
        this.newCredit = newCredit;
    }

    @FXML
    private void initialize() {
        System.out.println("initialize() appelé");
        System.out.println("tauxField : " + tauxField);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCredit(credit credit) {
        System.out.println("setCredit() appelé");
        if (tauxField == null) {
            System.out.println("⚠️ tauxField est NULL ! Vérifiez l'injection FXML.");
            return;
        }

        this.credit = credit;
        this.newCredit = false;

        if (credit != null) {
            formTitleLabel.setText("Modifier le crédit");
            montantField.setText(String.valueOf(credit.getMontant()));
            tauxField.setText(String.valueOf(credit.getTauxInteret()));
            dureeField.setText(String.valueOf(credit.getDuree_mois()));
            mensualiteField.setText(String.valueOf(credit.getMensualite()));
            if (credit.getClient() != null) {
                clientComboBox.setValue(credit.getClient());
            }
        } else {
            formTitleLabel.setText("Ajouter un crédit");
            this.newCredit = true;
        }
    }

    @FXML
    private void handleSave() {
        if (validateInput()) {
            try {
                double montant = Double.parseDouble(montantField.getText().trim());
                double tauxInteret = Double.parseDouble(tauxField.getText().trim());
                int dureeMois = Integer.parseInt(dureeField.getText().trim());
                double mensualite = Double.parseDouble(mensualiteField.getText().trim());
                client selectedClient = clientComboBox.getValue();

                if (credit == null) {
                    credit = new credit();
                }

                credit.setMontant(montant);
                credit.setTauxInteret(tauxInteret);
                credit.setDuree_mois(dureeMois);
                credit.setMensualite(mensualite);
                credit.setClient(selectedClient);

                if (dbAvailable && em != null) {
                    em.getTransaction().begin();
                    if (newCredit) {
                        em.persist(credit);
                    } else {
                        em.merge(credit);
                    }
                    em.getTransaction().commit();
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Le crédit a été " + (newCredit ? "créé" : "modifié") + " avec succès.");
                    dialogStage.close();
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Simulation", "En mode simulation, le crédit a été " + (newCredit ? "créé" : "modifié") + " virtuellement.");
                    dialogStage.close();
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la sauvegarde : " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();
        if (montantField.getText() == null || montantField.getText().trim().isEmpty()) errorMessage.append("Montant invalide\n");
        if (tauxField.getText() == null || tauxField.getText().trim().isEmpty()) errorMessage.append("Taux d'intérêt invalide\n");
        if (dureeField.getText() == null || dureeField.getText().trim().isEmpty()) errorMessage.append("Durée invalide\n");
        if (mensualiteField.getText() == null || mensualiteField.getText().trim().isEmpty()) errorMessage.append("Mensualité invalide\n");
        if (clientComboBox.getValue() == null) errorMessage.append("Client non sélectionné\n");
        if (!errorMessage.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de validation", errorMessage.toString());
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}