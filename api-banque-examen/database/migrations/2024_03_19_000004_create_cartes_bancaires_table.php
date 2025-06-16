<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up()
    {
        Schema::create('cartes_bancaires', function (Blueprint $table) {
            $table->id();
            $table->foreignId('compte_id')->constrained()->onDelete('cascade');
            $table->string('numero')->unique();
            $table->string('titulaire');
            $table->date('dateExpiration');
            $table->string('cvv');
            $table->enum('type', ['VISA', 'MASTERCARD']);
            $table->enum('statut', ['ACTIVE', 'BLOQUEE', 'EXPIREE'])->default('ACTIVE');
            $table->decimal('plafond', 15, 2)->default(1000);
            $table->timestamps();
        });
    }

    public function down()
    {
        Schema::dropIfExists('cartes_bancaires');
    }
}; 