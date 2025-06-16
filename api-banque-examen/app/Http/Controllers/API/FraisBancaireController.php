<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\FraisBancaire;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class FraisBancaireController extends Controller
{
    public function index()
    {
        $frais = FraisBancaire::with('compte')->get();
        return response()->json($frais);
    }

    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'type' => 'required|string|max:50',
            'montant' => 'required|numeric|min:0',
            'dateApplication' => 'required|date',
            'compte_id' => 'required|exists:compte,id'
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        $frais = FraisBancaire::create($request->all());
        return response()->json($frais, 201);
    }

    public function show($id)
    {
        $frais = FraisBancaire::with('compte')->find($id);
        if (!$frais) {
            return response()->json(['message' => 'Frais bancaire non trouvé'], 404);
        }
        return response()->json($frais);
    }

    public function update(Request $request, $id)
    {
        $frais = FraisBancaire::find($id);
        if (!$frais) {
            return response()->json(['message' => 'Frais bancaire non trouvé'], 404);
        }

        $validator = Validator::make($request->all(), [
            'type' => 'string|max:50',
            'montant' => 'numeric|min:0',
            'dateApplication' => 'date',
            'compte_id' => 'exists:compte,id'
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        $frais->update($request->all());
        return response()->json($frais);
    }

    public function destroy($id)
    {
        $frais = FraisBancaire::find($id);
        if (!$frais) {
            return response()->json(['message' => 'Frais bancaire non trouvé'], 404);
        }

        $frais->delete();
        return response()->json(['message' => 'Frais bancaire supprimé avec succès']);
    }

    public function getByCompte($compteId)
    {
        $frais = FraisBancaire::where('compte_id', $compteId)->get();
        return response()->json($frais);
    }
} 