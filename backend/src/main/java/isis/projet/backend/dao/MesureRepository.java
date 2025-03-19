package isis.projet.backend.dao;

import isis.projet.backend.entity.Mesure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MesureRepository extends JpaRepository<Mesure, Integer> {

}
