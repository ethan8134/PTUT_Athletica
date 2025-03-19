package isis.projet.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter @Setter
public class IndicateurSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idIndicateurSession;

    private String nom;
    private String unite;
    private LocalDate date;

    // Relation avec Utilisateur
    @ManyToOne
    @JoinColumn(name = "id_personne")
    private Utilisateur utilisateur;

    // Relation avec Session
    @ManyToOne
    @JoinColumn(name = "id_session")
    private Session session;

    // Relation avec Catégorie (si besoin)
    @ManyToOne
    @JoinColumn(name = "id_categorie")
    private Categorie categorie;
}
