<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Remboursement extends Model
{
    protected $table = 'remboursements';
    
    protected $fillable = [
        'credit_id',
        'montant',
        'dateRemboursement',
        'statut'
    ];

    protected $casts = [
        'montant' => 'decimal:2',
        'dateRemboursement' => 'date'
    ];

    public function credit()
    {
        return $this->belongsTo(Credit::class);
    }
} 