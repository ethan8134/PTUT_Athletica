package isis.projet.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPersonne;

    private String pseudo;
    private String email;
    private String mdp;
    private String preferences;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<IndicateurGlobal> indicateursGlobaux;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<IndicateurSession> indicateursSession;

}
