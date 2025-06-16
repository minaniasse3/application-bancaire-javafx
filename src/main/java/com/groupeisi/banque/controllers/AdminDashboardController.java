package com.groupeisi.banque.controllers;

import com.groupeisi.banque.App;
import com.groupeisi.banque.entities.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import jakarta.persistence.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    // EntityManager pour les opérations de base de données
    private EntityManager em;
    private boolean dbAvailable = false;
    //transaction
    @FXML
    private TableColumn<transaction, Void> transactionActionsColumn;

    @FXML
    private TableView<transaction> transactionsTable;

    @FXML
    private TableColumn<transaction, Long> transactionIdColumn;

    @FXML
    private TableColumn<transaction, String> transactionDateColumn;

    @FXML
    private TableColumn<transaction, String> transactionTypeColumn;

    @FXML
    private TableColumn<transaction, Double> transactionAmountColumn;

    @FXML
    private TableColumn<transaction, String> transactionSourceColumn;

    @FXML
    private TableColumn<transaction, String> transactionDestColumn;

    @FXML
    private TableColumn<transaction, String> transactionStatusColumn;

    private ObservableList<transaction> transactionsList = FXCollections.observableArrayList();
    // Cartes bancaires
    @FXML
    private TableView<carteBancaire> cardsTable;

    @FXML
    private TableColumn<carteBancaire, Long> cardIdColumn;

    @FXML
    private TableColumn<carteBancaire, String> cardNumberColumn;

    @FXML
    private TableColumn<carteBancaire, String> cardCvvColumn;

    @FXML
    private TableColumn<carteBancaire, String> cardExpirationColumn;

    @FXML
    private TableColumn<carteBancaire, Double> cardBalanceColumn;

    @FXML
    private TableColumn<carteBancaire, String> cardStatusColumn;

    @FXML
    private TableColumn<carteBancaire, String> cardAccountColumn;
    @FXML
    private TableColumn<credit, Void> creditActionsColumn;

    @FXML
    private TableColumn<carteBancaire, Void> cardActionsColumn;

    private ObservableList<carteBancaire> cardsList = FXCollections.observableArrayList();


    // Crédits
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
    private TableColumn<credit, String> creditDateColumn;

    @FXML
    private TableColumn<credit, String> creditStatusColumn;

    @FXML
    private TableColumn<credit, String> creditClientColumn;

    private ObservableList<credit> creditsList = FXCollections.observableArrayList();
    // Éléments d'interface utilisateur - Vues principales
    @FXML
    private VBox clientsView;

    @FXML
    private VBox accountsView;

    @FXML
    private VBox transactionsView;

    @FXML
    private VBox cardsView;

    @FXML
    private VBox creditsView;

    @FXML
    private VBox supportView;

    @FXML
    private VBox statsView;

    // Bouton de déconnexion
    @FXML
    private Button logoutButton;

    // Champs pour la gestion des clients
    @FXML
    private TextField clientSearchField;

    @FXML
    private TableView<client> clientsTable;

    @FXML
    private TableColumn<client, Long> clientIdColumn;

    @FXML
    private TableColumn<client, String> clientNameColumn;

    @FXML
    private TableColumn<client, String> clientFirstNameColumn;

    @FXML
    private TableColumn<client, String> clientEmailColumn;

    @FXML
    private TableColumn<client, String> clientPhoneColumn;

    @FXML
    private TableColumn<client, String> clientStatusColumn;

    @FXML
    private TableColumn<client, Void> clientActionsColumn;

    // Labels pour les statistiques
    @FXML
    private Label clientCountLabel;

    @FXML
    private Label accountCountLabel;

    @FXML
    private Label transactionCountLabel;

    @FXML
    private Label pendingTicketsLabel;

    // Labels pour la vue des comptes
    @FXML
    private Label totalClientsLabel;

    @FXML
    private Label totalAccountsLabel;

    @FXML
    private Label totalBalanceLabel;

    // Liste observable pour les clients
    private ObservableList<client> clientsList = FXCollections.observableArrayList();

    // Champs pour la gestion des comptes
    @FXML
    private TextField accountSearchField;

    @FXML
    private TableView<compte> accountsTable;

    @FXML
    private TableColumn<compte, Long> accountIdColumn;

    @FXML
    private TableColumn<compte, String> accountNumberColumn;

    @FXML
    private TableColumn<compte, String> accountTypeColumn;

    @FXML
    private TableColumn<compte, Double> accountBalanceColumn;

    @FXML
    private TableColumn<compte, String> accountClientColumn;

    @FXML
    private TableColumn<compte, String> accountDateColumn;

    @FXML
    private TableColumn<compte, Void> accountActionsColumn;

    // Liste observable pour les comptes
    private ObservableList<compte> accountsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialiser l'EntityManager
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("banquePU");
            em = emf.createEntityManager();
            dbAvailable = true;
            System.out.println("Connexion à la base de données établie dans AdminDashboardController");
        } catch (Exception e) {
            dbAvailable = false;
            System.out.println("Erreur de connexion à la base de données dans AdminDashboardController: " + e.getMessage());
            e.printStackTrace();
        }

        // Initialiser les colonnes de la table des clients
        setupClientTable();

        // Initialiser les colonnes de la table des comptes
        setupAccountTable();
        // Initialiser les colonnes de la table des transactions
        setupTransactionTable();
        // Charger les données de la table cartes bancaires
        setupCardTable();
        // Charger les données de la table credits
        setupCreditTable();
        // Charger les données initiales
        loadInitialData();
    }

    private void setupClientTable() {
        // Configuration des colonnes de la table des clients
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        clientFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        clientEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        clientPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        clientStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Configuration de la colonne d'actions
        setupClientActionsColumn();

        // Lier la table à la liste observable
        clientsTable.setItems(clientsList);
    }

    private void setupAccountTable() {
        // Configuration des colonnes de la table des comptes
        accountIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        accountTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        accountBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("solde"));
        accountDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));

        // Configuration de la colonne du client associé
        accountClientColumn.setCellValueFactory(cellData -> {
            client client = cellData.getValue().getClient();
            if (client != null) {
                return new SimpleStringProperty(client.getNom() + " " + client.getPrenom());
            } else {
                return new SimpleStringProperty("Non attribué");
            }
        });

        // Configuration de la colonne d'actions
        setupAccountActionsColumn();

        // Lier la table à la liste observable
        accountsTable.setItems(accountsList);
    }
    private void setupCardTable() {
        cardIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        cardNumberColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        cardCvvColumn.setCellValueFactory(new PropertyValueFactory<>("cvv"));
        cardExpirationColumn.setCellValueFactory(new PropertyValueFactory<>("dateExpiration"));
        cardBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("solde"));
        cardStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        cardAccountColumn.setCellValueFactory(cellData -> {
            compte c = cellData.getValue().getCompte();
            return new SimpleStringProperty(c != null ? c.getNumero() : "N/A");
        });
        setupCardActionsColumn();
        cardsTable.setItems(cardsList);
    }

    private void setupCreditTable() {
        creditIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        creditAmountColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        creditRateColumn.setCellValueFactory(new PropertyValueFactory<>("tauxInteret"));
        creditDurationColumn.setCellValueFactory(new PropertyValueFactory<>("dureeMois"));
        creditMonthlyColumn.setCellValueFactory(new PropertyValueFactory<>("mensualite"));
        creditDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateDemande"));
        creditStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        creditClientColumn.setCellValueFactory(cellData -> {
            client cl = cellData.getValue().getClient();
            return new SimpleStringProperty(cl != null ? cl.getNom() + " " + cl.getPrenom() : "N/A");
        });
        setupCreditActionsColumn();
        creditsTable.setItems(creditsList);
    }
    private void setupCreditActionsColumn() {
        Callback<TableColumn<credit, Void>, TableCell<credit, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<credit, Void> call(final TableColumn<credit, Void> param) {
                return new TableCell<credit, Void>() {
                    private final Button editBtn = new Button("Éditer");
                    private final Button deleteBtn = new Button("Supprimer");

                    {
                        editBtn.setStyle("-fx-background-color: #FFC107; -fx-text-fill: white; -fx-font-size: 10px;");
                        deleteBtn.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-font-size: 10px;");

                        // Action pour éditer le crédit
                        editBtn.setOnAction(event -> {
                            credit selectedCredit = getTableView().getItems().get(getIndex());
                            handleEditCredit(selectedCredit);
                        });

                        // Action pour supprimer le crédit
                        deleteBtn.setOnAction(event -> {
                            credit selectedCredit = getTableView().getItems().get(getIndex());
                            handleDeleteCredit(selectedCredit);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Créer un conteneur pour les boutons
                            Button viewBtn = new Button("Voir");
                            viewBtn.setStyle("-fx-background-color: #0D6EFD; -fx-text-fill: white; -fx-font-size: 10px;");
                            viewBtn.setOnAction(event -> {
                                credit selectedCredit = getTableView().getItems().get(getIndex());
                                handleViewCredit(selectedCredit);
                            });

                            // Ajouter les boutons au conteneur
                            javafx.scene.layout.HBox buttonsBox = new javafx.scene.layout.HBox(5);
                            buttonsBox.getChildren().addAll(viewBtn, editBtn, deleteBtn);
                            setGraphic(buttonsBox);
                        }
                    }
                };
            }
        };

        // Lier la cellule d'actions à la colonne de la TableView des crédits
        creditActionsColumn.setCellFactory(cellFactory);
    }
    private void setupCardActionsColumn() {
        Callback<TableColumn<carteBancaire, Void>, TableCell<carteBancaire, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<carteBancaire, Void> call(final TableColumn<carteBancaire, Void> param) {
                return new TableCell<carteBancaire, Void>() {
                    private final Button editBtn = new Button("Éditer");
                    private final Button deleteBtn = new Button("Supprimer");

                    {
                        editBtn.setStyle("-fx-background-color: #FFC107; -fx-text-fill: white; -fx-font-size: 10px;");
                        deleteBtn.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-font-size: 10px;");

                        // Action pour éditer la carte bancaire
                        editBtn.setOnAction(event -> {
                            carteBancaire selectedCard = getTableView().getItems().get(getIndex());
                            handleEditCard(selectedCard);
                        });

                        // Action pour supprimer la carte bancaire
                        deleteBtn.setOnAction(event -> {
                            carteBancaire selectedCard = getTableView().getItems().get(getIndex());
                            handleDeleteCard(selectedCard);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Créer un conteneur pour les boutons
                            Button viewBtn = new Button("Voir");
                            viewBtn.setStyle("-fx-background-color: #0D6EFD; -fx-text-fill: white; -fx-font-size: 10px;");
                            viewBtn.setOnAction(event -> {
                                carteBancaire selectedCard = getTableView().getItems().get(getIndex());
                                handleViewCard(selectedCard);
                            });

                            // Ajouter les boutons au conteneur
                            javafx.scene.layout.HBox buttonsBox = new javafx.scene.layout.HBox(5);
                            buttonsBox.getChildren().addAll(viewBtn, editBtn, deleteBtn);
                            setGraphic(buttonsBox);
                        }
                    }
                };
            }
        };

        // Lier la cellule d'actions à la colonne de la TableView des cartes bancaires
        cardActionsColumn.setCellFactory(cellFactory);
    }
    private void setupTransactionTable() {
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        transactionAmountColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));

        transactionSourceColumn.setCellValueFactory(cellData -> {
            compte source = cellData.getValue().getCompteSource();
            return new SimpleStringProperty(source != null ? source.getNumero() : "N/A");
        });

        transactionDestColumn.setCellValueFactory(cellData -> {
            compte dest = cellData.getValue().getCompteDest();
            return new SimpleStringProperty(dest != null ? dest.getNumero() : "N/A");
        });

        transactionStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        setupTransactionActionsColumn();
        transactionsTable.setItems(transactionsList);
    }
    private void setupTransactionActionsColumn() {
        Callback<TableColumn<transaction, Void>, TableCell<transaction, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<transaction, Void> call(final TableColumn<transaction, Void> param) {
                return new TableCell<transaction, Void>() {
                    private final Button viewBtn = new Button("Voir");
                    private final Button editBtn = new Button("Éditer");
                    private final Button deleteBtn = new Button("Supprimer");

                    {
                        viewBtn.setStyle("-fx-background-color: #0D6EFD; -fx-text-fill: white; -fx-font-size: 10px;");
                        editBtn.setStyle("-fx-background-color: #FFC107; -fx-text-fill: white; -fx-font-size: 10px;");
                        deleteBtn.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-font-size: 10px;");

                        viewBtn.setOnAction(event -> {
                            transaction selectedTransaction = getTableView().getItems().get(getIndex());
                            handleViewTransaction(selectedTransaction);
                        });

                        editBtn.setOnAction(event -> {
                            transaction selectedTransaction = getTableView().getItems().get(getIndex());
                            handleEditTransaction(selectedTransaction);
                        });

                        deleteBtn.setOnAction(event -> {
                            transaction selectedTransaction = getTableView().getItems().get(getIndex());
                            handleDeleteTransaction(selectedTransaction);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            javafx.scene.layout.HBox buttonsBox = new javafx.scene.layout.HBox(5);
                            buttonsBox.getChildren().addAll(viewBtn, editBtn, deleteBtn);
                            setGraphic(buttonsBox);
                        }
                    }
                };
            }
        };

        transactionActionsColumn.setCellFactory(cellFactory);
    }


    private void setupClientActionsColumn() {
        Callback<TableColumn<client, Void>, TableCell<client, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<client, Void> call(final TableColumn<client, Void> param) {
                return new TableCell<client, Void>() {
                    private final Button editBtn = new Button("Éditer");
                    private final Button deleteBtn = new Button("Supprimer");

                    {
                        editBtn.setStyle("-fx-background-color: #FFC107; -fx-text-fill: white; -fx-font-size: 10px;");
                        deleteBtn.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-font-size: 10px;");

                        editBtn.setOnAction(event -> {
                            client selectedClient = getTableView().getItems().get(getIndex());
                            handleEditClient(selectedClient);
                        });

                        deleteBtn.setOnAction(event -> {
                            client selectedClient = getTableView().getItems().get(getIndex());
                            handleDeleteClient(selectedClient);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Créer un conteneur pour les boutons
                            Button viewBtn = new Button("Voir");
                            viewBtn.setStyle("-fx-background-color: #0D6EFD; -fx-text-fill: white; -fx-font-size: 10px;");
                            viewBtn.setOnAction(event -> {
                                client selectedClient = getTableView().getItems().get(getIndex());
                                handleViewClient(selectedClient);
                            });

                            // Ajouter les boutons au conteneur
                            javafx.scene.layout.HBox buttonsBox = new javafx.scene.layout.HBox(5);
                            buttonsBox.getChildren().addAll(viewBtn, editBtn, deleteBtn);
                            setGraphic(buttonsBox);
                        }
                    }
                };
            }
        };

        clientActionsColumn.setCellFactory(cellFactory);
    }

    private void setupAccountActionsColumn() {
        Callback<TableColumn<compte, Void>, TableCell<compte, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<compte, Void> call(final TableColumn<compte, Void> param) {
                return new TableCell<compte, Void>() {
                    private final Button editBtn = new Button("Éditer");
                    private final Button deleteBtn = new Button("Supprimer");

                    {
                        editBtn.setStyle("-fx-background-color: #FFC107; -fx-text-fill: white; -fx-font-size: 10px;");
                        deleteBtn.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-font-size: 10px;");

                        editBtn.setOnAction(event -> {
                            compte selectedAccount = getTableView().getItems().get(getIndex());
                            handleEditAccount(selectedAccount);
                        });

                        deleteBtn.setOnAction(event -> {
                            compte selectedAccount = getTableView().getItems().get(getIndex());
                            handleDeleteAccount(selectedAccount);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Créer un conteneur pour les boutons
                            Button viewBtn = new Button("Voir");
                            viewBtn.setStyle("-fx-background-color: #0D6EFD; -fx-text-fill: white; -fx-font-size: 10px;");
                            viewBtn.setOnAction(event -> {
                                compte selectedAccount = getTableView().getItems().get(getIndex());
                                handleViewAccount(selectedAccount);
                            });

                            // Ajouter les boutons au conteneur
                            javafx.scene.layout.HBox buttonsBox = new javafx.scene.layout.HBox(5);
                            buttonsBox.getChildren().addAll(viewBtn, editBtn, deleteBtn);
                            setGraphic(buttonsBox);
                        }
                    }
                };
            }
        };

        accountActionsColumn.setCellFactory(cellFactory);
    }

    private void loadInitialData() {
        // Charger les clients
        loadClients();

        // Charger les comptes
        loadAccounts();
        // Charger les transactions
        loadTransactions();
        // Charger les cartes bancaires
        loadCards();
        // Mettre à jour les statistiques
        updateStatistics();
        // Charger les crédits
        loadCredits();
    }
    private void loadCards() {
        cardsList.clear();
        if (dbAvailable) {
            try {
                em.getTransaction().begin();
                TypedQuery<carteBancaire> query = em.createQuery("SELECT c FROM carteBancaire c", carteBancaire.class);
                List<carteBancaire> cartes = query.getResultList();
                cardsList.addAll(cartes);
                em.getTransaction().commit();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                e.printStackTrace();
            }
        }
    }

    private void loadCredits() {
        creditsList.clear();
        if (dbAvailable) {
            try {
                em.getTransaction().begin();
                TypedQuery<credit> query = em.createQuery("SELECT c FROM credit c", credit.class);
                List<credit> credits = query.getResultList();
                creditsList.addAll(credits);
                em.getTransaction().commit();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                e.printStackTrace();
            }
        }
    }
    private void loadClients() {
        clientsList.clear();

        if (dbAvailable) {
            try {
                em.getTransaction().begin();
                TypedQuery<client> query = em.createQuery("SELECT c FROM client c", client.class);
                List<client> clients = query.getResultList();
                clientsList.addAll(clients);
                em.getTransaction().commit();

                System.out.println("Nombre de clients chargés : " + clientsList.size());
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                System.out.println("Erreur lors du chargement des clients : " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur",
                        "Impossible de charger les clients : " + e.getMessage());
            }
        } else {
            // Mode simulation - ajouter des clients fictifs
            client client1 = new client();
            client1.setId(1L);
            client1.setNom("Doe");
            client1.setPrenom("John");
            client1.setEmail("john.doe@example.com");
            client1.setTelephone("123456789");
            client1.setStatut("Actif");

            client client2 = new client();
            client2.setId(2L);
            client2.setNom("Dupont");
            client2.setPrenom("Jean");
            client2.setEmail("jean.dupont@example.com");
            client2.setTelephone("987654321");
            client2.setStatut("Actif");

            clientsList.addAll(client1, client2);

            System.out.println("Clients fictifs chargés en mode simulation");
        }
    }

    private void loadAccounts() {
        accountsList.clear();

        if (dbAvailable) {
            try {
                em.getTransaction().begin();
                TypedQuery<compte> query = em.createQuery("SELECT c FROM compte c", compte.class);
                List<compte> accounts = query.getResultList();
                accountsList.addAll(accounts);
                em.getTransaction().commit();

                System.out.println("Nombre de comptes chargés : " + accountsList.size());
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                System.out.println("Erreur lors du chargement des comptes : " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur",
                        "Impossible de charger les comptes : " + e.getMessage());
            }
        } else {
            // Mode simulation - ajouter des comptes fictifs
            compte compte1 = new compte();
            compte1.setId(1L);
            compte1.setNumero("FR7630001007941234567890185");
            compte1.setType("Courant");
            compte1.setSolde(1250.75);
            compte1.setDateCreation("2023-01-15");

            compte compte2 = new compte();
            compte2.setId(2L);
            compte2.setNumero("FR7630004000031234567890143");
            compte2.setType("Épargne");
            compte2.setSolde(5490.30);
            compte2.setDateCreation("2023-02-22");

            accountsList.addAll(compte1, compte2);

            System.out.println("Comptes fictifs chargés en mode simulation");
        }
    }
    private void loadTransactions() {
        if (dbAvailable) {
            try {
                em.getTransaction().begin();
                TypedQuery<transaction> query = em.createQuery("SELECT t FROM transaction t", transaction.class);
                List<transaction> transactions = query.getResultList();
                transactionsList.setAll(transactions);
                em.getTransaction().commit();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                System.out.println("Erreur lors du chargement des transactions : " + e.getMessage());
            }
        }
    }

    private void updateStatistics() {
        if (dbAvailable) {
            try {
                // Récupérer le nombre de clients
                Query clientQuery = em.createQuery("SELECT COUNT(c.id) FROM client c");
                Long clientCount = (Long) clientQuery.getSingleResult();
                totalClientsLabel.setText(clientCount.toString());

                // Récupérer le nombre de comptes
                Query accountQuery = em.createQuery("SELECT COUNT(c.id) FROM compte c");
                Long accountCount = (Long) accountQuery.getSingleResult();
                totalAccountsLabel.setText(accountCount.toString());

                // Récupérer le solde total
                Query balanceQuery = em.createQuery("SELECT SUM(c.solde) FROM compte c");
                Double totalBalance = (Double) balanceQuery.getSingleResult();
                if (totalBalance == null) totalBalance = 0.0;
                totalBalanceLabel.setText(String.format("%.2f XOF", totalBalance));

                // Récupérer le nombre de transactions
                Query transactionQuery = em.createQuery("SELECT COUNT(t.id) FROM transaction t");
                Long transactionCount = (Long) transactionQuery.getSingleResult();

                // Récupérer le nombre de tickets de support ouverts
                Query ticketQuery = em.createQuery("SELECT COUNT(ts.id) FROM ticketSupport ts WHERE UPPER(ts.statut) = 'OUVERT'");
                Long ticketCount = (Long) ticketQuery.getSingleResult();

                // Si les labels existent dans la vue statistiques, les mettre à jour
                if (clientCountLabel != null) clientCountLabel.setText(clientCount.toString());
                if (accountCountLabel != null) accountCountLabel.setText(accountCount.toString());
                if (transactionCountLabel != null) transactionCountLabel.setText(transactionCount.toString());
                if (pendingTicketsLabel != null) pendingTicketsLabel.setText(ticketCount.toString());
            } catch (Exception e) {
                System.out.println("Erreur lors de la requête à la base de données : " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            // Mode simulation
            totalClientsLabel.setText("4");
            totalAccountsLabel.setText("6");
            totalBalanceLabel.setText("12500.00 XOF");

            if (clientCountLabel != null) clientCountLabel.setText("4");
            if (accountCountLabel != null) accountCountLabel.setText("6");
            if (transactionCountLabel != null) transactionCountLabel.setText("10");
            if (pendingTicketsLabel != null) pendingTicketsLabel.setText("2");
        }
    }

    // Méthodes de navigation entre les vues
    @FXML
    public void showClients() {
        clientsView.setVisible(true);
        accountsView.setVisible(false);
        transactionsView.setVisible(false);
        cardsView.setVisible(false);
        creditsView.setVisible(false);
        supportView.setVisible(false);
        statsView.setVisible(false);

        // Recharger les clients
        loadClients();
    }

    @FXML
    public void showAccounts() {
        clientsView.setVisible(false);
        accountsView.setVisible(true);
        transactionsView.setVisible(false);
        cardsView.setVisible(false);
        creditsView.setVisible(false);
        supportView.setVisible(false);
        statsView.setVisible(false);

        // Recharger les comptes
        loadAccounts();
    }

    @FXML
    public void showTransactions() {
        clientsView.setVisible(false);
        accountsView.setVisible(false);
        transactionsView.setVisible(true);
        cardsView.setVisible(false);
        creditsView.setVisible(false);
        supportView.setVisible(false);
        statsView.setVisible(false);
        loadTransactions();

    }

    @FXML
    public void showCards() {
        clientsView.setVisible(false);
        accountsView.setVisible(false);
        transactionsView.setVisible(false);
        cardsView.setVisible(true);
        creditsView.setVisible(false);
        supportView.setVisible(false);
        statsView.setVisible(false);
        loadCards();
    }

    @FXML
    public void showCredits() {
        clientsView.setVisible(false);
        accountsView.setVisible(false);
        transactionsView.setVisible(false);
        cardsView.setVisible(false);
        creditsView.setVisible(true);
        supportView.setVisible(false);
        statsView.setVisible(false);
        loadCredits();
    }

    @FXML
    public void showSupport() {
        clientsView.setVisible(false);
        accountsView.setVisible(false);
        transactionsView.setVisible(false);
        cardsView.setVisible(false);
        creditsView.setVisible(false);
        supportView.setVisible(true);
        statsView.setVisible(false);
    }

    @FXML
    public void showStats() {
        clientsView.setVisible(false);
        accountsView.setVisible(false);
        transactionsView.setVisible(false);
        cardsView.setVisible(false);
        creditsView.setVisible(false);
        supportView.setVisible(false);
        statsView.setVisible(true);

        // Mettre à jour les statistiques
        updateStatistics();
    }

    // Méthodes de gestion des clients
    @FXML
    public void handleSearchClient() {
        String searchText = clientSearchField.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            // Si le champ de recherche est vide, afficher tous les clients
            loadClients();
            return;
        }

        ObservableList<client> filteredList = FXCollections.observableArrayList();

        if (dbAvailable) {
            try {
                TypedQuery<client> query = em.createQuery(
                    "SELECT c FROM client c WHERE LOWER(c.nom) LIKE :search " +
                    "OR LOWER(c.prenom) LIKE :search " +
                    "OR LOWER(c.email) LIKE :search " +
                    "OR LOWER(c.telephone) LIKE :search",
                    client.class);
                query.setParameter("search", "%" + searchText + "%");

                List<client> clients = query.getResultList();
                filteredList.addAll(clients);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur",
                        "Erreur lors de la recherche : " + e.getMessage());
            }
        } else {
            // Mode simulation - filtrer les clients existants
            for (client c : clientsList) {
                if (c.getNom().toLowerCase().contains(searchText) ||
                    c.getPrenom().toLowerCase().contains(searchText) ||
                    c.getEmail().toLowerCase().contains(searchText) ||
                    c.getTelephone().toLowerCase().contains(searchText)) {
                    filteredList.add(c);
                }
            }
        }

        clientsTable.setItems(filteredList);
    }

    @FXML
    public void handleNewClient() {
        try {
            // Créer une nouvelle fenêtre pour l'ajout de client
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter un nouveau client");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(logoutButton.getScene().getWindow());

            // Créer un formulaire pour l'ajout de client
            FXMLLoader loader = new FXMLLoader(App.class.getResource("client-form.fxml"));
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            // Configurer le contrôleur du formulaire
            ClientFormController controller = loader.getController();
            controller.setEntityManager(em);
            controller.setDbAvailable(dbAvailable);
            controller.setDialogStage(dialogStage);
            controller.setNewClient(true);

            // Afficher la fenêtre et attendre qu'elle soit fermée
            dialogStage.showAndWait();

            // Recharger la liste des clients
            loadClients();

            // Mettre à jour les statistiques
            updateStatistics();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible de charger le formulaire d'ajout de client : " + e.getMessage());
        }
    }

    private void handleViewClient(client selectedClient) {
        showAlert(Alert.AlertType.INFORMATION, "Détails du client",
                "ID: " + selectedClient.getId() + "\n" +
                "Nom: " + selectedClient.getNom() + "\n" +
                "Prénom: " + selectedClient.getPrenom() + "\n" +
                "Email: " + selectedClient.getEmail() + "\n" +
                "Téléphone: " + selectedClient.getTelephone() + "\n" +
                "Adresse: " + selectedClient.getAdresse() + "\n" +
                "Statut: " + selectedClient.getStatut());
    }

    private void handleEditClient(client selectedClient) {
        try {
            // Créer une nouvelle fenêtre pour l'édition de client
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modifier le client");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(logoutButton.getScene().getWindow());

            // Créer un formulaire pour l'édition de client
            FXMLLoader loader = new FXMLLoader(App.class.getResource("client-form.fxml"));
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            // Configurer le contrôleur du formulaire
            ClientFormController controller = loader.getController();
            controller.setEntityManager(em);
            controller.setDbAvailable(dbAvailable);
            controller.setDialogStage(dialogStage);
            controller.setClient(selectedClient);
            controller.setNewClient(false);

            // Afficher la fenêtre et attendre qu'elle soit fermée
            dialogStage.showAndWait();

            // Recharger la liste des clients
            loadClients();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible de charger le formulaire d'édition de client : " + e.getMessage());
        }
    }

    private void handleDeleteClient(client selectedClient) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation de suppression");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer le client " +
                selectedClient.getPrenom() + " " + selectedClient.getNom() + " ?");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (dbAvailable) {
                    try {
                        em.getTransaction().begin();
                        // Récupérer une référence à l'entité client à partir de la base de données
                        client clientToDelete = em.find(client.class, selectedClient.getId());
                        if (clientToDelete != null) {
                            em.remove(clientToDelete);
                            em.getTransaction().commit();

                            // Recharger la liste des clients
                            loadClients();

                            // Mettre à jour les statistiques
                            updateStatistics();

                            showAlert(Alert.AlertType.INFORMATION, "Suppression réussie",
                                    "Le client a été supprimé avec succès.");
                        } else {
                            em.getTransaction().rollback();
                            showAlert(Alert.AlertType.ERROR, "Erreur",
                                    "Le client n'a pas été trouvé dans la base de données.");
                        }
                    } catch (Exception e) {
                        if (em.getTransaction().isActive()) {
                            em.getTransaction().rollback();
                        }
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Erreur",
                                "Erreur lors de la suppression du client : " + e.getMessage());
                    }
                } else {
                    // Mode simulation - supprimer de la liste observable
                    clientsList.remove(selectedClient);
                    showAlert(Alert.AlertType.INFORMATION, "Suppression réussie",
                            "Le client a été supprimé en mode simulation.");

                    // Mettre à jour les statistiques
                    updateStatistics();
                }
            }
        });
    }
    @FXML
    public void handleNewTransaction() {
        try {
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter une nouvelle transaction");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(transactionsTable.getScene().getWindow());

            FXMLLoader loader = new FXMLLoader(App.class.getResource("transaction-form.fxml"));
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            TransactionFormController controller = loader.getController();
            controller.setEntityManager(em);
            controller.setDbAvailable(dbAvailable);
            controller.setDialogStage(dialogStage);
            controller.setNewTransaction(true);

            dialogStage.showAndWait();

            loadTransactions();
            updateStatistics();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible de charger le formulaire d'ajout de transaction : " + e.getMessage());
        }
    }

    private void handleViewTransaction(transaction selectedTransaction) {
        showAlert(Alert.AlertType.INFORMATION, "Détails de la transaction",
                "ID: " + selectedTransaction.getId() + "\n" +
                        "Date: " + selectedTransaction.getDate() + "\n" +
                        "Type: " + selectedTransaction.getType() + "\n" +
                        "Montant: " + selectedTransaction.getMontant() + "\n" +
                        "Source: " + (selectedTransaction.getCompteSource() != null ? selectedTransaction.getCompteSource().getNumero() : "N/A") + "\n" +
                        "Destination: " + (selectedTransaction.getCompteDest() != null ? selectedTransaction.getCompteDest().getNumero() : "N/A") + "\n" +
                        "Statut: " + selectedTransaction.getStatut());
    }

    private void handleEditTransaction(transaction selectedTransaction) {
        try {
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modifier la transaction");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(transactionsTable.getScene().getWindow());

            FXMLLoader loader = new FXMLLoader(App.class.getResource("transaction-form.fxml"));
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            TransactionFormController controller = loader.getController();
            controller.setEntityManager(em);
            controller.setDbAvailable(dbAvailable);
            controller.setDialogStage(dialogStage);
            controller.setTransaction(selectedTransaction);
            controller.setNewTransaction(false);

            dialogStage.showAndWait();

            loadTransactions();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible de charger le formulaire d'édition de transaction : " + e.getMessage());
        }
    }

    private void handleDeleteTransaction(transaction selectedTransaction) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation de suppression");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer cette transaction ?");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (dbAvailable) {
                    try {
                        em.getTransaction().begin();
                        transaction transactionToDelete = em.find(transaction.class, selectedTransaction.getId());
                        if (transactionToDelete != null) {
                            em.remove(transactionToDelete);
                            em.getTransaction().commit();

                            loadTransactions();
                            updateStatistics();

                            showAlert(Alert.AlertType.INFORMATION, "Suppression réussie",
                                    "La transaction a été supprimée avec succès.");
                        } else {
                            em.getTransaction().rollback();
                            showAlert(Alert.AlertType.ERROR, "Erreur",
                                    "La transaction n'a pas été trouvée dans la base de données.");
                        }
                    } catch (Exception e) {
                        if (em.getTransaction().isActive()) {
                            em.getTransaction().rollback();
                        }
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Erreur",
                                "Erreur lors de la suppression de la transaction : " + e.getMessage());
                    }
                } else {
                    transactionsList.remove(selectedTransaction);
                    showAlert(Alert.AlertType.INFORMATION, "Suppression réussie",
                            "La transaction a été supprimée en mode simulation.");

                    updateStatistics();
                }
            }
        });
    }

    @FXML

    public void handleNewCredit() {
        try {
            // Créer une nouvelle fenêtre pour l'ajout d'un crédit
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter un nouveau crédit");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(logoutButton.getScene().getWindow());

            // Charger le formulaire de crédit
            FXMLLoader loader = new FXMLLoader(App.class.getResource("credit-form.fxml"));
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            // Passer le contrôleur et les données nécessaires
            CreditFormController controller = loader.getController();
            controller.setEntityManager(em);
            controller.setDbAvailable(dbAvailable);
            controller.setDialogStage(dialogStage);

            // Ici on définit directement la variable newCredit dans le contrôleur
            controller.setNewCredit(true);  // Vous pouvez directement initialiser cette valeur

            // Afficher le formulaire et attendre sa fermeture
            dialogStage.showAndWait();

            // Recharger les données de crédits
            loadCredits();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible de charger le formulaire d'ajout de crédit : " + e.getMessage());
        }
    }
    private void handleViewCredit(credit selectedCredit) {
        showAlert(Alert.AlertType.INFORMATION, "Détails du crédit",
                "ID: " + selectedCredit.getId() + "\n" +
                        "Montant: " + selectedCredit.getMontant() + "\n" +
                        "Taux d'intérêt: " + selectedCredit.getTauxInteret() + "\n" +
                        "Durée (mois): " + selectedCredit.getDuree_mois() + "\n" +
                        "Mensualité: " + selectedCredit.getMensualite() + "\n" +
                        "Date de demande: " + selectedCredit.getDateDemande() + "\n" +
                        "Statut: " + selectedCredit.getStatut());
    }

    private void handleEditCredit(credit selectedCredit) {
        try {
            // Créer une nouvelle fenêtre pour l'édition du crédit
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modifier le crédit");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(logoutButton.getScene().getWindow());

            // Créer un formulaire pour l'édition du crédit
            FXMLLoader loader = new FXMLLoader(App.class.getResource("credit-form.fxml"));
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            // Configurer le contrôleur du formulaire
            CreditFormController controller = loader.getController();
            controller.setEntityManager(em);
            controller.setDbAvailable(dbAvailable);
            controller.setDialogStage(dialogStage);
            controller.setCredit(selectedCredit);
            controller.setNewCredit(false);

            // Afficher la fenêtre et attendre qu'elle soit fermée
            dialogStage.showAndWait();

            // Recharger la liste des crédits
            loadCredits();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible de charger le formulaire d'édition de crédit : " + e.getMessage());
        }
    }
    private void handleDeleteCredit(credit selectedCredit) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation de suppression");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer le crédit de " +
                selectedCredit.getClient().getNom() + " ?");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (dbAvailable) {
                    try {
                        em.getTransaction().begin();
                        // Récupérer une référence à l'entité crédit à partir de la base de données
                        credit creditToDelete = em.find(credit.class, selectedCredit.getId());
                        if (creditToDelete != null) {
                            em.remove(creditToDelete);
                            em.getTransaction().commit();

                            // Recharger la liste des crédits
                            loadCredits();

                            showAlert(Alert.AlertType.INFORMATION, "Suppression réussie",
                                    "Le crédit a été supprimé avec succès.");
                        } else {
                            em.getTransaction().rollback();
                            showAlert(Alert.AlertType.ERROR, "Erreur",
                                    "Le crédit n'a pas été trouvé dans la base de données.");
                        }
                    } catch (Exception e) {
                        if (em.getTransaction().isActive()) {
                            em.getTransaction().rollback();
                        }
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Erreur",
                                "Erreur lors de la suppression du crédit : " + e.getMessage());
                    }
                } else {
                    // Mode simulation - supprimer de la liste observable
                    creditsList.remove(selectedCredit);
                    showAlert(Alert.AlertType.INFORMATION, "Suppression réussie",
                            "Le crédit a été supprimé en mode simulation.");
                }
            }
        });
    }



    @FXML
    public void handleNewCard() {
        try {
            // Create a new stage for adding a bank card
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter une nouvelle carte bancaire");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(logoutButton.getScene().getWindow());

            // Load the card form
            FXMLLoader loader = new FXMLLoader(App.class.getResource("carte-form.fxml"));
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            // Set the controller and pass necessary data
            CarteFormController controller = loader.getController();
            controller.setEntityManager(em);
            controller.setDbAvailable(dbAvailable);
            controller.setDialogStage(dialogStage);
            controller.setNewCard(true);

            // Show the form and wait for it to close
            dialogStage.showAndWait();

            // Reload the card data
            loadCards();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible de charger le formulaire d'ajout de carte bancaire : " + e.getMessage());
        }
    }
    private void handleViewCard(carteBancaire selectedCard) {
        showAlert(Alert.AlertType.INFORMATION, "Détails de la carte bancaire",
                "ID: " + selectedCard.getId() + "\n" +
                        "Numéro: " + selectedCard.getNumero() + "\n" +
                        "CVV: " + selectedCard.getCvv() + "\n" +
                        "Expiration: " + selectedCard.getDateExpiration() + "\n" +
                        "Solde: " + selectedCard.getSolde() + "\n" +
                        "Statut: " + selectedCard.getStatut());
    }
    private void handleEditCard(carteBancaire selectedCard) {
        try {
            // Créer une nouvelle fenêtre pour l'édition de la carte bancaire
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modifier la carte bancaire");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(logoutButton.getScene().getWindow());

            // Créer un formulaire pour l'édition de la carte bancaire
            FXMLLoader loader = new FXMLLoader(App.class.getResource("carte-form.fxml"));
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            // Configurer le contrôleur du formulaire
            CarteFormController controller = loader.getController();
            controller.setEntityManager(em);
            controller.setDbAvailable(dbAvailable);
            controller.setDialogStage(dialogStage);
            controller.setCarte(selectedCard);
            controller.setNewCard(false);

            // Afficher la fenêtre et attendre qu'elle soit fermée
            dialogStage.showAndWait();

            // Recharger la liste des cartes bancaires
            loadCards();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible de charger le formulaire d'édition de carte bancaire : " + e.getMessage());
        }
    }
    private void handleDeleteCard(carteBancaire selectedCard) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation de suppression");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer la carte bancaire " +
                selectedCard.getNumero() + " ?");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (dbAvailable) {
                    try {
                        em.getTransaction().begin();
                        // Récupérer une référence à l'entité carte bancaire à partir de la base de données
                        carteBancaire cardToDelete = em.find(carteBancaire.class, selectedCard.getId());
                        if (cardToDelete != null) {
                            em.remove(cardToDelete);
                            em.getTransaction().commit();

                            // Recharger la liste des cartes bancaires
                            loadCards();

                            showAlert(Alert.AlertType.INFORMATION, "Suppression réussie",
                                    "La carte bancaire a été supprimée avec succès.");
                        } else {
                            em.getTransaction().rollback();
                            showAlert(Alert.AlertType.ERROR, "Erreur",
                                    "La carte bancaire n'a pas été trouvée dans la base de données.");
                        }
                    } catch (Exception e) {
                        if (em.getTransaction().isActive()) {
                            em.getTransaction().rollback();
                        }
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Erreur",
                                "Erreur lors de la suppression de la carte bancaire : " + e.getMessage());
                    }
                } else {
                    // Mode simulation - supprimer de la liste observable
                    cardsList.remove(selectedCard);
                    showAlert(Alert.AlertType.INFORMATION, "Suppression réussie",
                            "La carte bancaire a été supprimée en mode simulation.");
                }
            }
        });
    }


    // Méthodes de gestion des comptes
    @FXML
    public void handleSearchAccount() {
        String searchText = accountSearchField.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            // Si le champ de recherche est vide, afficher tous les comptes
            loadAccounts();
            return;
        }

        ObservableList<compte> filteredList = FXCollections.observableArrayList();

        if (dbAvailable) {
            try {
                TypedQuery<compte> query = em.createQuery(
                    "SELECT c FROM compte c WHERE LOWER(c.numero) LIKE :search " +
                    "OR LOWER(c.type) LIKE :search",
                    compte.class);
                query.setParameter("search", "%" + searchText + "%");

                List<compte> accounts = query.getResultList();
                filteredList.addAll(accounts);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur",
                        "Erreur lors de la recherche : " + e.getMessage());
            }
        } else {
            // Mode simulation - filtrer les comptes existants
            for (compte c : accountsList) {
                if (c.getNumero().toLowerCase().contains(searchText) ||
                    c.getType().toLowerCase().contains(searchText)) {
                    filteredList.add(c);
                }
            }
        }

        accountsTable.setItems(filteredList);
    }

    private void handleViewAccount(compte selectedAccount) {
        StringBuilder detailsBuilder = new StringBuilder();
        detailsBuilder.append("ID: ").append(selectedAccount.getId()).append("\n");
        detailsBuilder.append("Numéro: ").append(selectedAccount.getNumero()).append("\n");
        detailsBuilder.append("Type: ").append(selectedAccount.getType()).append("\n");
        detailsBuilder.append("Solde: ").append(String.format("%.2f XOF", selectedAccount.getSolde())).append("\n");
        detailsBuilder.append("Date de création: ").append(selectedAccount.getDateCreation()).append("\n");

        // Afficher le client associé
        String clientInfo = "Non attribué";
        if (selectedAccount.getClient() != null) {
            client accountClient = selectedAccount.getClient();
            clientInfo = accountClient.getNom() + " " + accountClient.getPrenom() +
                    " (" + accountClient.getEmail() + ")";
        }
        detailsBuilder.append("Client: ").append(clientInfo).append("\n\n");

        // Afficher les 5 dernières transactions si la base de données est disponible
        if (dbAvailable) {
            try {
                detailsBuilder.append("Dernières transactions:\n");

                TypedQuery<transaction> query = em.createQuery(
                    "SELECT t FROM transaction t WHERE t.compteSource.id = :accountId OR t.compteDest.id = :accountId " +
                    "ORDER BY t.id DESC", transaction.class);
                query.setParameter("accountId", selectedAccount.getId());
                query.setMaxResults(5);

                List<transaction> transactions = query.getResultList();

                if (transactions.isEmpty()) {
                    detailsBuilder.append("Aucune transaction trouvée pour ce compte.\n");
                } else {
                    for (transaction t : transactions) {
                        detailsBuilder.append("- ").append(t.getType()).append(" | ")
                             .append(t.getDate()).append(" | ")
                             .append(String.format("%.2f XOF", t.getMontant())).append(" | ")
                             .append(t.getStatut()).append("\n");
                    }
                }
            } catch (Exception e) {
                detailsBuilder.append("Erreur lors de la récupération des transactions: ").append(e.getMessage());
            }
        }

        showAlert(Alert.AlertType.INFORMATION, "Détails du compte", detailsBuilder.toString());
    }

    @FXML
    public void handleNewAccount() {
        try {
            // Créer une nouvelle fenêtre pour l'ajout de compte
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter un nouveau compte");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(logoutButton.getScene().getWindow());

            // Créer un formulaire pour l'ajout de compte
            FXMLLoader loader = new FXMLLoader(App.class.getResource("account-form.fxml"));
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            // Configurer le contrôleur du formulaire
            AccountFormController controller = loader.getController();
            controller.setEntityManager(em);
            controller.setDbAvailable(dbAvailable);
            controller.setDialogStage(dialogStage);
            controller.setNewAccount(true);
            controller.setCompte(null);

            // Afficher la fenêtre et attendre qu'elle soit fermée
            dialogStage.showAndWait();

            // Recharger la liste des comptes
            loadAccounts();

            // Mettre à jour les statistiques
            updateStatistics();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible de charger le formulaire d'ajout de compte : " + e.getMessage());
        }
    }

    private void handleEditAccount(compte selectedAccount) {
        try {
            // Créer une nouvelle fenêtre pour l'édition de compte
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modifier le compte");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(logoutButton.getScene().getWindow());

            // Créer un formulaire pour l'édition de compte
            FXMLLoader loader = new FXMLLoader(App.class.getResource("account-form.fxml"));
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            // Configurer le contrôleur du formulaire
            AccountFormController controller = loader.getController();
            controller.setEntityManager(em);
            controller.setDbAvailable(dbAvailable);
            controller.setDialogStage(dialogStage);
            controller.setCompte(selectedAccount);
            controller.setNewAccount(false);

            // Afficher la fenêtre et attendre qu'elle soit fermée
            dialogStage.showAndWait();

            // Recharger la liste des comptes
            loadAccounts();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible de charger le formulaire d'édition de compte : " + e.getMessage());
        }
    }

    private void handleDeleteAccount(compte selectedAccount) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation de suppression");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer le compte " +
                selectedAccount.getNumero() + " ?");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (dbAvailable) {
                    try {
                        em.getTransaction().begin();
                        // Récupérer une référence à l'entité compte à partir de la base de données
                        compte accountToDelete = em.find(compte.class, selectedAccount.getId());
                        if (accountToDelete != null) {
                            em.remove(accountToDelete);
                            em.getTransaction().commit();

                            // Recharger la liste des comptes
                            loadAccounts();

                            // Mettre à jour les statistiques
                            updateStatistics();

                            showAlert(Alert.AlertType.INFORMATION, "Suppression réussie",
                                    "Le compte a été supprimé avec succès.");
                        } else {
                            em.getTransaction().rollback();
                            showAlert(Alert.AlertType.ERROR, "Erreur",
                                    "Le compte n'a pas été trouvé dans la base de données.");
                        }
                    } catch (Exception e) {
                        if (em.getTransaction().isActive()) {
                            em.getTransaction().rollback();
                        }
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Erreur",
                                "Erreur lors de la suppression du compte : " + e.getMessage());
                    }
                } else {
                    // Mode simulation - supprimer de la liste observable
                    accountsList.remove(selectedAccount);
                    showAlert(Alert.AlertType.INFORMATION, "Suppression réussie",
                            "Le compte a été supprimé en mode simulation.");

                    // Mettre à jour les statistiques
                    updateStatistics();
                }
            }
        });
    }

    // Méthodes de gestion des transactions
    @FXML
    public void handleFilterTransactions() {
        // Fonctionnalité à implémenter
        showAlert(Alert.AlertType.INFORMATION, "Filtrage",
                "Fonctionnalité de filtrage des transactions à implémenter.");
    }

    // Méthodes de gestion des cartes
    @FXML
    public void handleFilterCards() {
        // Fonctionnalité à implémenter
        showAlert(Alert.AlertType.INFORMATION, "Filtrage",
                "Fonctionnalité de filtrage des cartes à implémenter.");
    }

    // Méthodes de gestion des crédits
    @FXML
    public void handleFilterCredits() {
        // Fonctionnalité à implémenter
        showAlert(Alert.AlertType.INFORMATION, "Filtrage",
                "Fonctionnalité de filtrage des crédits à implémenter.");
    }

    // Méthodes de gestion du support
    @FXML
    public void handleFilterTickets() {
        // Fonctionnalité à implémenter
        showAlert(Alert.AlertType.INFORMATION, "Filtrage",
                "Fonctionnalité de filtrage des tickets à implémenter.");
    }

    @FXML
    public void handleLogout() {
        try {
            // Fermer l'EntityManager
            if (em != null && em.isOpen()) {
                em.close();
            }

            // Charger la vue de connexion
            FXMLLoader loader = new FXMLLoader(App.class.getResource("login-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setTitle("Connexion - Système Bancaire");
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible de charger la page de connexion : " + e.getMessage());
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