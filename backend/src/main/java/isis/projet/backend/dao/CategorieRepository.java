package isis.projet.backend.dao;

import isis.projet.backend.entity.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository extends JpaRepository<Categorie, Integer> {

}
