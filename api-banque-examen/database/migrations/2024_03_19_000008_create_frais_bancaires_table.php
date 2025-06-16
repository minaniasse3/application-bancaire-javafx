<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up()
    {
        Schema::create('frais_bancaires', function (Blueprint $table) {
            $table->id();
            $table->foreignId('compte_id')->constrained()->onDelete('cascade');
            $table->string('type');
            $table->decimal('montant', 15, 2);
            $table->text('description')->nullable();
            $table->date('dateApplication');
            $table->enum('statut', ['EN_ATTENTE', 'APPLIQUE', 'ANNULE'])->default('EN_ATTENTE');
            $table->timestamps();
        });
    }

    public function down()
    {
        Schema::dropIfExists('frais_bancaires');
    }
}; 