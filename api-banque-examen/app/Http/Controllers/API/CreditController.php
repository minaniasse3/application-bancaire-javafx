<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\Credit;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class CreditController extends Controller
{
    public function index()
    {
        $credits = Credit::with(['client', 'remboursements'])->get();
        return response()->json($credits);
    }

    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'montant' => 'required|numeric|min:0',
            'tauxInteret' => 'required|numeric|min:0',
            'duree_mois' => 'required|integer|min:1',
            'mensualite' => 'required|numeric|min:0',
            'dateDemande' => 'required|date',
            'statut' => 'required|string|in:EN_COURS,APPROUVE,REFUSE',
            'client_id' => 'required|exists:client,id'
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        $credit = Credit::create($request->all());
        return response()->json($credit, 201);
    }

    public function show($id)
    {
        $credit = Credit::with(['client', 'remboursements'])->find($id);
        if (!$credit) {
            return response()->json(['message' => 'Crédit non trouvé'], 404);
        }
        return response()->json($credit);
    }

    public function update(Request $request, $id)
    {
        $credit = Credit::find($id);
        if (!$credit) {
            return response()->json(['message' => 'Crédit non trouvé'], 404);
        }

        $validator = Validator::make($request->all(), [
            'montant' => 'numeric|min:0',
            'tauxInteret' => 'numeric|min:0',
            'duree_mois' => 'integer|min:1',
            'mensualite' => 'numeric|min:0',
            'dateDemande' => 'date',
            'statut' => 'string|in:EN_COURS,APPROUVE,REFUSE',
            'client_id' => 'exists:client,id'
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        $credit->update($request->all());
        return response()->json($credit);
    }

    public function destroy($id)
    {
        $credit = Credit::find($id);
        if (!$credit) {
            return response()->json(['message' => 'Crédit non trouvé'], 404);
        }

        $credit->delete();
        return response()->json(['message' => 'Crédit supprimé avec succès']);
    }

    public function approuver($id)
    {
        $credit = Credit::find($id);
        if (!$credit) {
            return response()->json(['message' => 'Crédit non trouvé'], 404);
        }

        $credit->statut = 'APPROUVE';
        $credit->save();
        return response()->json(['message' => 'Crédit approuvé avec succès']);
    }

    public function refuser($id)
    {
        $credit = Credit::find($id);
        if (!$credit) {
            return response()->json(['message' => 'Crédit non trouvé'], 404);
        }

        $credit->statut = 'REFUSE';
        $credit->save();
        return response()->json(['message' => 'Crédit refusé avec succès']);
    }
} 