package isis.projet.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter @Setter
public class IndicateurGlobal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idIndicateurGlobal;

    private String nom;
    private String unite;
    private LocalDate date;

    // Relation avec Utilisateur
    @ManyToOne
    @JoinColumn(name = "id_personne")
    private Utilisateur utilisateur;

    // Relation avec Catégorie (si tu en as besoin)
    @ManyToOne
    @JoinColumn(name = "id_categorie")
    private Categorie categorie;
}
