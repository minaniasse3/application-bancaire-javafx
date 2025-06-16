package com.groupeisi.banque.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double montant;
    private double tauxInteret;
    private int dureeMois;
    private double mensualite;
    private String dateDemande;
    private String statut; // En cours/Termine/Rejete

    // Un credit est lie a un client
    @ManyToOne
    @JoinColumn(name = "client_id")
    private client client;

    // Un credit peut avoir plusieurs remboursements
    @OneToMany(mappedBy = "credit", cascade = CascadeType.ALL)
    private List<remboursement> remboursements;

    // Méthode alias pour la compatibilité avec le code existant
    public int getDuree_mois() {
        return dureeMois;
    }
    
    public void setDuree_mois(int dureeMois) {
        this.dureeMois = dureeMois;
    }
}
