package isis.projet.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter @Setter
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSession;

    private String nom;
    private LocalDate dateSession;
    private String fichier;

    @ManyToOne
    @JoinColumn(name = "id_personne")
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<Mesure> mesures;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<IndicateurSession> indicateursSession;

}
