package isis.projet.backend.service;

import isis.projet.backend.dao.CategorieRepository;
import isis.projet.backend.entity.Categorie;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
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

    public Categorie updateCategorie(Integer id, Categorie categorie) {
        return categorieRepository.findById(id)
                .map(existing -> {
                    existing.setNomCategorie(categorie.getNomCategorie());
                    return categorieRepository.save(existing);
                }).orElse(null);
    }

    public boolean deleteCategorie(Integer id) {
        if (categorieRepository.existsById(id)) {
            categorieRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
