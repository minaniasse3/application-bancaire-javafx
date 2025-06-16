<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up()
    {
        Schema::create('credits', function (Blueprint $table) {
            $table->id();
            $table->foreignId('client_id')->constrained()->onDelete('cascade');
            $table->decimal('montant', 15, 2);
            $table->decimal('tauxInteret', 5, 2);
            $table->integer('dureeMois');
            $table->decimal('mensualite', 15, 2);
            $table->enum('statut', ['EN_ATTENTE', 'APPROUVE', 'REFUSE', 'EN_COURS', 'TERMINE'])->default('EN_ATTENTE');
            $table->date('dateDebut')->nullable();
            $table->date('dateFin')->nullable();
            $table->timestamps();
        });
    }

    public function down()
    {
        Schema::dropIfExists('credits');
    }
}; 