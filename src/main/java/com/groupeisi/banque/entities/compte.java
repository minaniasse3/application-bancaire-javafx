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
public class compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numero;
    private String type; // courant/epargne
    private double solde;
    private String dateCreation;

    // Un compte appartient a un seul client
    @ManyToOne
    @JoinColumn(name = "client_id")
    private client client;

    // Un compte peut avoir plusieurs transactions
    @OneToMany(mappedBy = "compteSource", cascade = CascadeType.ALL)
    private List<transaction> transactions;

    // Un compte peut avoir plusieurs cartes bancaires
    @OneToMany(mappedBy = "compte", cascade = CascadeType.ALL)
    private List<carteBancaire> cartes;
    
    @Override
    public String toString() {
        String clientInfo = (client != null) ? client.getNom() + " " + client.getPrenom() : "Non attribué";
        return type + " - " + numero + " - " + solde + " XOF (" + clientInfo + ")";
    }
}
