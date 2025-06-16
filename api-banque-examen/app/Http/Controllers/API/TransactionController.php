<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\Transaction;
use App\Models\Compte;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\DB;

class TransactionController extends Controller
{
    public function index()
    {
        $transactions = Transaction::with(['compteSource', 'compteDest'])->get();
        return response()->json($transactions);
    }

    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'type' => 'required|string|in:DEPOT,RETRAIT,VIREMENT',
            'montant' => 'required|numeric|min:0',
            'date' => 'required|date',
            'statut' => 'required|string|in:VALIDE,ANNULE',
            'motif' => 'required|string',
            'compte_source_id' => 'required|exists:compte,id',
            'compte_dest_id' => 'required_if:type,VIREMENT|exists:compte,id'
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        try {
            DB::beginTransaction();

            $compteSource = Compte::find($request->compte_source_id);
            
            if ($request->type === 'RETRAIT' || $request->type === 'VIREMENT') {
                if ($compteSource->solde < $request->montant) {
                    return response()->json(['message' => 'Solde insuffisant'], 400);
                }
                $compteSource->solde -= $request->montant;
                $compteSource->save();
            }

            if ($request->type === 'DEPOT') {
                $compteSource->solde += $request->montant;
                $compteSource->save();
            }

            if ($request->type === 'VIREMENT' && $request->compte_dest_id) {
                $compteDest = Compte::find($request->compte_dest_id);
                $compteDest->solde += $request->montant;
                $compteDest->save();
            }

            $transaction = Transaction::create($request->all());

            DB::commit();
            return response()->json($transaction, 201);

        } catch (\Exception $e) {
            DB::rollBack();
            return response()->json(['message' => 'Erreur lors de la transaction: ' . $e->getMessage()], 500);
        }
    }

    public function show($id)
    {
        $transaction = Transaction::with(['compteSource', 'compteDest'])->find($id);
        if (!$transaction) {
            return response()->json(['message' => 'Transaction non trouvée'], 404);
        }
        return response()->json($transaction);
    }

    public function update(Request $request, $id)
    {
        $transaction = Transaction::find($id);
        if (!$transaction) {
            return response()->json(['message' => 'Transaction non trouvée'], 404);
        }

        $validator = Validator::make($request->all(), [
            'type' => 'string|in:DEPOT,RETRAIT,VIREMENT',
            'montant' => 'numeric|min:0',
            'date' => 'date',
            'statut' => 'string|in:VALIDE,ANNULE',
            'motif' => 'string',
            'compte_source_id' => 'exists:compte,id',
            'compte_dest_id' => 'exists:compte,id'
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        $transaction->update($request->all());
        return response()->json($transaction);
    }

    public function destroy($id)
    {
        $transaction = Transaction::find($id);
        if (!$transaction) {
            return response()->json(['message' => 'Transaction non trouvée'], 404);
        }

        $transaction->delete();
        return response()->json(['message' => 'Transaction supprimée avec succès']);
    }
} 