<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use App\Models\Client;
use App\Models\Compte;
use App\Models\Transaction;
use App\Models\CarteBancaire;
use App\Models\Credit;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        // Création d'un client de test
        $client = Client::create([
            'nom' => 'Dupont',
            'prenom' => 'Jean',
            'email' => 'jean.dupont@example.com',
            'telephone' => '0123456789',
            'adresse' => '123 rue de la Paix, Paris',
            'dateInscription' => now(),
            'statut' => 'ACTIF'
        ]);

        // Création d'un compte pour le client
        $compte = Compte::create([
            'client_id' => $client->id,
            'numero' => 'FR' . rand(100000000000, 999999999999),
            'solde' => 5000.00,
            'type' => 'COURANT',
            'statut' => 'ACTIF',
            'dateOuverture' => now()
        ]);

        // Création d'une carte bancaire
        CarteBancaire::create([
            'compte_id' => $compte->id,
            'numero' => '4532' . rand(100000000000, 999999999999),
            'titulaire' => $client->nom . ' ' . $client->prenom,
            'dateExpiration' => now()->addYears(4),
            'cvv' => rand(100, 999),
            'type' => 'VISA',
            'statut' => 'ACTIVE',
            'plafond' => 2000.00
        ]);

        // Création d'une transaction
        Transaction::create([
            'compte_id' => $compte->id,
            'montant' => 1000.00,
            'type' => 'DEPOT',
            'description' => 'Dépôt initial',
            'statut' => 'COMPLETE'
        ]);

        // Création d'un crédit
        Credit::create([
            'client_id' => $client->id,
            'montant' => 10000.00,
            'tauxInteret' => 3.5,
            'dureeMois' => 24,
            'mensualite' => 450.00,
            'statut' => 'EN_ATTENTE'
        ]);
    }
}
