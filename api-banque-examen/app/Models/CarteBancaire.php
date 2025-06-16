<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class CarteBancaire extends Model
{
    protected $table = 'cartes_bancaires';
    
    protected $fillable = [
        'compte_id',
        'numero',
        'titulaire',
        'dateExpiration',
        'cvv',
        'type',
        'statut',
        'plafond'
    ];

    protected $hidden = [
        'cvv'
    ];

    protected $casts = [
        'dateExpiration' => 'date',
        'plafond' => 'decimal:2'
    ];

    public function compte()
    {
        return $this->belongsTo(Compte::class);
    }
} 