-- Script pour configurer les utilisateurs administrateurs

-- Création de la base de données (si elle n'existe pas)
CREATE DATABASE IF NOT EXISTS banque;
USE banque;

-- Création d'administrateurs avec des mots de passe plus sécurisés
-- Note: Dans un environnement de production, les mots de passe devraient être hashés
-- avec BCrypt ou Argon2, mais pour la simplicité du test nous utilisons MD5
-- Ne jamais utiliser MD5 en production!

-- Supprimer les administrateurs existants (optionnel)
-- DELETE FROM admin;

-- Ajouter des administrateurs avec différents rôles
INSERT INTO admin (username, password, role) VALUES
('admin', 'admin123', 'SuperAdmin'),           -- Administrateur principal
('support', 'support123', 'Support'),          -- Agent de support
('manager', 'manager123', 'BranchManager'),    -- Gestionnaire d'agence
('security', 'security123', 'SecurityOfficer') -- Responsable sécurité

-- Vérifier que les administrateurs ont été créés
SELECT * FROM admin;

-- Instructions pour se connecter:
-- Utiliser l'interface de connexion avec:
-- Type d'utilisateur: Administrateur
-- Nom d'utilisateur: admin
-- Mot de passe: admin123
--
-- Ou avec les autres comptes créés ci-dessus 