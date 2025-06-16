<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Credit extends Model
{
    protected $table = 'credits';
    
    protected $fillable = [
        'client_id',
        'montant',
        'tauxInteret',
        'dureeMois',
        'mensualite',
        'statut',
        'dateDebut',
        'dateFin'
    ];

    protected $casts = [
        'montant' => 'decimal:2',
        'tauxInteret' => 'decimal:2',
        'mensualite' => 'decimal:2',
        'dateDebut' => 'date',
        'dateFin' => 'date'
    ];

    public function client()
    {
        return $this->belongsTo(Client::class);
    }

    public function remboursements()
    {
        return $this->hasMany(Remboursement::class);
    }
} 