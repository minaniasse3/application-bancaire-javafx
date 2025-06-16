package com.groupeisi.banque.controllers;

import com.groupeisi.banque.App;
import com.groupeisi.banque.entities.client;
import com.groupeisi.banque.entities.compte;
import com.groupeisi.banque.entities.transaction;
import com.groupeisi.banque.entities.carteBancaire;
import com.groupeisi.banque.entities.credit;
import com.groupeisi.banque.entities.ticketSupport;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import jakarta.persistence.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Map;
import java.util.HashMap;

public class ClientDashboardController implements Initializable {
    
    // Référence au client connecté
    private client currentClient;
    
    // EntityManager pour les opérations de base de données
    private EntityManager em;
    
    // Indicateur de disponibilité de la base de données
    private boolean dbAvailable = false;
    
    // Éléments d'interface utilisateur - En-tête
    @FXML
    private Label clientNameLabel;
    
    @FXML
    private Button logoutButton;
    
    // Vues principales
    @FXML
    private VBox accountsView;
    
    @FXML
    private VBox transfersView;
    
    @FXML
    private VBox historyView;
    
    @FXML
    private VBox cardsView;
    
    @FXML
    private VBox creditsView;
    
    @FXML
    private VBox supportView;
    
    // Éléments de la vue Comptes
    @FXML
    private TableView<compte> accountsTable;
    
    @FXML
    private TableColumn<compte, String> accountNumberColumn;
    
    @FXML
    private TableColumn<compte, String> accountTypeColumn;
    
    @FXML
    private TableColumn<compte, Double> accountBalanceColumn;
    
    @FXML
    private TableColumn<compte, String> accountDateColumn;
    
    @FXML
    private TableColumn<compte, String> accountActionsColumn;
    
    @FXML
    private TextField depositAmountField;
    
    @FXML
    private TextField withdrawAmountField;
    
    // Éléments de la vue Virements
    @FXML
    private ComboBox<compte> sourceAccountComboBox;
    
    @FXML
    private ComboBox<compte> destAccountComboBox;
    
    @FXML
    private TextField transferAmountField;
    
    @FXML
    private TextField transferMotifField;
    
    @FXML
    private TableView<transaction> transfersTable;
    
    @FXML
    private TableColumn<transaction, String> transferDateColumn;
    
    @FXML
    private TableColumn<transaction, String> transferSourceColumn;
    
    @FXML
    private TableColumn<transaction, String> transferDestColumn;
    
    @FXML
    private TableColumn<transaction, Double> transferAmountColumn;
    
    @FXML
    private TableColumn<transaction, String> transferStatusColumn;
    
    // Éléments de la vue Historique
    @FXML
    private ComboBox<compte> historyAccountComboBox;
    
    @FXML
    private ComboBox<String> historyTypeComboBox;
    
    @FXML
    private DatePicker historyStartDatePicker;
    
    @FXML
    private DatePicker historyEndDatePicker;
    
    @FXML
    private TableView<transaction> historyTable;
    
    @FXML
    private TableColumn<transaction, String> historyDateColumn;
    
    @FXML
    private TableColumn<transaction, String> historyTypeColumn;
    
    @FXML
    private TableColumn<transaction, String> historyAccountColumn;
    
    @FXML
    private TableColumn<transaction, Double> historyAmountColumn;
    
    @FXML
    private TableColumn<transaction, Double> historyBalanceColumn;
    
    // Éléments de la vue Cartes Bancaires
    @FXML
    private TableView<carteBancaire> cardsTable;
    
    @FXML
    private TableColumn<carteBancaire, String> cardNumberColumn;
    
    @FXML
    private TableColumn<carteBancaire, String> cardExpiryColumn;
    
    @FXML
    private TableColumn<carteBancaire, String> cardAccountColumn;
    
    @FXML
    private TableColumn<carteBancaire, String> cardStatusColumn;
    
    @FXML
    private TableColumn<carteBancaire, String> cardActionsColumn;
    
    // Éléments de la vue Crédits
    @FXML
    private TableView<credit> creditsTable;
    
    @FXML
    private TableColumn<credit, Long> creditIdColumn;
    
    @FXML
    private TableColumn<credit, Double> creditAmountColumn;
    
    @FXML
    private TableColumn<credit, Double> creditRateColumn;
    
    @FXML
    private TableColumn<credit, Integer> creditDurationColumn;
    
    @FXML
    private TableColumn<credit, Double> creditMonthlyColumn;
    
    @FXML
    private TableColumn<credit, String> creditStatusColumn;
    
    @FXML
    private TableColumn<credit, String> creditActionsColumn;
    
    @FXML
    private TextField simulationAmountField;
    
    @FXML
    private Slider simulationDurationSlider;
    
    @FXML
    private Label simulationRateLabel;
    
    @FXML
    private Label simulationMonthlyLabel;
    
    @FXML
    private Label simulationTotalLabel;
    
    // Éléments de la vue Support
    @FXML
    private TableView<ticketSupport> ticketsTable;
    
    @FXML
    private TableColumn<ticketSupport, Long> ticketIdColumn;
    
    @FXML
    private TableColumn<ticketSupport, String> ticketSubjectColumn;
    
    @FXML
    private TableColumn<ticketSupport, String> ticketDateColumn;
    
    @FXML
    private TableColumn<ticketSupport, String> ticketStatusColumn;
    
    @FXML
    private TableColumn<ticketSupport, String> ticketActionsColumn;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialiser l'EntityManager
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("banquePU");
        em = emf.createEntityManager();
        
        // Configurer les colonnes des tableaux
        setupTableColumns();
        
        // Configurer les écouteurs d'événements
        setupEventListeners();
        
        // Note: Le chargement des données sera fait par setCurrentClient
        // Nous ne chargeons plus les données factices ici
    }
    
    private void setupTableColumns() {
        // Configuration des colonnes pour la table des comptes
        accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        accountTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        accountBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("solde"));
        accountDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
        
        // Configuration des colonnes pour la table des virements
        transferDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        transferAmountColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        transferStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        
        // Configuration des colonnes pour la table de l'historique
        historyDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        historyTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        historyAmountColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        
        // Configuration des colonnes pour la table des cartes
        cardNumberColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        cardExpiryColumn.setCellValueFactory(new PropertyValueFactory<>("dateExpiration"));
        cardStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        
        // Configuration des colonnes pour la table des crédits
        creditIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        creditAmountColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        creditRateColumn.setCellValueFactory(new PropertyValueFactory<>("tauxInteret"));
        creditDurationColumn.setCellValueFactory(new PropertyValueFactory<>("duree_mois"));
        creditMonthlyColumn.setCellValueFactory(new PropertyValueFactory<>("mensualite"));
        creditStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        
        // Configuration des colonnes pour la table des tickets
        ticketIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ticketSubjectColumn.setCellValueFactory(new PropertyValueFactory<>("sujet"));
        ticketDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateOuverture"));
        ticketStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }
    
    private void setupEventListeners() {
        // Écouteur pour le slider de durée de simulation de crédit
        simulationDurationSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Mettre à jour l'affichage de la durée
                int months = newValue.intValue();
                // Recalculer la simulation si un montant est déjà saisi
                if (!simulationAmountField.getText().isEmpty()) {
                    calculateCreditSimulation();
                }
            }
        });
    }
    
    // Méthodes de navigation entre les vues
    @FXML
    public void showAccounts() {
        accountsView.setVisible(true);
        transfersView.setVisible(false);
        historyView.setVisible(false);
        cardsView.setVisible(false);
        creditsView.setVisible(false);
        supportView.setVisible(false);
    }
    
    @FXML
    public void showTransfers() {
        accountsView.setVisible(false);
        transfersView.setVisible(true);
        historyView.setVisible(false);
        cardsView.setVisible(false);
        creditsView.setVisible(false);
        supportView.setVisible(false);
        
        // Rafraîchir les transactions à chaque affichage de l'onglet
        refreshTransfersTable();
    }
    
    @FXML
    public void showHistory() {
        accountsView.setVisible(false);
        transfersView.setVisible(false);
        historyView.setVisible(true);
        cardsView.setVisible(false);
        creditsView.setVisible(false);
        supportView.setVisible(false);
        
        // Rafraîchir l'historique à chaque affichage de l'onglet
        refreshHistoryTable();
    }
    
    @FXML
    public void showCards() {
        accountsView.setVisible(false);
        transfersView.setVisible(false);
        historyView.setVisible(false);
        cardsView.setVisible(true);
        creditsView.setVisible(false);
        supportView.setVisible(false);
    }
    
    @FXML
    public void showCredits() {
        accountsView.setVisible(false);
        transfersView.setVisible(false);
        historyView.setVisible(false);
        cardsView.setVisible(false);
        creditsView.setVisible(true);
        supportView.setVisible(false);
    }
    
    @FXML
    public void showSupport() {
        accountsView.setVisible(false);
        transfersView.setVisible(false);
        historyView.setVisible(false);
        cardsView.setVisible(false);
        creditsView.setVisible(false);
        supportView.setVisible(true);
    }
    
    @FXML
    public void handleLogout() {
        try {
            // Fermer l'EntityManager
            if (em != null && em.isOpen()) {
                em.close();
            }
            
            // Retourner à la page de connexion
            FXMLLoader loader = new FXMLLoader(App.class.getResource("login-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setTitle("Connexion - Système Bancaire");
            stage.setScene(scene);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                    "Impossible de se déconnecter : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void calculateCreditSimulation() {
        // Cette méthode sera implémentée plus tard
    }
    
    // Méthodes de gestion des comptes
    @FXML
    public void handleNewAccount() {
        // Cette fonctionnalité est réservée aux administrateurs
        showAlert(Alert.AlertType.INFORMATION, "Opération non autorisée", 
                "La création de comptes est une opération réservée aux administrateurs. Veuillez contacter votre conseiller.");
    }
    
    @FXML
    public void handleDeposit() {
        try {
            // Récupérer le compte sélectionné
            compte selectedAccount = accountsTable.getSelectionModel().getSelectedItem();
            if (selectedAccount == null) {
                showAlert(Alert.AlertType.WARNING, "Dépôt", 
                        "Veuillez sélectionner un compte.");
                return;
            }
            
            // Récupérer le montant
            String amountStr = depositAmountField.getText().trim();
            if (amountStr.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Dépôt", 
                        "Veuillez saisir un montant.");
                return;
            }
            
            double amount;
            try {
                amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Dépôt", 
                        "Veuillez saisir un montant valide (nombre positif).");
                return;
            }
            
            // Effectuer le dépôt
            if (dbAvailable) {
                em.getTransaction().begin();
                
                // Mettre à jour le solde du compte
                selectedAccount.setSolde(selectedAccount.getSolde() + amount);
                
                // Créer une transaction
                transaction newTransaction = new transaction();
                newTransaction.setType("depot");
                newTransaction.setMontant(amount);
                newTransaction.setDate(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
                newTransaction.setCompteSource(selectedAccount);
                newTransaction.setStatut("validé");
                newTransaction.setMotif("Dépôt en espèces");
                
                em.persist(newTransaction);
                em.getTransaction().commit();
            } else {
                // En mode simulation, mettre à jour directement le solde
                selectedAccount.setSolde(selectedAccount.getSolde() + amount);
                System.out.println("Dépôt simulé de " + amount + "€ sur le compte " + selectedAccount.getNumero());
            }
            
            // Rafraîchir l'affichage
            refreshAccountsTable();
            refreshTransfersTable();
            
            // Vider le champ de montant
            depositAmountField.clear();
            
            showAlert(Alert.AlertType.INFORMATION, "Dépôt", 
                    "Dépôt de " + amount + " XOF effectué avec succès sur le compte " + 
                    formatAccountNumber(selectedAccount.getNumero()) + ".");
            
        } catch (Exception e) {
            if (dbAvailable && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            showAlert(Alert.AlertType.ERROR, "Erreur de dépôt", 
                    "Une erreur est survenue : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    public void handleWithdraw() {
        try {
            // Récupérer le compte sélectionné
            compte selectedAccount = accountsTable.getSelectionModel().getSelectedItem();
            if (selectedAccount == null) {
                showAlert(Alert.AlertType.WARNING, "Retrait", 
                        "Veuillez sélectionner un compte.");
                return;
            }
            
            // Récupérer le montant
            String amountStr = withdrawAmountField.getText().trim();
            if (amountStr.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Retrait", 
                        "Veuillez saisir un montant.");
                return;
            }
            
            double amount;
            try {
                amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Retrait", 
                        "Veuillez saisir un montant valide (nombre positif).");
                return;
            }
            
            // Vérifier si le solde est suffisant
            if (selectedAccount.getSolde() < amount) {
                showAlert(Alert.AlertType.ERROR, "Retrait", 
                        "Solde insuffisant pour effectuer ce retrait.");
                return;
            }
            
            // Effectuer le retrait
            if (dbAvailable) {
                em.getTransaction().begin();
                
                // Mettre à jour le solde du compte
                selectedAccount.setSolde(selectedAccount.getSolde() - amount);
                
                // Créer une transaction
                transaction newTransaction = new transaction();
                newTransaction.setType("retrait");
                newTransaction.setMontant(amount);
                newTransaction.setDate(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
                newTransaction.setCompteSource(selectedAccount);
                newTransaction.setStatut("validé");
                newTransaction.setMotif("Retrait en espèces");
                
                em.persist(newTransaction);
                em.getTransaction().commit();
            } else {
                // En mode simulation, mettre à jour directement le solde
                selectedAccount.setSolde(selectedAccount.getSolde() - amount);
                System.out.println("Retrait simulé de " + amount + "€ sur le compte " + selectedAccount.getNumero());
            }
            
            // Rafraîchir l'affichage
            refreshAccountsTable();
            refreshTransfersTable();
            
            // Vider le champ de montant
            withdrawAmountField.clear();
            
            showAlert(Alert.AlertType.INFORMATION, "Retrait", 
                    "Retrait de " + amount + " XOF effectué avec succès sur le compte " + 
                    formatAccountNumber(selectedAccount.getNumero()) + ".");
            
        } catch (Exception e) {
            if (dbAvailable && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            showAlert(Alert.AlertType.ERROR, "Erreur de retrait", 
                    "Une erreur est survenue : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Méthodes de gestion des virements
    @FXML
    public void handleTransfer() {
        try {
            // Récupérer les comptes source et destination
            compte sourceAccount = sourceAccountComboBox.getValue();
            compte destAccount = destAccountComboBox.getValue();
            
            if (sourceAccount == null || destAccount == null) {
                showAlert(Alert.AlertType.WARNING, "Virement", 
                        "Veuillez sélectionner les comptes source et destination.");
                return;
            }
            
            if (sourceAccount.equals(destAccount)) {
                showAlert(Alert.AlertType.WARNING, "Virement", 
                        "Les comptes source et destination doivent être différents.");
                return;
            }
            
            // Récupérer le montant
            String amountStr = transferAmountField.getText().trim();
            if (amountStr.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Virement", 
                        "Veuillez saisir un montant.");
                return;
            }
            
            double amount;
            try {
                amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Virement", 
                        "Veuillez saisir un montant valide (nombre positif).");
                return;
            }
            
            // Vérifier si le solde est suffisant
            if (sourceAccount.getSolde() < amount) {
                showAlert(Alert.AlertType.ERROR, "Virement", 
                        "Solde insuffisant pour effectuer ce virement.");
                return;
            }
            
            // Récupérer le motif
            String motif = transferMotifField.getText().trim();
            if (motif.isEmpty()) {
                motif = "Virement entre comptes";
            }
            
            // Effectuer le virement
            if (dbAvailable) {
                em.getTransaction().begin();
                
                // Mettre à jour les soldes des comptes
                sourceAccount.setSolde(sourceAccount.getSolde() - amount);
                destAccount.setSolde(destAccount.getSolde() + amount);
                
                // Créer une transaction pour le compte source (débit)
                transaction sourceTransaction = new transaction();
                sourceTransaction.setType("virement_sortant");
                sourceTransaction.setMontant(amount);
                sourceTransaction.setDate(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
                sourceTransaction.setCompteSource(sourceAccount);
                sourceTransaction.setCompteDest(destAccount);
                sourceTransaction.setStatut("validé");
                sourceTransaction.setMotif(motif);
                
                // Créer une transaction pour le compte destination (crédit)
                transaction destTransaction = new transaction();
                destTransaction.setType("virement_entrant");
                destTransaction.setMontant(amount);
                destTransaction.setDate(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
                destTransaction.setCompteSource(destAccount);
                destTransaction.setCompteDest(sourceAccount);
                destTransaction.setStatut("validé");
                destTransaction.setMotif(motif);
                
                em.persist(sourceTransaction);
                em.persist(destTransaction);
                em.getTransaction().commit();
            } else {
                // En mode simulation, on met à jour directement les objets
                sourceAccount.setSolde(sourceAccount.getSolde() - amount);
                destAccount.setSolde(destAccount.getSolde() + amount);
                
                System.out.println("Virement simulé: " + amount + "€ de " + sourceAccount.getNumero() + " vers " + destAccount.getNumero());
            }
            
            // Rafraîchir l'affichage
            refreshAccountsTable();
            refreshTransfersTable();
            
            // Vider les champs
            transferAmountField.clear();
            transferMotifField.clear();
            // Ne pas réinitialiser les ComboBox pour faciliter les virements multiples
            
            showAlert(Alert.AlertType.INFORMATION, "Virement", 
                    "Virement de " + amount + " XOF effectué avec succès du compte " + 
                    formatAccountNumber(sourceAccount.getNumero()) + " vers le compte " + 
                    formatAccountNumber(destAccount.getNumero()) + ".");
            
        } catch (Exception e) {
            if (dbAvailable && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            showAlert(Alert.AlertType.ERROR, "Erreur de virement", 
                    "Une erreur est survenue : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Méthodes de gestion de l'historique
    @FXML
    public void handleFilterHistory() {
        // Récupération des filtres
        compte selectedAccount = historyAccountComboBox.getValue();
        String selectedType = historyTypeComboBox.getValue();
        LocalDate startDate = historyStartDatePicker.getValue();
        LocalDate endDate = historyEndDatePicker.getValue();
        
        // Appliquer les filtres et rafraîchir l'historique
        refreshHistoryTable(selectedAccount, selectedType, startDate, endDate);
    }
    
    @FXML
    public void handleExportPDF() {
        // TODO: Implémenter l'export PDF
        showAlert(Alert.AlertType.INFORMATION, "Export PDF", 
                "Fonctionnalité d'export PDF à implémenter.");
    }
    
    // Méthodes utilitaires
    private void refreshAccountsTable() {
        try {
            // Effacer la liste actuelle
            ObservableList<compte> comptesList = FXCollections.observableArrayList();
            
            if (dbAvailable && currentClient != null) {
                // Charger les comptes depuis la base de données
                em.getTransaction().begin();
                
                TypedQuery<compte> query = em.createQuery(
                    "SELECT c FROM compte c WHERE c.client = :client", 
                    compte.class);
                query.setParameter("client", currentClient);
                
                List<compte> comptes = query.getResultList();
                comptesList.addAll(comptes);
                
                em.getTransaction().commit();
                
                System.out.println("Comptes chargés depuis la base de données: " + comptes.size());
            } else if (currentClient != null) {
                // En mode simulation, créer des comptes de test
                compte compte1 = new compte();
                compte1.setId(1L);
                compte1.setNumero("FR7630001007941234567890185");
                compte1.setType("COURANT");
                compte1.setSolde(1500.0);
                compte1.setDateCreation(java.time.LocalDate.now().minusYears(1).toString());
                compte1.setClient(currentClient);
                
                compte compte2 = new compte();
                compte2.setId(2L);
                compte2.setNumero("FR7630004000031234567890143");
                compte2.setType("EPARGNE");
                compte2.setSolde(5000.0);
                compte2.setDateCreation(java.time.LocalDate.now().minusMonths(6).toString());
                compte2.setClient(currentClient);
                
                comptesList.add(compte1);
                comptesList.add(compte2);
                
                System.out.println("Comptes simulés créés: " + comptesList.size());
            }
            
            // Mettre à jour le tableau
            accountsTable.setItems(comptesList);
            
            // Configuration des ComboBox pour les transferts
            configureComboBox(sourceAccountComboBox, comptesList);
            configureComboBox(destAccountComboBox, comptesList);
            
            // Configuration de la ComboBox pour l'historique
            configureComboBox(historyAccountComboBox, comptesList);
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            showAlert(Alert.AlertType.ERROR, "Erreur de chargement", 
                    "Impossible de charger les comptes : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private <T> void configureComboBox(ComboBox<compte> comboBox, ObservableList<compte> items) {
        comboBox.setItems(items);
        
        // Définir comment afficher les comptes dans la liste déroulante
        comboBox.setCellFactory(listView -> new ListCell<compte>() {
            @Override
            protected void updateItem(compte item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getType() + " - " + formatAccountNumber(item.getNumero()) + " - " + item.getSolde() + " XOF");
                }
            }
        });
        
        // Définir comment afficher le compte sélectionné dans la ComboBox
        comboBox.setButtonCell(new ListCell<compte>() {
            @Override
            protected void updateItem(compte item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Sélectionnez un compte");
                } else {
                    setText(item.getType() + " - " + formatAccountNumber(item.getNumero()) + " - " + item.getSolde() + " XOF");
                }
            }
        });
    }
    
    private String formatAccountNumber(String numero) {
        // Format IBAN: FRXX XXXX XXXX XXXX XXXX XXXX XXX
        if (numero == null || numero.length() < 14) {
            return numero;
        }
        
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < numero.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                formatted.append(" ");
            }
            formatted.append(numero.charAt(i));
        }
        return formatted.toString();
    }
    
    private void refreshTransfersTable() {
        try {
            // Effacer la liste actuelle
            ObservableList<transaction> transactionsList = FXCollections.observableArrayList();
            
            if (dbAvailable && currentClient != null) {
                // Charger les transactions depuis la base de données
                em.getTransaction().begin();
                
                // Récupérer tous les comptes du client
                TypedQuery<compte> compteQuery = em.createQuery(
                    "SELECT c FROM compte c WHERE c.client = :client", 
                    compte.class);
                compteQuery.setParameter("client", currentClient);
                List<compte> comptes = compteQuery.getResultList();
                
                // Pour chaque compte, récupérer les transactions (entrantes et sortantes)
                for (compte c : comptes) {
                    // Transactions sortantes (où le compte est la source)
                    TypedQuery<transaction> sourceQuery = em.createQuery(
                        "SELECT t FROM transaction t WHERE t.compteSource = :compte ORDER BY t.date DESC", 
                        transaction.class);
                    sourceQuery.setParameter("compte", c);
                    transactionsList.addAll(sourceQuery.getResultList());
                    
                    // Transactions entrantes (où le compte est la destination)
                    TypedQuery<transaction> destQuery = em.createQuery(
                        "SELECT t FROM transaction t WHERE t.compteDest = :compte ORDER BY t.date DESC", 
                        transaction.class);
                    destQuery.setParameter("compte", c);
                    transactionsList.addAll(destQuery.getResultList());
                }
                
                em.getTransaction().commit();
                
                System.out.println("Transactions chargées depuis la base de données: " + transactionsList.size());
            } else if (currentClient != null) {
                // En mode simulation, créer des transactions de test
                System.out.println("Mode simulation : aucune transaction chargée");
            }
            
            // Configurer les colonnes pour afficher les informations de transaction
            transferDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            
            // Colonne compte source
            transferSourceColumn.setCellFactory(column -> {
                return new TableCell<transaction, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            transaction tx = getTableView().getItems().get(getIndex());
                            if (tx.getCompteSource() != null) {
                                setText(formatAccountNumber(tx.getCompteSource().getNumero()));
                            } else {
                                setText("N/A");
                            }
                        }
                    }
                };
            });
            
            // Colonne compte destination
            transferDestColumn.setCellFactory(column -> {
                return new TableCell<transaction, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            transaction tx = getTableView().getItems().get(getIndex());
                            if (tx.getCompteDest() != null) {
                                setText(formatAccountNumber(tx.getCompteDest().getNumero()));
                            } else {
                                setText("N/A");
                            }
                        }
                    }
                };
            });
            
            transferAmountColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
            transferStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
            
            // Colorier les montants selon le type de transaction
            transferAmountColumn.setCellFactory(column -> {
                return new TableCell<transaction, Double>() {
                    @Override
                    protected void updateItem(Double amount, boolean empty) {
                        super.updateItem(amount, empty);
                        if (empty || amount == null) {
                            setText(null);
                            setStyle("");
                        } else {
                            transaction tx = getTableView().getItems().get(getIndex());
                            setText(String.format("%.2f XOF", amount));
                            
                            // Retrait ou virement sortant en rouge, dépôt ou virement entrant en vert
                            if (tx.getType().equals("retrait") || tx.getType().equals("virement_sortant")) {
                                setStyle("-fx-text-fill: red;");
                            } else if (tx.getType().equals("depot") || tx.getType().equals("virement_entrant")) {
                                setStyle("-fx-text-fill: green;");
                            } else {
                                setStyle("");
                            }
                        }
                    }
                };
            });
            
            // Mettre à jour le tableau
            transfersTable.setItems(transactionsList);
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Erreur lors du chargement des transactions : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Méthodes de gestion des cartes bancaires
    @FXML
    public void handleRequestCard() {
        try {
            // Récupérer le compte sélectionné
            compte selectedAccount = accountsTable.getSelectionModel().getSelectedItem();
            if (selectedAccount == null) {
                showAlert(Alert.AlertType.WARNING, "Demande de carte", 
                        "Veuillez sélectionner un compte pour lequel demander une carte.");
                return;
            }
            
            // Vérifier si le compte a déjà une carte active
            boolean hasActiveCard = false;
            for (carteBancaire card : selectedAccount.getCartes()) {
                if (card.getStatut().equals("active")) {
                    hasActiveCard = true;
                    break;
                }
            }
            
            if (hasActiveCard) {
                showAlert(Alert.AlertType.WARNING, "Demande de carte", 
                        "Ce compte possède déjà une carte active.");
                return;
            }
            
            // Créer une nouvelle carte
            carteBancaire newCard = new carteBancaire();
            newCard.setNumero(generateCardNumber());
            newCard.setCvv(generateCVV());
            
            // Date d'expiration : aujourd'hui + 3 ans
            LocalDate expiryDate = LocalDate.now().plusYears(3);
            newCard.setDateExpiration(expiryDate.format(DateTimeFormatter.ofPattern("MM/yy")));
            
            newCard.setSolde(0.0); // Solde initial de la carte
            newCard.setStatut("en_attente"); // Statut initial
            newCard.setCompte(selectedAccount);
            newCard.setCodePin(generatePIN());
            
            // Persister en base de données
            em.getTransaction().begin();
            em.persist(newCard);
            em.getTransaction().commit();
            
            // Rafraîchir l'affichage
            refreshCardsTable();
            
            showAlert(Alert.AlertType.INFORMATION, "Demande de carte", 
                    "Votre demande de carte a été enregistrée avec succès.\n" +
                    "Numéro de carte : " + maskCardNumber(newCard.getNumero()) + "\n" +
                    "Date d'expiration : " + newCard.getDateExpiration() + "\n" +
                    "CVV : " + newCard.getCvv() + "\n" +
                    "Code PIN : " + newCard.getCodePin() + "\n\n" +
                    "Votre carte sera activée après validation.");
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            showAlert(Alert.AlertType.ERROR, "Erreur de demande de carte", 
                    "Une erreur est survenue : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Méthodes de gestion des crédits
    @FXML
    public void handleRequestCredit() {
        try {
            // Récupérer les valeurs de la simulation
            String amountStr = simulationAmountField.getText().trim();
            if (amountStr.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Demande de crédit", 
                        "Veuillez saisir un montant.");
                return;
            }
            
            double amount;
            try {
                amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Demande de crédit", 
                        "Veuillez saisir un montant valide (nombre positif).");
                return;
            }
            
            // Récupérer la durée
            int duration = (int) simulationDurationSlider.getValue();
            
            // Taux d'intérêt (fixé à 5% pour l'exemple)
            double rate = 5.0;
            
            // Calcul de la mensualité
            double monthlyRate = rate / 100 / 12;
            double monthly = (amount * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -duration));
            
            // Créer un nouveau crédit
            credit newCredit = new credit();
            newCredit.setMontant(amount);
            newCredit.setTauxInteret(rate);
            newCredit.setDuree_mois(duration);
            newCredit.setMensualite(monthly);
            newCredit.setDateDemande(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            newCredit.setStatut("en_attente");
            newCredit.setClient(currentClient);
            
            // Persister en base de données
            em.getTransaction().begin();
            em.persist(newCredit);
            em.getTransaction().commit();
            
            // Rafraîchir l'affichage
            refreshCreditsTable();
            
            // Vider les champs
            simulationAmountField.clear();
            simulationDurationSlider.setValue(24);
            simulationRateLabel.setText("5.0%");
            simulationMonthlyLabel.setText("0.00 XOF");
            simulationTotalLabel.setText("0.00 XOF");
            
            showAlert(Alert.AlertType.INFORMATION, "Demande de crédit", 
                    "Votre demande de crédit a été enregistrée avec succès.\n" +
                    "Montant : " + amount + " XOF\n" +
                    "Durée : " + duration + " mois\n" +
                    "Taux : " + rate + "%\n" +
                    "Mensualité : " + String.format("%.2f XOF", monthly) + "\n\n" +
                    "Votre demande sera traitée dans les plus brefs délais.");
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            showAlert(Alert.AlertType.ERROR, "Erreur de demande de crédit", 
                    "Une erreur est survenue : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    public void handleSimulateCredit() {
        try {
            // Récupérer le montant
            String amountStr = simulationAmountField.getText().trim();
            if (amountStr.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Simulation", 
                        "Veuillez saisir un montant.");
                return;
            }
            
            double amount;
            try {
                amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Simulation", 
                        "Veuillez saisir un montant valide (nombre positif).");
                return;
            }
            
            // Récupérer la durée
            int duration = (int) simulationDurationSlider.getValue();
            
            // Taux d'intérêt (fixé à 5% pour l'exemple)
            double rate = 5.0;
            simulationRateLabel.setText(rate + "%");
            
            // Calcul de la mensualité (formule simplifiée)
            double monthlyRate = rate / 100 / 12;
            double monthly = (amount * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -duration));
            double totalCost = monthly * duration;
            
            // Mise à jour de l'interface
            simulationMonthlyLabel.setText(String.format("%.2f XOF", monthly));
            simulationTotalLabel.setText(String.format("%.2f XOF", totalCost));
            
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de simulation", 
                    "Une erreur est survenue : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Méthodes de gestion du support
    @FXML
    public void handleNewTicket() {
        try {
            // Afficher une boîte de dialogue pour saisir les informations du ticket
            Dialog<ticketSupport> dialog = new Dialog<>();
            dialog.setTitle("Nouveau ticket de support");
            dialog.setHeaderText("Veuillez saisir les informations de votre demande");
            
            // Boutons
            ButtonType submitButtonType = new ButtonType("Soumettre", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);
            
            // Grille pour les champs
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            
            TextField subjectField = new TextField();
            subjectField.setPromptText("Sujet");
            TextArea descriptionArea = new TextArea();
            descriptionArea.setPromptText("Description détaillée");
            
            grid.add(new Label("Sujet:"), 0, 0);
            grid.add(subjectField, 1, 0);
            grid.add(new Label("Description:"), 0, 1);
            grid.add(descriptionArea, 1, 1);
            
            dialog.getDialogPane().setContent(grid);
            
            // Convertir le résultat
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == submitButtonType) {
                    String subject = subjectField.getText().trim();
                    String description = descriptionArea.getText().trim();
                    
                    if (subject.isEmpty() || description.isEmpty()) {
                        showAlert(Alert.AlertType.WARNING, "Ticket incomplet", 
                                "Veuillez remplir tous les champs.");
                        return null;
                    }
                    
                    // Créer un nouveau ticket
                    ticketSupport newTicket = new ticketSupport();
                    newTicket.setSujet(subject);
                    newTicket.setDescription(description);
                    newTicket.setDateOuverture(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
                    newTicket.setStatut("ouvert");
                    newTicket.setClient(currentClient);
                    
                    return newTicket;
                }
                return null;
            });
            
            // Afficher la boîte de dialogue et traiter le résultat
            dialog.showAndWait().ifPresent(ticket -> {
                // Persister en base de données
                em.getTransaction().begin();
                em.persist(ticket);
                em.getTransaction().commit();
                
                // Rafraîchir l'affichage
                refreshTicketsTable();
                
                showAlert(Alert.AlertType.INFORMATION, "Ticket créé", 
                        "Votre ticket a été créé avec succès.\n" +
                        "Référence : " + ticket.getId() + "\n" +
                        "Sujet : " + ticket.getSujet() + "\n" +
                        "Date : " + ticket.getDateOuverture() + "\n\n" +
                        "Un conseiller vous répondra dans les plus brefs délais.");
            });
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            showAlert(Alert.AlertType.ERROR, "Erreur de création de ticket", 
                    "Une erreur est survenue : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Méthodes utilitaires supplémentaires
    private void refreshCardsTable() {
        try {
            // Effacer la liste actuelle
            ObservableList<carteBancaire> cardsList = FXCollections.observableArrayList();
            
            if (dbAvailable && currentClient != null) {
                // Charger les cartes depuis la base de données
                em.getTransaction().begin();
                
                // Récupérer tous les comptes du client
                TypedQuery<compte> compteQuery = em.createQuery(
                    "SELECT c FROM compte c WHERE c.client = :client", 
                    compte.class);
                compteQuery.setParameter("client", currentClient);
                List<compte> comptes = compteQuery.getResultList();
                
                // Pour chaque compte, récupérer les cartes
                for (compte c : comptes) {
                    cardsList.addAll(c.getCartes());
                }
                
                em.getTransaction().commit();
                
                System.out.println("Cartes chargées depuis la base de données: " + cardsList.size());
            } else if (currentClient != null) {
                // En mode simulation, créer des cartes de test
                System.out.println("Mode simulation : aucune carte chargée");
            }
            
            // Mettre à jour le tableau
            cardsTable.setItems(cardsList);
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Erreur lors du chargement des cartes : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void refreshCreditsTable() {
        try {
            // Effacer la liste actuelle
            ObservableList<credit> creditsList = FXCollections.observableArrayList();
            
            if (dbAvailable && currentClient != null) {
                // Charger les crédits depuis la base de données
                em.getTransaction().begin();
                
                TypedQuery<credit> query = em.createQuery(
                    "SELECT c FROM credit c WHERE c.client = :client", 
                    credit.class);
                query.setParameter("client", currentClient);
                
                List<credit> credits = query.getResultList();
                creditsList.addAll(credits);
                
                em.getTransaction().commit();
                
                System.out.println("Crédits chargés depuis la base de données: " + creditsList.size());
            } else if (currentClient != null) {
                // En mode simulation, créer des crédits de test
                System.out.println("Mode simulation : aucun crédit chargé");
            }
            
            // Mettre à jour le tableau
            creditsTable.setItems(creditsList);
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Erreur lors du chargement des crédits : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void refreshTicketsTable() {
        try {
            // Effacer la liste actuelle
            ObservableList<ticketSupport> ticketsList = FXCollections.observableArrayList();
            
            if (dbAvailable && currentClient != null) {
                // Charger les tickets depuis la base de données
                em.getTransaction().begin();
                
                TypedQuery<ticketSupport> query = em.createQuery(
                    "SELECT t FROM ticketSupport t WHERE t.client = :client", 
                    ticketSupport.class);
                query.setParameter("client", currentClient);
                
                List<ticketSupport> tickets = query.getResultList();
                ticketsList.addAll(tickets);
                
                em.getTransaction().commit();
                
                System.out.println("Tickets chargés depuis la base de données: " + ticketsList.size());
            } else if (currentClient != null) {
                // En mode simulation, créer des tickets de test
                System.out.println("Mode simulation : aucun ticket chargé");
            }
            
            // Mettre à jour le tableau
            ticketsTable.setItems(ticketsList);
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Erreur lors du chargement des tickets : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String generateCardNumber() {
        // Génération d'un numéro de carte au format: 4XXX XXXX XXXX XXXX
        StringBuilder sb = new StringBuilder("4"); // Commencer par 4 pour simuler une Visa
        for (int i = 1; i < 16; i++) {
            sb.append((int) (Math.random() * 10));
            if (i % 4 == 0 && i < 15) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
    
    private String generateCVV() {
        // Génération d'un CVV à 3 chiffres
        return String.format("%03d", (int) (Math.random() * 1000));
    }
    
    private String generatePIN() {
        // Génération d'un code PIN à 4 chiffres
        return String.format("%04d", (int) (Math.random() * 10000));
    }
    
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 16) {
            return cardNumber;
        }
        return cardNumber.substring(0, 5) + "XXXX XXXX " + cardNumber.substring(15);
    }
    
    // Méthode pour rafraîchir l'historique des transactions avec filtres
    private void refreshHistoryTable(compte selectedAccount, String selectedType, LocalDate startDate, LocalDate endDate) {
        try {
            // Effacer la liste actuelle
            ObservableList<transaction> historyList = FXCollections.observableArrayList();
            
            if (dbAvailable && currentClient != null) {
                // Charger les transactions depuis la base de données
                em.getTransaction().begin();
                
                // Construction de la requête de base
                StringBuilder queryBuilder = new StringBuilder();
                queryBuilder.append("SELECT t FROM transaction t WHERE ");
                
                // Liste des paramètres
                Map<String, Object> parameters = new HashMap<>();
                
                // Filtrage par compte
                if (selectedAccount != null) {
                    queryBuilder.append("(t.compteSource = :compte OR t.compteDest = :compte) AND ");
                    parameters.put("compte", selectedAccount);
                } else {
                    // Sinon, récupérer tous les comptes du client
                    TypedQuery<compte> compteQuery = em.createQuery(
                        "SELECT c FROM compte c WHERE c.client = :client", 
                        compte.class);
                    compteQuery.setParameter("client", currentClient);
                    List<compte> comptes = compteQuery.getResultList();
                    
                    if (!comptes.isEmpty()) {
                        queryBuilder.append("(");
                        for (int i = 0; i < comptes.size(); i++) {
                            if (i > 0) queryBuilder.append(" OR ");
                            queryBuilder.append("t.compteSource = :compte").append(i);
                            queryBuilder.append(" OR t.compteDest = :compte").append(i);
                            parameters.put("compte" + i, comptes.get(i));
                        }
                        queryBuilder.append(") AND ");
                    }
                }
                
                // Filtrage par type
                if (selectedType != null && !selectedType.equals("Tous")) {
                    queryBuilder.append("t.type = :type AND ");
                    parameters.put("type", selectedType.toLowerCase());
                }
                
                // Filtrage par date
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                
                if (startDate != null) {
                    queryBuilder.append("t.date >= :startDate AND ");
                    parameters.put("startDate", startDate.format(formatter));
                }
                
                if (endDate != null) {
                    queryBuilder.append("t.date <= :endDate AND ");
                    parameters.put("endDate", endDate.format(formatter));
                }
                
                // Retirer le dernier "AND"
                String query = queryBuilder.toString();
                if (query.endsWith("AND ")) {
                    query = query.substring(0, query.length() - 4);
                }
                
                // Ajouter l'ordre de tri par date décroissante
                query += " ORDER BY t.date DESC";
                
                // Exécuter la requête
                TypedQuery<transaction> transactionQuery = em.createQuery(query, transaction.class);
                
                // Définir les paramètres
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    transactionQuery.setParameter(entry.getKey(), entry.getValue());
                }
                
                // Récupérer les résultats
                List<transaction> transactions = transactionQuery.getResultList();
                historyList.addAll(transactions);
                
                em.getTransaction().commit();
                
                System.out.println("Historique des transactions chargé depuis la base de données: " + historyList.size());
            } else if (currentClient != null) {
                // En mode simulation, créer des transactions de test
                System.out.println("Mode simulation : création de transactions d'historique simulées");
                
                // Créer des comptes simulés
                compte compte1 = new compte();
                compte1.setId(1L);
                compte1.setNumero("FR7630001007941234567890185");
                compte1.setType("COURANT");
                compte1.setSolde(1500.0);
                
                compte compte2 = new compte();
                compte2.setId(2L);
                compte2.setNumero("FR7630004000031234567890143");
                compte2.setType("EPARGNE");
                compte2.setSolde(5000.0);
                
                // Créer des transactions simulées
                transaction t1 = new transaction();
                t1.setId(1L);
                t1.setType("depot");
                t1.setMontant(1000.0);
                t1.setDate(LocalDate.now().minusDays(30).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                t1.setStatut("valide");
                t1.setCompteSource(compte1);
                
                transaction t2 = new transaction();
                t2.setId(2L);
                t2.setType("retrait");
                t2.setMontant(200.0);
                t2.setDate(LocalDate.now().minusDays(15).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                t2.setStatut("valide");
                t2.setCompteSource(compte1);
                
                transaction t3 = new transaction();
                t3.setId(3L);
                t3.setType("virement");
                t3.setMontant(500.0);
                t3.setDate(LocalDate.now().minusDays(7).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                t3.setStatut("valide");
                t3.setCompteSource(compte1);
                t3.setCompteDest(compte2);
                
                // Ajouter à la liste d'historique
                historyList.addAll(t1, t2, t3);
            }
            
            // Configuration des colonnes pour afficher les informations de transaction
            historyDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            historyTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            
            // Colonne du compte (source ou destination selon le contexte)
            historyAccountColumn.setCellFactory(column -> {
                return new TableCell<transaction, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            transaction tx = getTableView().getItems().get(getIndex());
                            
                            // Déterminer quel compte afficher
                            if (tx.getType().equals("virement")) {
                                // Pour un virement, montrer les deux comptes
                                String source = tx.getCompteSource() != null ? formatAccountNumber(tx.getCompteSource().getNumero()) : "N/A";
                                String dest = tx.getCompteDest() != null ? formatAccountNumber(tx.getCompteDest().getNumero()) : "N/A";
                                setText(source + " → " + dest);
                            } else {
                                // Pour dépôt ou retrait, montrer le compte source
                                if (tx.getCompteSource() != null) {
                                    setText(formatAccountNumber(tx.getCompteSource().getNumero()));
                                } else {
                                    setText("N/A");
                                }
                            }
                        }
                    }
                };
            });
            
            historyAmountColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
            
            // Colorier les montants selon le type de transaction
            historyAmountColumn.setCellFactory(column -> {
                return new TableCell<transaction, Double>() {
                    @Override
                    protected void updateItem(Double amount, boolean empty) {
                        super.updateItem(amount, empty);
                        if (empty || amount == null) {
                            setText(null);
                            setStyle("");
                        } else {
                            transaction tx = getTableView().getItems().get(getIndex());
                            setText(String.format("%.2f XOF", amount));
                            
                            // Retrait en rouge, dépôt en vert
                            if (tx.getType().equals("retrait")) {
                                setStyle("-fx-text-fill: red;");
                            } else if (tx.getType().equals("depot")) {
                                setStyle("-fx-text-fill: green;");
                            } else if (tx.getType().equals("virement")) {
                                // Déterminer si c'est une sortie ou une entrée d'argent
                                boolean isOutgoing = false;
                                
                                if (selectedAccount != null) {
                                    // Cas où on filtre par compte
                                    isOutgoing = selectedAccount.equals(tx.getCompteSource());
                                } else {
                                    // Cas où on affiche tous les comptes, on considère que les virements internes sont neutres
                                    isOutgoing = tx.getCompteSource() != null && tx.getCompteDest() != null && 
                                        currentClient.equals(tx.getCompteSource().getClient()) && 
                                        !currentClient.equals(tx.getCompteDest().getClient());
                                }
                                
                                setStyle(isOutgoing ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
                            } else {
                                setStyle("");
                            }
                        }
                    }
                };
            });
            
            // Mettre à jour le tableau
            historyTable.setItems(historyList);
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Erreur lors du chargement de l'historique des transactions : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Méthode de rafraîchissement simple (sans filtres)
    private void refreshHistoryTable() {
        // Appeler la méthode avec des filtres à null
        refreshHistoryTable(null, null, null, null);
    }
    
    // Méthodes setter pour permettre au LoginController de définir le client et la disponibilité de la BDD
    public void setCurrentClient(client client) {
        this.currentClient = client;
        // Mise à jour du label avec le nom du client
        clientNameLabel.setText("Bienvenue, " + client.getPrenom() + " " + client.getNom());
        // Charger les comptes du client
        refreshAccountsTable();
        // Charger les autres données client
        refreshTransfersTable();
        refreshHistoryTable();  // Ajout du chargement de l'historique
        refreshCardsTable();
        refreshCreditsTable();
        refreshTicketsTable();
    }
    
    public void setDbAvailable(boolean available) {
        this.dbAvailable = available;
    }
}
