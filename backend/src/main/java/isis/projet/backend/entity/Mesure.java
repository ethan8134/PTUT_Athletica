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
    @JsonBackReference
    private IndicateurSession indicateurSession;

    @ManyToOne
    @JoinColumn(name = "id_indicateur_global")
    private IndicateurGlobal indicateurGlobal; // ✅ à ajouter

    @ManyToOne
    @JoinColumn(name = "id_session")
    private Session session;

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getValeur() { return valeur; }
    public void setValeur(Double valeur) { this.valeur = valeur; }

    public LocalDate getDateMesure() { return dateMesure; }
    public void setDateMesure(LocalDate dateMesure) { this.dateMesure = dateMesure; }

    public IndicateurSession getIndicateurSession() { return indicateurSession; }
    public void setIndicateurSession(IndicateurSession indicateurSession) {
        this.indicateurSession = indicateurSession;
    }

    public IndicateurGlobal getIndicateurGlobal() {
        return indicateurGlobal;
    }
    public void setIndicateurGlobal(IndicateurGlobal indicateurGlobal) {
        this.indicateurGlobal = indicateurGlobal;
    }

    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }
}
