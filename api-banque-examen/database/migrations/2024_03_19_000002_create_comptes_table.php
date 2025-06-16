<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up()
    {
        Schema::create('comptes', function (Blueprint $table) {
            $table->id();
            $table->foreignId('client_id')->constrained()->onDelete('cascade');
            $table->string('numero')->unique();
            $table->decimal('solde', 15, 2)->default(0);
            $table->enum('type', ['COURANT', 'EPARGNE'])->default('COURANT');
            $table->enum('statut', ['ACTIF', 'BLOQUE', 'FERME'])->default('ACTIF');
            $table->date('dateOuverture');
            $table->timestamps();
        });
    }

    public function down()
    {
        Schema::dropIfExists('comptes');
    }
}; 