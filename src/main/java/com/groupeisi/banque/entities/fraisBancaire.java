package com.groupeisi.banque.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class fraisBancaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type; // Frais de gestion, penalite, etc.
    private double montant;
    private String dateApplication;

    // Les frais sont appliques a un compte
    @ManyToOne
    @JoinColumn(name = "compte_id")
    private compte compte;

}