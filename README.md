# Application Bancaire JavaFX

Ce projet est une application de gestion bancaire construite avec JavaFX, JPA/Hibernate et MySQL. Ce système
permet la gestion des comptes clients, des transactions, des crédits, et plus encore.

## Prérequis

- Java 17 ou supérieur
- Maven 3.6.3 ou supérieur
- Docker et Docker Compose
- MySQL 8.0

## Configuration avec Docker

L'application est configurée pour utiliser une base de données MySQL s'exécutant dans un conteneur Docker.

### Configuration de la base de données MySQL avec Docker

1. Assurez-vous que Docker est installé et en cours d'exécution sur votre système :

```shell
docker --version
```

2. Le fichier `docker-compose.yml` est déjà configuré. Il définit un service MySQL avec les paramètres suivants :
   - Image : mysql:8.0
   - Port : 3306
   - Base de données : banque
   - Utilisateur : banque
   - Mot de passe : banque

3. Démarrez le conteneur MySQL avec Docker Compose :

```shell
docker-compose up -d
```

4. Initialisez la base de données avec le script SQL fourni :

```shell
docker exec -i mysql-banque mysql -uroot -proot < init_db.sql
```

### Structure de la base de données

La base de données contient les tables suivantes :
- client : Informations sur les clients
- admin : Informations sur les administrateurs
- compte : Comptes bancaires des clients
- transaction : Transactions effectuées sur les comptes
- credit : Crédits accordés aux clients
- remboursement : Remboursements des crédits
- carte_bancaire : Cartes bancaires associées aux comptes
- ticket_support : Tickets de support pour l'assistance clientèle
- frais_bancaire : Frais bancaires appliqués aux comptes

## Exécution de l'application

Pour exécuter l'application, utilisez la commande Maven suivante :

```shell
mvn javafx:run
```

## Connexion à l'application

L'application supporte deux types d'utilisateurs :

### Administrateur
- Nom d'utilisateur : admin
- Mot de passe : admin123

### Client
- Nom d'utilisateur : john.doe@example.com
- Mot de passe : (tous les mots de passe sont acceptés en mode simulation)

## Mode de fonctionnement

L'application peut fonctionner dans deux modes :
1. **Mode Base de données** : L'application se connecte à la base de données MySQL pour toutes les opérations CRUD.
2. **Mode Simulation** : Si la base de données n'est pas accessible, l'application bascule automatiquement en mode de simulation, où les opérations CRUD sont simulées.

## Fonctionnalités

- **Gestion des clients** : Inscription, modification et consultation des informations clients.
- **Gestion des comptes** : Création, consultation et gestion des comptes bancaires.
- **Transactions** : Dépôts, retraits et virements entre comptes.
- **Crédits** : Demande, approbation et gestion des crédits.
- **Support client** : Système de tickets pour l'assistance clientèle.
- **Tableaux de bord** : Tableaux de bord distincts pour les clients et les administrateurs.

## Structure du projet

- `src/main/java/com/groupeisi/banque` : Code source Java
- `src/main/resources` : Ressources (FXML, images, etc.)
- `src/main/resources/META-INF` : Configuration de persistance JPA
- `docker-compose.yml` : Configuration Docker pour MySQL
- `init_db.sql` : Script d'initialisation de la base de données

## Résolution des problèmes courants

### Erreur de connexion à la base de données
Si l'application ne peut pas se connecter à la base de données, elle passera automatiquement en mode simulation. Vous pouvez vérifier l'état du conteneur Docker avec :

```shell
docker ps
```

Si le conteneur n'est pas en cours d'exécution, redémarrez-le :

```shell
docker-compose up -d
```

### Erreur de version Java
Assurez-vous d'utiliser Java 17 pour la compilation et l'exécution de l'application.

## 📋 Table des matières

- [Prérequis](#prérequis)
- [Installation](#installation)
- [Configuration de la base de données](#configuration-de-la-base-de-données)
- [Exécution de l'application](#exécution-de-lapplication)
- [Utilisateurs de test](#utilisateurs-de-test)
- [Fonctionnalités](#fonctionnalités)
- [Structure du projet](#structure-du-projet)
- [Dépannage](#dépannage)

## 🔧 Prérequis

- Java 17 ou supérieur
- Maven
- MySQL (5.7 ou supérieur)
- Un serveur SMTP pour les fonctionnalités d'envoi d'email (optionnel pour les tests initiaux)

## 💻 Installation

1. Clonez le dépôt Git :
   ```bash
   git clone https://github.com/votre-nom/systeme-bancaire.git
   cd systeme-bancaire
   ```

2. Compilez le projet avec Maven :
   ```bash
   mvn clean compile
   ```

## 🗄️ Configuration de la base de données

1. Assurez-vous que MySQL est installé et en cours d'exécution.

2. Créez une base de données MySQL nommée `banque` :
   ```sql
   CREATE DATABASE banque;
   ```

3. Configurez les informations de connexion à la base de données dans le fichier `src/main/resources/META-INF/persistence.xml` :
   ```xml
   <property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/banque?serverTimezone=UTC"/>
   <property name="javax.persistence.jdbc.user" value="root"/>
   <property name="javax.persistence.jdbc.password" value="root"/>
   ```
   Assurez-vous d'ajuster le nom d'utilisateur et le mot de passe selon votre configuration MySQL.

4. Initialisez la base de données avec des données de test (recommandé pour les tests) :
   ```bash
   mysql -u root -p banque < init_db.sql
   ```

## ▶️ Exécution de l'application

Exécutez l'application avec Maven :
```bash
mvn javafx:run
```

L'application démarrera et affichera l'écran de connexion.

## 👥 Utilisateurs de test

Après avoir initialisé la base de données avec le script `init_db.sql`, les utilisateurs suivants sont disponibles pour les tests :

### Connexion administrateur
Sélectionnez "Administrateur" dans le type d'utilisateur et utilisez :

| Nom d'utilisateur | Mot de passe | Rôle       |
|-------------------|--------------|------------|
| admin             | admin123     | SuperAdmin |
| support           | support123   | Support    |

**Note importante** : D'après le code actuel du contrôleur LoginController, vous pouvez également utiliser le nom d'utilisateur "admin" avec le mot de passe "admin" pour vous connecter en tant qu'administrateur pendant les tests.

### Connexion client
Sélectionnez "Client" dans le type d'utilisateur. Les clients suivants sont préchargés dans la base de données :

| Email                   | Informations                                        |
|-------------------------|-----------------------------------------------------|
| jean.dupont@example.com | Compte courant: FR76-2024-1234-5678 (1500€)<br>Compte épargne: FR76-2024-8765-4321 (5000€) |
| sophie.martin@example.com | Compte courant: FR76-2024-2345-6789 (2500€)      |
| pierre.dubois@example.com | Compte courant: FR76-2024-3456-7890 (3000€)      |

**Important** : Avec l'implémentation actuelle, vous devez vous inscrire via l'interface d'inscription pour créer un compte client et un mot de passe. Vous pouvez utiliser les emails des clients préchargés pour voir leurs données, ou créer de nouveaux clients.

## 🔐 Création d'utilisateurs

### Création de compte client

1. Sur l'écran de connexion, cliquez sur "S'inscrire"
2. Remplissez le formulaire d'inscription avec vos informations personnelles :
   - Nom, prénom
   - Email (doit être unique)
   - Téléphone, adresse
   - Choisissez le type de compte (Courant ou Épargne)
   - Définissez un nom d'utilisateur et un mot de passe
3. Cliquez sur "S'inscrire" pour créer votre compte
4. Un numéro de compte bancaire sera automatiquement généré

### Création de compte administrateur

Les comptes administrateurs doivent être créés directement dans la base de données. Utilisez le script SQL suivant pour créer un nouvel administrateur :

```sql
INSERT INTO admin (username, password, role) VALUES ('nouvel_admin', 'mot_de_passe', 'Support');
```

## 🚀 Fonctionnalités

### Pour les clients

- Gestion des comptes bancaires (consulter le solde, l'historique des transactions)
- Effectuer des transactions (dépôts, retraits, virements)
- Demander une carte bancaire
- Soumettre des demandes de crédit
- Créer des tickets de support pour assistance

### Pour les administrateurs

- Gestion des clients (visualisation des informations, modification des statuts)
- Validation des demandes de cartes bancaires et de crédits
- Traitement des tickets de support
- Surveillance des transactions

## 📊 Structure du projet

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── groupeisi/
│   │           └── banque/
│   │               ├── App.java                   # Point d'entrée de l'application
│   │               ├── controllers/               # Contrôleurs JavaFX
│   │               ├── entities/                  # Entités JPA
│   │               └── services/                  # Services métier
│   └── resources/
│       ├── META-INF/
│       │   └── persistence.xml                    # Configuration JPA
│       └── com/
│           └── groupeisi/
│               └── banque/
│                   ├── login-view.fxml            # Vue de connexion
│                   ├── register-view.fxml         # Vue d'inscription
│                   ├── client-dashboard.fxml      # Vue tableau de bord client
│                   └── admin-dashboard.fxml       # Vue tableau de bord admin
├── test/                                          # Tests unitaires
init_db.sql                                        # Script d'initialisation de la base de données
pom.xml                                            # Configuration Maven
```

## 📧 Configuration du service d'email

Pour que l'envoi d'emails fonctionne, modifiez les constantes dans la classe `EmailService.java` :

```java
private static final String FROM_EMAIL = "votre-email@gmail.com";
private static final String EMAIL_PASSWORD = "votre-mot-de-passe-application";
```

**Important** : Pour Gmail, vous devrez utiliser un "mot de passe d'application" généré dans les paramètres de sécurité de votre compte Google.

## ❓ Dépannage

### Connexion à l'application

- **Utilisateur administrateur** : Pour l'instant, vous pouvez utiliser "admin" comme nom d'utilisateur et "admin" comme mot de passe (codé en dur dans `LoginController.java`).
- **Utilisateur client** : Inscrivez-vous via l'interface d'inscription pour créer un nouveau compte.

### Problèmes avec la base de données

- Vérifiez que MySQL est en cours d'exécution avec `systemctl status mysql` ou `service mysql status`
- Assurez-vous que les informations de connexion dans `persistence.xml` sont correctes
- Si vous rencontrez des erreurs "Table does not exist", la propriété `hibernate.hbm2ddl.auto` dans `persistence.xml` doit être définie sur "create" pour la première exécution.

### Problèmes de JavaFX

- Si l'interface ne s'affiche pas correctement, vérifiez que toutes les ressources FXML sont présentes et correctement formatées
- Utilisez `mvn javafx:run -X` pour un débogage plus détaillé

## Contribution
1. Fork le projet
2. Créer une branche (`git checkout -b feature/AmazingFeature`)
3. Commit les changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

## License
Distribué sous la licence MIT. Voir `LICENSE` pour plus d'informations.

## Contact
Votre Nom - [@votre_twitter](https://twitter.com/votre_twitter)
Lien du projet : [https://github.com/votre-username/banque](https://github.com/votre-username/banque)

#### Résolution des erreurs dans les tests EmailServiceTest

Si vous rencontrez des erreurs dans les tests EmailServiceTest après avoir corrigé la classe EmailService :

1. Remplacez les appels à `session.createMimeMessage()` par une approche différente :

   ```java
   // Avant
   when(session.createMimeMessage()).thenReturn(new MimeMessage(session));
   
   // Après
   @Mock
   private MimeMessage mimeMessage;
   
   // Dans la méthode de test
   doReturn(mimeMessage).when(session).getTransport("smtp");
   ```

2. Utilisez les méthodes statiques de EmailService au lieu des méthodes d'instance :

   ```java
   // Avant
   emailService.setSession(session);
   assertDoesNotThrow(() -> emailService.sendEmail(to, subject, content));
   
   // Après
   EmailService.setSession(session);
   assertDoesNotThrow(() -> EmailService.sendEmail(to, subject, content));
   ```

3. Utilisez `atLeastOnce()` pour les vérifications pour plus de flexibilité :

   ```java
   // Avant
   verify(transport).connect(anyString(), anyString(), anyString());
   
   // Après
   verify(transport, atLeastOnce()).connect(anyString(), anyString(), anyString());
   ```

4. Assurez-vous d'importer correctement toutes les classes nécessaires :

   ```java
   import static org.mockito.Mockito.*;
   import static org.mockito.ArgumentMatchers.*;
   ```

### Problèmes de compilation et d'exécution

# API Bancaire - Documentation

## Configuration requise
- PHP >= 8.2.0
- Composer
- PostgreSQL
- Laragon (environnement de développement)

## Installation

1. Cloner le repository
```bash
git clone [URL_DU_REPO]
cd api-banque-examen
```

2. Installer les dépendances
```bash
composer install
```

3. Configurer l'environnement
```bash
cp .env.example .env
php artisan key:generate
```

4. Configurer la base de données dans le fichier `.env`
```
DB_CONNECTION=pgsql
DB_HOST=127.0.0.1
DB_PORT=5432
DB_DATABASE=banque_examen
DB_USERNAME=postgres
DB_PASSWORD=votre_mot_de_passe
```

5. Lancer les migrations
```bash
php artisan migrate
```

## Endpoints API

### Clients
- `GET /api/clients` - Liste tous les clients
- `POST /api/clients` - Crée un nouveau client
- `GET /api/clients/{id}` - Récupère un client spécifique
- `PUT /api/clients/{id}` - Met à jour un client
- `DELETE /api/clients/{id}` - Supprime un client

### Comptes
- `GET /api/comptes` - Liste tous les comptes
- `POST /api/comptes` - Crée un nouveau compte
- `GET /api/comptes/{id}` - Récupère un compte spécifique
- `PUT /api/comptes/{id}` - Met à jour un compte
- `DELETE /api/comptes/{id}` - Supprime un compte
- `GET /api/comptes/{id}/solde` - Vérifie le solde d'un compte

### Transactions
- `GET /api/transactions` - Liste toutes les transactions
- `POST /api/transactions` - Crée une nouvelle transaction
- `GET /api/transactions/{id}` - Récupère une transaction spécifique

### Cartes Bancaires
- `GET /api/cartes` - Liste toutes les cartes
- `POST /api/cartes` - Crée une nouvelle carte
- `GET /api/cartes/{id}` - Récupère une carte spécifique
- `PUT /api/cartes/{id}` - Met à jour une carte
- `DELETE /api/cartes/{id}` - Supprime une carte
- `POST /api/cartes/{id}/bloquer` - Bloque une carte
- `POST /api/cartes/{id}/debloquer` - Débloque une carte

### Crédits
- `GET /api/credits` - Liste tous les crédits
- `POST /api/credits` - Crée une nouvelle demande de crédit
- `GET /api/credits/{id}` - Récupère un crédit spécifique
- `POST /api/credits/{id}/approuver` - Approuve un crédit
- `POST /api/credits/{id}/refuser` - Refuse un crédit

### Remboursements
- `GET /api/remboursements` - Liste tous les remboursements
- `POST /api/remboursements` - Crée un nouveau remboursement
- `GET /api/remboursements/{id}` - Récupère un remboursement spécifique
- `GET /api/credits/{creditId}/remboursements` - Liste les remboursements d'un crédit

### Tickets Support
- `GET /api/tickets` - Liste tous les tickets
- `POST /api/tickets` - Crée un nouveau ticket
- `GET /api/tickets/{id}` - Récupère un ticket spécifique
- `POST /api/tickets/{id}/assigner-admin` - Assigne un admin au ticket
- `POST /api/tickets/{id}/changer-statut` - Change le statut d'un ticket

### Frais Bancaires
- `GET /api/frais` - Liste tous les frais
- `POST /api/frais` - Crée un nouveau frais
- `GET /api/frais/{id}` - Récupère un frais spécifique
- `GET /api/comptes/{compteId}/frais` - Liste les frais d'un compte

## Communication entre Applications

### Configuration CORS
Pour permettre la communication entre votre API et votre application frontend, assurez-vous que CORS est correctement configuré dans `config/cors.php` :

```php
return [
    'paths' => ['api/*'],
    'allowed_methods' => ['*'],
    'allowed_origins' => ['http://localhost:3000'], // URL de votre frontend
    'allowed_origins_patterns' => [],
    'allowed_headers' => ['*'],
    'exposed_headers' => [],
    'max_age' => 0,
    'supports_credentials' => true,
];
```

### Exemple d'utilisation avec Axios (Frontend)
```javascript
import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8000/api',
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    },
    withCredentials: true
});

// Exemple de requête
const getClients = async () => {
    try {
        const response = await api.get('/clients');
        return response.data;
    } catch (error) {
        console.error('Erreur:', error);
    }
};
```

## Tests
Pour lancer les tests :
```bash
php artisan test
```

## Déploiement
1. Configurer les variables d'environnement de production
2. Optimiser l'application :
```bash
php artisan optimize
php artisan config:cache
php artisan route:cache
```

## Sécurité
- Toutes les routes API sont protégées par Sanctum
- Les requêtes doivent inclure un token CSRF pour les méthodes POST/PUT/DELETE
- Les données sensibles sont chiffrées
- Les mots de passe sont hashés avec bcrypt

## Support
Pour toute question ou problème, veuillez créer une issue dans le repository.