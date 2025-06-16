<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up()
    {
        Schema::create('remboursements', function (Blueprint $table) {
            $table->id();
            $table->foreignId('credit_id')->constrained()->onDelete('cascade');
            $table->decimal('montant', 15, 2);
            $table->date('dateRemboursement');
            $table->enum('statut', ['EN_ATTENTE', 'COMPLETE', 'ANNULEE'])->default('EN_ATTENTE');
            $table->timestamps();
        });
    }

    public function down()
    {
        Schema::dropIfExists('remboursements');
    }
}; 