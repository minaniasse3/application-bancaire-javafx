package com.groupeisi.banque.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class remboursement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double montant;
    private String date;

    // Un remboursement est lie a un credit
    @ManyToOne
    @JoinColumn(name = "credit_id")
    private credit credit;
}
