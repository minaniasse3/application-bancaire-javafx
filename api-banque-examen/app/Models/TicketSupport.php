<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class TicketSupport extends Model
{
    protected $table = 'tickets_support';
    
    protected $fillable = [
        'client_id',
        'admin_id',
        'sujet',
        'description',
        'statut',
        'dateOuverture',
        'dateResolution'
    ];

    protected $casts = [
        'dateOuverture' => 'datetime',
        'dateResolution' => 'datetime'
    ];

    public function client()
    {
        return $this->belongsTo(Client::class);
    }

    public function admin()
    {
        return $this->belongsTo(User::class, 'admin_id');
    }
} 