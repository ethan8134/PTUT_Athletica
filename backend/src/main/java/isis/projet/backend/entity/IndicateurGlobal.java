package isis.projet.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class IndicateurGlobal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idIndicateurGlobal;

    private String nom;
    private String unite;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "id_personne")
    private Utilisateur utilisateur;
}
