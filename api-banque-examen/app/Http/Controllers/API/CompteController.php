<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\Compte;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class CompteController extends Controller
{
    public function index()
    {
        $comptes = Compte::with(['client', 'carteBancaire', 'transactionsSource', 'transactionsDest'])->get();
        return response()->json($comptes);
    }

    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'numero' => 'required|string|unique:compte',
            'type' => 'required|string|in:COURANT,EPARGNE',
            'solde' => 'required|numeric|min:0',
            'dateCreation' => 'required|date',
            'client_id' => 'required|exists:client,id'
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        $compte = Compte::create($request->all());
        return response()->json($compte, 201);
    }

    public function show($id)
    {
        $compte = Compte::with(['client', 'carteBancaire', 'transactionsSource', 'transactionsDest', 'fraisBancaires'])
            ->find($id);
        
        if (!$compte) {
            return response()->json(['message' => 'Compte non trouvé'], 404);
        }
        
        return response()->json($compte);
    }

    public function update(Request $request, $id)
    {
        $compte = Compte::find($id);
        if (!$compte) {
            return response()->json(['message' => 'Compte non trouvé'], 404);
        }

        $validator = Validator::make($request->all(), [
            'numero' => 'string|unique:compte,numero,' . $id,
            'type' => 'string|in:COURANT,EPARGNE',
            'solde' => 'numeric|min:0',
            'dateCreation' => 'date',
            'client_id' => 'exists:client,id'
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        $compte->update($request->all());
        return response()->json($compte);
    }

    public function destroy($id)
    {
        $compte = Compte::find($id);
        if (!$compte) {
            return response()->json(['message' => 'Compte non trouvé'], 404);
        }

        $compte->delete();
        return response()->json(['message' => 'Compte supprimé avec succès']);
    }

    public function getSolde($id)
    {
        $compte = Compte::find($id);
        if (!$compte) {
            return response()->json(['message' => 'Compte non trouvé'], 404);
        }

        return response()->json(['solde' => $compte->solde]);
    }
} 