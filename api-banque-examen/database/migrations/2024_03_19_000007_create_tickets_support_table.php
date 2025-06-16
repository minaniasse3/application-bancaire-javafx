<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up()
    {
        Schema::create('tickets_support', function (Blueprint $table) {
            $table->id();
            $table->foreignId('client_id')->constrained()->onDelete('cascade');
            $table->foreignId('admin_id')->nullable()->constrained('users')->onDelete('set null');
            $table->string('sujet');
            $table->text('description');
            $table->enum('statut', ['OUVERT', 'EN_COURS', 'RESOLU', 'FERME'])->default('OUVERT');
            $table->timestamp('dateOuverture');
            $table->timestamp('dateResolution')->nullable();
            $table->timestamps();
        });
    }

    public function down()
    {
        Schema::dropIfExists('tickets_support');
    }
}; 