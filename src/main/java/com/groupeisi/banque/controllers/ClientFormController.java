package com.groupeisi.banque.controllers;

import com.groupeisi.banque.entities.client;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import com.groupeisi.banque.entities.client;
import com.groupeisi.banque.utils.ApiClient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ClientFormController {
    @FXML
    private Label formTitleLabel;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField telephoneField;

    @FXML
    private TextField adresseField;

    @FXML
    private ComboBox<String> statutComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private Stage dialogStage;
    private client client;
    private boolean newClient = true;
    private ApiClient apiClient;
    private final Gson gson = new Gson();

    private final String baseUrl = "http://localhost/api-banque-examen/public/api/";

    @FXML
    private void initialize() {
        statutComboBox.getItems().addAll("ACTIF", "INACTIF");
        statutComboBox.getSelectionModel().selectFirst();
        apiClient = new ApiClient(baseUrl);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setClient(client client) {
        this.client = client;

        if (client != null) {
            nomField.setText(client.getNom());
            prenomField.setText(client.getPrenom());
            emailField.setText(client.getEmail());
            telephoneField.setText(client.getTelephone());
            adresseField.setText(client.getAdresse());

            String statut = client.getStatut();
            if (statut != null) {
                statutComboBox.getSelectionModel().select(statut);
            } else {
                statutComboBox.getSelectionModel().selectFirst();
            }

            formTitleLabel.setText("Modifier le client");
            newClient = false;
        } else {
            newClient = true;
            formTitleLabel.setText("Nouveau client");
        }
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            try {
                client = new client();
                client.setNom(nomField.getText());
                client.setPrenom(prenomField.getText());
                client.setEmail(emailField.getText());
                client.setTelephone(telephoneField.getText());
                client.setAdresse(adresseField.getText());
                client.setStatut(statutComboBox.getValue());
                client.setDateInscription(java.time.LocalDate.now().toString());

                String jsonPayload = gson.toJson(client);

                String response;
                if (newClient) {
                    response = apiClient.post("clients", jsonPayload);
                } else {
                    response = apiClient.put("clients/" + client.getId(), jsonPayload);
                }

                client = gson.fromJson(response, client.class);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Client enregistré avec succès.");
                alert.showAndWait();

                dialogStage.close();
            } catch (JsonSyntaxException e) {
                showError("Erreur de parsing JSON : " + e.getMessage());
            } catch (Exception e) {
                showError("Erreur lors de l'enregistrement : " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nomField.getText() == null || nomField.getText().isEmpty()) {
            errorMessage += "Le nom ne peut pas être vide.\n";
        }

        if (prenomField.getText() == null || prenomField.getText().isEmpty()) {
            errorMessage += "Le prénom ne peut pas être vide.\n";
        }

        if (emailField.getText() == null || emailField.getText().isEmpty()) {
            errorMessage += "L'email ne peut pas être vide.\n";
        } else if (!emailField.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            errorMessage += "L'email n'est pas valide.\n";
        }

        if (telephoneField.getText() == null || telephoneField.getText().isEmpty()) {
            errorMessage += "Le téléphone ne peut pas être vide.\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showError(errorMessage);
            return false;
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
