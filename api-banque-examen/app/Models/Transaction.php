<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Transaction extends Model
{
    protected $table = 'transactions';
    
    protected $fillable = [
        'compte_id',
        'montant',
        'type',
        'description',
        'statut'
    ];

    protected $casts = [
        'montant' => 'decimal:2'
    ];

    public function compte()
    {
        return $this->belongsTo(Compte::class);
    }
} 