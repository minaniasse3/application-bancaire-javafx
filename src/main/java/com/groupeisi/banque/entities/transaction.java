package com.groupeisi.banque.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type; // depot/retrait/virement
    private double montant;
    private String date;
    private String statut; // valide/rejete
    private String motif; // Motif de la transaction (optionnel)

    // Compte source de la transaction
    @ManyToOne
    @JoinColumn(name = "compte_source_id")
    private compte compteSource;

    // Compte destinataire (optionnel, pour virement)
    @ManyToOne
    @JoinColumn(name = "compte_dest_id", nullable = true)
    private compte compteDest;
    
    // Méthode pour définir le compte source (alias pour compteSource)
    public void setCompte(compte compte) {
        this.compteSource = compte;
    }
    
    // Méthode pour obtenir le compte source (alias pour compteSource)
    public compte getCompte() {
        return this.compteSource;
    }
    
    @Override
    public String toString() {
        String sourceInfo = (compteSource != null) ? compteSource.getNumero() : "N/A";
        String destInfo = (compteDest != null) ? compteDest.getNumero() : "N/A";
        return type + " - " + montant + " XOF - " + date + " - " + statut;
    }
}
