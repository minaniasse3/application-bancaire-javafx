<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\TicketSupport;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class TicketSupportController extends Controller
{
    public function index()
    {
        $tickets = TicketSupport::with(['client', 'admin'])->get();
        return response()->json($tickets);
    }

    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'sujet' => 'required|string|max:255',
            'description' => 'required|string',
            'dateOuverture' => 'required|date',
            'statut' => 'required|string|in:OUVERT,EN_TRAITEMENT,RESOLU,FERME',
            'client_id' => 'required|exists:client,id',
            'admin_id' => 'nullable|exists:admin,id'
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        $ticket = TicketSupport::create($request->all());
        return response()->json($ticket, 201);
    }

    public function show($id)
    {
        $ticket = TicketSupport::with(['client', 'admin'])->find($id);
        if (!$ticket) {
            return response()->json(['message' => 'Ticket non trouvé'], 404);
        }
        return response()->json($ticket);
    }

    public function update(Request $request, $id)
    {
        $ticket = TicketSupport::find($id);
        if (!$ticket) {
            return response()->json(['message' => 'Ticket non trouvé'], 404);
        }

        $validator = Validator::make($request->all(), [
            'sujet' => 'string|max:255',
            'description' => 'string',
            'dateOuverture' => 'date',
            'statut' => 'string|in:OUVERT,EN_TRAITEMENT,RESOLU,FERME',
            'client_id' => 'exists:client,id',
            'admin_id' => 'nullable|exists:admin,id'
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        $ticket->update($request->all());
        return response()->json($ticket);
    }

    public function destroy($id)
    {
        $ticket = TicketSupport::find($id);
        if (!$ticket) {
            return response()->json(['message' => 'Ticket non trouvé'], 404);
        }

        $ticket->delete();
        return response()->json(['message' => 'Ticket supprimé avec succès']);
    }

    public function assignerAdmin(Request $request, $id)
    {
        $ticket = TicketSupport::find($id);
        if (!$ticket) {
            return response()->json(['message' => 'Ticket non trouvé'], 404);
        }

        $validator = Validator::make($request->all(), [
            'admin_id' => 'required|exists:admin,id'
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        $ticket->admin_id = $request->admin_id;
        $ticket->statut = 'EN_TRAITEMENT';
        $ticket->save();

        return response()->json($ticket);
    }

    public function changerStatut(Request $request, $id)
    {
        $ticket = TicketSupport::find($id);
        if (!$ticket) {
            return response()->json(['message' => 'Ticket non trouvé'], 404);
        }

        $validator = Validator::make($request->all(), [
            'statut' => 'required|string|in:OUVERT,EN_TRAITEMENT,RESOLU,FERME'
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        $ticket->statut = $request->statut;
        $ticket->save();

        return response()->json($ticket);
    }
} 