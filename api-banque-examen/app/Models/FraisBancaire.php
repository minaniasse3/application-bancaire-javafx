<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class FraisBancaire extends Model
{
    protected $table = 'frais_bancaires';
    
    protected $fillable = [
        'compte_id',
        'type',
        'montant',
        'description',
        'dateApplication',
        'statut'
    ];

    protected $casts = [
        'montant' => 'decimal:2',
        'dateApplication' => 'date'
    ];

    public function compte()
    {
        return $this->belongsTo(Compte::class);
    }
} 