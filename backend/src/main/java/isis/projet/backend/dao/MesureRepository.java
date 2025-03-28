package isis.projet.backend.dao;

import isis.projet.backend.entity.Mesure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MesureRepository extends JpaRepository<Mesure, Integer> {
    List<Mesure> findByIndicateurSessionIdIndicateurSession(Integer idIndicateurSession);
}
