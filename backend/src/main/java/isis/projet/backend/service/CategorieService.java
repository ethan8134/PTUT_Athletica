package isis.projet.backend.service;

import isis.projet.backend.entity.Categorie;
import isis.projet.backend.dao.CategorieRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategorieService {

    private final CategorieRepository categorieRepository;

    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }

    public Optional<Categorie> getCategorieById(Integer id) {
        return categorieRepository.findById(id);
    }

    public Categorie createCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    public void deleteCategorie(Integer id) {
        categorieRepository.deleteById(id);
    }
}
