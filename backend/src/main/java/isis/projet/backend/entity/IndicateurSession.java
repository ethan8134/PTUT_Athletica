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
    @JoinColumn(name = "id_personne")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "id_session")
    private Session session;


    @ManyToOne
    @JoinColumn(name = "id_categorie")
    private Categorie categorie;

    @OneToMany(mappedBy = "indicateurSession", cascade = CascadeType.ALL)
    @JsonManagedReference // 👈 Ici on garde la relation
    private List<Mesure> mesures;
}

