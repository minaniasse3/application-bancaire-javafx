<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\Client;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class ClientController extends Controller
{
    public function index()
    {
        $clients = Client::with(['comptes', 'credits'])->get();
        return response()->json($clients);
    }

    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'nom' => 'required|string|max:255',
            'prenom' => 'required|string|max:255',
            'email' => 'required|email|unique:clients',
            'telephone' => 'required|string|max:20',
            'adresse' => 'required|string',
            'dateInscription' => 'required|date',
            'statut' => 'required|string|in:ACTIF,INACTIF'
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        $client = Client::create($request->all());
        return response()->json($client, 201);
    }

    public function show($id)
    {
        $client = Client::with(['comptes', 'credits', 'tickets'])->find($id);
        if (!$client) {
            return response()->json(['message' => 'Client non trouvé'], 404);
        }
        return response()->json($client);
    }

    public function update(Request $request, $id)
    {
        $client = Client::find($id);
        if (!$client) {
            return response()->json(['message' => 'Client non trouvé'], 404);
        }

        $validator = Validator::make($request->all(), [
            'nom' => 'string|max:255',
            'prenom' => 'string|max:255',
            'email' => 'email|unique:clients,email,' . $id,
            'telephone' => 'string|max:20',
            'adresse' => 'string',
            'dateInscription' => 'date',
            'statut' => 'string|in:ACTIF,INACTIF'
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        $client->update($request->all());
        return response()->json($client);
    }

    public function destroy($id)
    {
        $client = Client::find($id);
        if (!$client) {
            return response()->json(['message' => 'Client non trouvé'], 404);
        }

        $client->delete();
        return response()->json(['message' => 'Client supprimé avec succès']);
    }
} 