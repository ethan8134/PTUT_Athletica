package isis.projet.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class IndicateurSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idIndicateurSession;

    private String nom;
    private String unite;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "id_categorie")
    private Categorie categorie;

    @ManyToOne
    @JoinColumn(name = "id_personne")
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "indicateurSession", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Mesure> mesures;
}

