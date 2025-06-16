<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\CarteBancaire;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Hash;

class CarteBancaireController extends Controller
{
    public function index()
    {
        $cartes = CarteBancaire::with('compte')->get();
        return response()->json($cartes);
    }

    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'numero' => 'required|string|size:16|unique:carteBancaire',
            'cvv' => 'required|string|size:3',
            'dateExpiration' => 'required|string|size:5',
            'codePin' => 'required|string|size:4',
            'solde' => 'required|numeric|min:0',
            'statut' => 'required|string|in:ACTIVE,BLOQUEE',
            'compte_id' => 'required|exists:compte,id'
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        $data = $request->all();
        $data['codePin'] = Hash::make($request->codePin);
        
        $carte = CarteBancaire::create($data);
        return response()->json($carte, 201);
    }

    public function show($id)
    {
        $carte = CarteBancaire::with('compte')->find($id);
        if (!$carte) {
            return response()->json(['message' => 'Carte bancaire non trouvée'], 404);
        }
        return response()->json($carte);
    }

    public function update(Request $request, $id)
    {
        $carte = CarteBancaire::find($id);
        if (!$carte) {
            return response()->json(['message' => 'Carte bancaire non trouvée'], 404);
        }

        $validator = Validator::make($request->all(), [
            'numero' => 'string|size:16|unique:carteBancaire,numero,' . $id,
            'cvv' => 'string|size:3',
            'dateExpiration' => 'string|size:5',
            'codePin' => 'string|size:4',
            'solde' => 'numeric|min:0',
            'statut' => 'string|in:ACTIVE,BLOQUEE',
            'compte_id' => 'exists:compte,id'
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        $data = $request->all();
        if (isset($data['codePin'])) {
            $data['codePin'] = Hash::make($data['codePin']);
        }

        $carte->update($data);
        return response()->json($carte);
    }

    public function destroy($id)
    {
        $carte = CarteBancaire::find($id);
        if (!$carte) {
            return response()->json(['message' => 'Carte bancaire non trouvée'], 404);
        }

        $carte->delete();
        return response()->json(['message' => 'Carte bancaire supprimée avec succès']);
    }

    public function bloquer($id)
    {
        $carte = CarteBancaire::find($id);
        if (!$carte) {
            return response()->json(['message' => 'Carte bancaire non trouvée'], 404);
        }

        $carte->statut = 'BLOQUEE';
        $carte->save();
        return response()->json(['message' => 'Carte bancaire bloquée avec succès']);
    }

    public function debloquer($id)
    {
        $carte = CarteBancaire::find($id);
        if (!$carte) {
            return response()->json(['message' => 'Carte bancaire non trouvée'], 404);
        }

        $carte->statut = 'ACTIVE';
        $carte->save();
        return response()->json(['message' => 'Carte bancaire débloquée avec succès']);
    }
} 