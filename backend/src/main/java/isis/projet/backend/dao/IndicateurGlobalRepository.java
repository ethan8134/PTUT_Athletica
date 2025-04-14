package isis.projet.backend.dao;

import isis.projet.backend.entity.IndicateurGlobal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IndicateurGlobalRepository extends JpaRepository<IndicateurGlobal, Integer> {
    Optional<IndicateurGlobal> findByNomAndUniteAndUtilisateurIdPersonne(String nom, String unite, Integer idPersonne);

}
