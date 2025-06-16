package com.groupeisi.banque.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ticketSupport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sujet;
    private String description;
    private String dateOuverture;
    private String statut; // Ouvert/Ferme

    // Un ticket est cree par un client
    @ManyToOne
    @JoinColumn(name = "client_id")
    private client client;

    // Un ticket est traite par un administrateur
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private admin admin;

    public void setNumero(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setNumero'");
    }
}
