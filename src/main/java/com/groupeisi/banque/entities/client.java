package com.groupeisi.banque.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

// Classe representant un client de la banque
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String dateInscription;
    private String statut; // Actif/Inactif

    // Un client peut avoir plusieurs comptes
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<compte> comptes;

    // Un client peut avoir plusieurs credits
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<credit> credits;
    
    @Override
    public String toString() {
        return prenom + " " + nom + " (" + email + ")";
    }
}
