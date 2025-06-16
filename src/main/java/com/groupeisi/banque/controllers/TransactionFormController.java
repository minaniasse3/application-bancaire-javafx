package com.groupeisi.banque.controllers;

import com.groupeisi.banque.entities.compte;
import com.groupeisi.banque.entities.transaction;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.groupeisi.banque.entities.compte;
import com.groupeisi.banque.entities.transaction;
import com.groupeisi.banque.utils.ApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class TransactionFormController {
    @FXML
    private Label formTitleLabel;
    @FXML
    private TextField montantField;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private ComboBox<compte> sourceCompteComboBox;
    @FXML
    private ComboBox<compte> destCompteComboBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private Stage dialogStage;
    private transaction transaction;
    private boolean newTransaction = true;
    private ApiClient apiClient;
    private final Gson gson = new Gson();

    private final String baseUrl = "http://localhost/api-banque-examen/public/api/";

    @FXML
    private void initialize() {
        typeComboBox.getItems().addAll("Dépôt", "Retrait", "Virement");
        apiClient = new ApiClient(baseUrl);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setTransaction(transaction transaction) {
        this.transaction = transaction;
        this.newTransaction = false;

        if (transaction != null) {
            formTitleLabel.setText("Modifier la transaction");
            montantField.setText(String.valueOf(transaction.getMontant()));
            typeComboBox.setValue(transaction.getType());
            sourceCompteComboBox.setValue(transaction.getCompteSource());
            destCompteComboBox.setValue(transaction.getCompteDest());
        } else {
            formTitleLabel.setText("Nouvelle transaction");
            this.newTransaction = true;
        }
    }

    @FXML
    private void handleSave() {
        if (validateInput()) {
            try {
                double montant = Double.parseDouble(montantField.getText().trim());
                String type = typeComboBox.getValue();
                compte source = sourceCompteComboBox.getValue();
                compte dest = destCompteComboBox.getValue();

                if (transaction == null) {
                    transaction = new transaction();
                }

                transaction.setMontant(montant);
                transaction.setType(type);
                transaction.setCompteSource(source);
                transaction.setCompteDest(dest);

                String jsonPayload = gson.toJson(transaction);

                String response;
                if (newTransaction) {
                    response = apiClient.post("transactions", jsonPayload);
                } else {
                    response = apiClient.put("transactions/" + transaction.getId(), jsonPayload);
                }

                transaction = gson.fromJson(response, transaction.class);

                showAlert(Alert.AlertType.INFORMATION, "Succès", "La transaction a été " + (newTransaction ? "ajoutée" : "modifiée") + " avec succès.");
                dialogStage.close();
            } catch (JsonSyntaxException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de parsing JSON : " + e.getMessage());
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'enregistrement : " + e.getMessage());
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
        if (typeComboBox.getValue() == null) errorMessage.append("Type de transaction non sélectionné\n");
        if (sourceCompteComboBox.getValue() == null) errorMessage.append("Compte source non sélectionné\n");
        if (destCompteComboBox.getValue() == null && !"Dépôt".equals(typeComboBox.getValue())) errorMessage.append("Compte destination non sélectionné\n");

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
