<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\API\ClientController;
use App\Http\Controllers\API\CompteController;
use App\Http\Controllers\API\TransactionController;
use App\Http\Controllers\API\CarteBancaireController;
use App\Http\Controllers\API\CreditController;
use App\Http\Controllers\API\RemboursementController;
use App\Http\Controllers\API\TicketSupportController;
use App\Http\Controllers\API\FraisBancaireController;
use App\Http\Controllers\API\TestController;

// Route de test
Route::get('/test', [TestController::class, 'test']);

// Routes pour les clients
Route::apiResource('clients', ClientController::class);

// Routes pour les comptes
Route::apiResource('comptes', CompteController::class);
Route::get('comptes/{id}/solde', [CompteController::class, 'getSolde']);

// Routes pour les transactions
Route::apiResource('transactions', TransactionController::class);

// Routes pour les cartes bancaires
Route::apiResource('cartes', CarteBancaireController::class);
Route::post('cartes/{id}/bloquer', [CarteBancaireController::class, 'bloquer']);
Route::post('cartes/{id}/debloquer', [CarteBancaireController::class, 'debloquer']);

// Routes pour les crédits
Route::apiResource('credits', CreditController::class);
Route::post('credits/{id}/approuver', [CreditController::class, 'approuver']);
Route::post('credits/{id}/refuser', [CreditController::class, 'refuser']);

// Routes pour les remboursements
Route::apiResource('remboursements', RemboursementController::class);
Route::get('credits/{creditId}/remboursements', [RemboursementController::class, 'getByCredit']);

// Routes pour les tickets de support
Route::apiResource('tickets', TicketSupportController::class);
Route::post('tickets/{id}/assigner-admin', [TicketSupportController::class, 'assignerAdmin']);
Route::post('tickets/{id}/changer-statut', [TicketSupportController::class, 'changerStatut']);

// Routes pour les frais bancaires
Route::apiResource('frais', FraisBancaireController::class);
Route::get('comptes/{compteId}/frais', [FraisBancaireController::class, 'getByCompte']);

// ... existing code ... 