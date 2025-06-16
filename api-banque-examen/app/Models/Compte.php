<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Compte extends Model
{
    protected $table = 'comptes';
    
    protected $fillable = [
        'client_id',
        'numero',
        'solde',
        'type',
        'statut',
        'dateOuverture'
    ];

    protected $casts = [
        'solde' => 'decimal:2',
        'dateOuverture' => 'date'
    ];

    public function client()
    {
        return $this->belongsTo(Client::class);
    }

    public function transactions()
    {
        return $this->hasMany(Transaction::class);
    }

    public function carteBancaire()
    {
        return $this->hasOne(CarteBancaire::class);
    }

    public function fraisBancaires()
    {
        return $this->hasMany(FraisBancaire::class);
    }
} 