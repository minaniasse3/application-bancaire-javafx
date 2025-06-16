package com.groupeisi.banque.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class carteBancaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numero;
    private String cvv;
    private String dateExpiration;
    private double solde;
    private String statut; // active/bloquee
    private String codePin;

    // Une carte est associee a un seul compte
    @ManyToOne
    @JoinColumn(name = "compte_id")
    private compte compte;
}
