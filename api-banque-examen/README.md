
## Endpoints API

### Clients
-  http://127.0.0.1:8000/api/clients - Liste tous les clients
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
