package isis.projet.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Mesure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double valeur;

    @Column(name = "date_mesure")
    private LocalDate dateMesure;

    @ManyToOne
    @JoinColumn(name = "id_indicateur_session")
    @JsonBackReference // 👈 Ici on indique : "ne pas re-serialiser ce champ"
    private IndicateurSession indicateurSession;


    // ✅ GETTERS ET SETTERS OBLIGATOIRES

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public LocalDate getDateMesure() {
        return dateMesure;
    }

    public void setDateMesure(LocalDate dateMesure) {
        this.dateMesure = dateMesure;
    }

    public IndicateurSession getIndicateurSession() {
        return indicateurSession;
    }

    public void setIndicateurSession(IndicateurSession indicateurSession) {
        this.indicateurSession = indicateurSession;
    }
}

