package isis.projet.backend.dao;

import isis.projet.backend.entity.Indicateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndicateurRepository extends JpaRepository<Indicateur, Integer> {
}