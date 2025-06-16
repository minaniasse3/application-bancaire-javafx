<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\Remboursement;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class RemboursementController extends Controller
{
    public function index()
    {
        $remboursements = Remboursement::with('credit')->get();
        return response()->json($remboursements);
    }

    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'montant' => 'required|numeric|min:0',
            'date' => 'required|date',
            'credit_id' => 'required|exists:credit,id'
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        $remboursement = Remboursement::create($request->all());
        return response()->json($remboursement, 201);
    }

    public function show($id)
    {
        $remboursement = Remboursement::with('credit')->find($id);
        if (!$remboursement) {
            return response()->json(['message' => 'Remboursement non trouvé'], 404);
        }
        return response()->json($remboursement);
    }

    public function update(Request $request, $id)
    {
        $remboursement = Remboursement::find($id);
        if (!$remboursement) {
            return response()->json(['message' => 'Remboursement non trouvé'], 404);
        }

        $validator = Validator::make($request->all(), [
            'montant' => 'numeric|min:0',
            'date' => 'date',
            'credit_id' => 'exists:credit,id'
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        $remboursement->update($request->all());
        return response()->json($remboursement);
    }

    public function destroy($id)
    {
        $remboursement = Remboursement::find($id);
        if (!$remboursement) {
            return response()->json(['message' => 'Remboursement non trouvé'], 404);
        }

        $remboursement->delete();
        return response()->json(['message' => 'Remboursement supprimé avec succès']);
    }

    public function getByCredit($creditId)
    {
        $remboursements = Remboursement::where('credit_id', $creditId)->get();
        return response()->json($remboursements);
    }
} 