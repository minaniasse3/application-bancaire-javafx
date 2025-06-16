<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Client extends Model
{
    protected $table = 'client';
    
    protected $fillable = [
        'nom',
        'prenom',
        'email',
        'telephone',
        'adresse',
        'dateInscription',
        'statut'
    ];

    public function comptes()
    {
        return $this->hasMany(Compte::class);
    }

    public function credits()
    {
        return $this->hasMany(Credit::class);
    }

    public function tickets()
    {
        return $this->hasMany(TicketSupport::class);
    }
} 