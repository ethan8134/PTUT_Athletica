package isis.projet.backend.dao;

import isis.projet.backend.entity.IndicateurSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IndicateurSessionRepository extends JpaRepository<IndicateurSession, Integer> {
    Optional<IndicateurSession> findByNomAndUniteAndUtilisateurIdPersonne(String nom, String unite, Integer idPersonne);

}
