package isis.projet.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
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



}
