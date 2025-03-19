package isis.projet.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCategorie;

    private String nomCategorie;

    // Relations vers les indicateurs globaux
    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL)
    private List<IndicateurGlobal> indicateursGlobaux;

    // Relations vers les indicateurs session
    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL)
    private List<IndicateurSession> indicateursSession;
}
