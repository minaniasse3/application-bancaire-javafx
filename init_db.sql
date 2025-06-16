-- Script d'initialisation de la base de données pour le Système Bancaire

-- Suppression et recréation de la base de données
DROP DATABASE IF EXISTS banque;
CREATE DATABASE banque;
USE banque;

-- Tables principales

-- Création de la table client
CREATE TABLE IF NOT EXISTS client (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telephone VARCHAR(20),
    adresse TEXT,
    dateInscription VARCHAR(10),
    statut VARCHAR(10) DEFAULT 'ACTIF'
);

-- Création de la table admin
CREATE TABLE IF NOT EXISTS admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'ADMIN'
);

-- Création de la table compte
CREATE TABLE IF NOT EXISTS compte (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(34) UNIQUE NOT NULL,
    type VARCHAR(20) NOT NULL,
    solde DOUBLE DEFAULT 0,
    dateCreation VARCHAR(10),
    client_id BIGINT,
    FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE
);

-- Création de la table transaction
CREATE TABLE IF NOT EXISTS transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(20) NOT NULL,
    montant DOUBLE NOT NULL,
    date VARCHAR(10),
    statut VARCHAR(20) DEFAULT 'VALIDE',
    motif VARCHAR(255),
    compte_source_id BIGINT,
    compte_dest_id BIGINT,
    FOREIGN KEY (compte_source_id) REFERENCES compte(id) ON DELETE CASCADE,
    FOREIGN KEY (compte_dest_id) REFERENCES compte(id) ON DELETE SET NULL
);

-- Création de la table carte_bancaire
CREATE TABLE IF NOT EXISTS carteBancaire (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(16) UNIQUE NOT NULL,
    cvv VARCHAR(3) NOT NULL,
    dateExpiration VARCHAR(5) NOT NULL,
    codePin VARCHAR(255),
    solde DOUBLE DEFAULT 0,
    statut VARCHAR(20) DEFAULT 'ACTIVE',
    compte_id BIGINT,
    FOREIGN KEY (compte_id) REFERENCES compte(id) ON DELETE CASCADE
);

-- Création de la table credit
CREATE TABLE IF NOT EXISTS credit (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    montant DOUBLE NOT NULL,
    tauxInteret DOUBLE NOT NULL,
    duree_mois INT NOT NULL,
    mensualite DOUBLE NOT NULL,
    dateDemande VARCHAR(10),
    statut VARCHAR(20) DEFAULT 'EN_COURS',
    client_id BIGINT,
    FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE
);

-- Création de la table remboursement
CREATE TABLE IF NOT EXISTS remboursement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    montant DOUBLE NOT NULL,
    date VARCHAR(10),
    credit_id BIGINT,
    FOREIGN KEY (credit_id) REFERENCES credit(id) ON DELETE CASCADE
);

-- Création de la table ticket_support
CREATE TABLE IF NOT EXISTS ticketSupport (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sujet VARCHAR(255) NOT NULL,
    description TEXT,
    dateOuverture VARCHAR(10),
    statut VARCHAR(20) DEFAULT 'OUVERT',
    client_id BIGINT,
    admin_id BIGINT,
    FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE,
    FOREIGN KEY (admin_id) REFERENCES admin(id) ON DELETE SET NULL
);

-- Création de la table frais_bancaire
CREATE TABLE IF NOT EXISTS fraisBancaire (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    montant DOUBLE NOT NULL,
    dateApplication VARCHAR(10),
    compte_id BIGINT,
    FOREIGN KEY (compte_id) REFERENCES compte(id) ON DELETE CASCADE
);

-- Insertion de données de test

-- Insertion d'administrateurs
INSERT INTO admin (username, password, role) VALUES ('admin@banque.com', 'admin123', 'ADMIN');

-- Insertion de clients
INSERT INTO client (nom, prenom, email, telephone, adresse, dateInscription, statut)
VALUES ('Admin', 'System', 'admin@banque.com', '123456789', 'Banque Centrale', '2025-01-01', 'ADMIN');

INSERT INTO client (nom, prenom, email, telephone, adresse, dateInscription, statut)
VALUES ('Dupont', 'Jean', 'jean.dupont@example.com', '123456789', '123 Rue de Paris', '2025-01-15', 'ACTIF');

INSERT INTO client (nom, prenom, email, telephone, adresse, dateInscription, statut)
VALUES ('Martin', 'Sophie', 'sophie.martin@example.com', '987654321', '456 Avenue de Lyon', '2025-02-01', 'ACTIF');

-- Insertion de comptes
INSERT INTO compte (numero, type, solde, dateCreation, client_id)
VALUES ('FR7630001007941234567890185', 'COURANT', 1500.75, '2025-01-15', 2);

INSERT INTO compte (numero, type, solde, dateCreation, client_id)
VALUES ('FR7630004000031234567890143', 'EPARGNE', 5000.50, '2025-01-20', 2);

INSERT INTO compte (numero, type, solde, dateCreation, client_id)
VALUES ('FR7630006000011234567890189', 'COURANT', 2500.25, '2025-02-05', 3);

-- Insertion de transactions
INSERT INTO transaction (type, montant, date, statut, motif, compte_source_id, compte_dest_id)
VALUES ('DEPOT', 1000.00, '2025-01-16', 'VALIDE', 'Dépôt initial', 1, NULL);

INSERT INTO transaction (type, montant, date, statut, motif, compte_source_id, compte_dest_id)
VALUES ('VIREMENT', 500.00, '2025-01-25', 'VALIDE', 'Virement mensuel', 1, 3);

INSERT INTO transaction (type, montant, date, statut, motif, compte_source_id, compte_dest_id)
VALUES ('RETRAIT', 200.00, '2025-02-10', 'VALIDE', 'Retrait DAB', 3, NULL);

-- Insertion de cartes bancaires
INSERT INTO carteBancaire (numero, cvv, dateExpiration, solde, statut, codePin, compte_id) VALUES
('4111111111111111', '123', '12/25', 1000.0, 'active', '1234', 1),
('5500000000000004', '456', '12/27', 2500.0, 'active', '5678', 3);

-- Insertion de crédits
INSERT INTO credit (montant, tauxInteret, duree_mois, mensualite, dateDemande, statut, client_id) VALUES
(5000.0, 3.5, 12, 433.33, '2023-06-15', 'En cours', 1),
(10000.00, 3.5, 24, 433.33, '2024-03-19', 'Approuvé', 2);

-- Insertion de remboursements
INSERT INTO remboursement (montant, date, credit_id) VALUES
(433.33, '2024-04-19', 2),
(433.33, '2024-05-19', 2);

-- Insertion de tickets de support
INSERT INTO ticketSupport (sujet, description, dateOuverture, statut, client_id, admin_id) VALUES
('Problème de connexion', 'Je n\'arrive pas à me connecter à mon compte en ligne', '2023-06-20', 'Ouvert', 1, 1),
('Demande d\'information', 'Je souhaite des informations sur les offres de crédit immobilier', '2024-03-19', 'En Traitement', 2, 2);

-- Insertion de frais bancaires
INSERT INTO fraisBancaire (type, montant, dateApplication, compte_id) VALUES
('Frais mensuels de tenue de compte', 2.50, '2024-03-31', 1),
('Commission d\'intervention pour dépassement', 8.00, '2024-03-19', 3);

-- Affichage des données insérées pour vérification
SELECT 'Administrateurs:' AS '';
SELECT * FROM admin;

SELECT 'Clients:' AS '';
SELECT * FROM client;

SELECT 'Comptes:' AS '';
SELECT * FROM compte;

SELECT 'Cartes Bancaires:' AS '';
SELECT * FROM carteBancaire;

SELECT 'Transactions:' AS '';
SELECT * FROM transaction; 