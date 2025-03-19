package isis.projet.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Mesure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMesure;

    private String valeur;
    private LocalDateTime dateMesure;

    // Relation avec Utilisateur
    @ManyToOne
    @JoinColumn(name = "id_personne")
    private Utilisateur utilisateur;

    // Relation avec Session
    @ManyToOne
    @JoinColumn(name = "id_session")
    private Session session;

    // Relation avec IndicateurSession
    @ManyToOne
    @JoinColumn(name = "id_indicateur_session")
    private IndicateurSession indicateurSession;
}
