package isis.projet.backend.controller;

import isis.projet.backend.entity.Categorie;
import isis.projet.backend.service.CategorieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategorieController {

    private final CategorieService categorieService;

    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    @GetMapping
    public List<Categorie> getAllCategories() {
        return categorieService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Optional<Categorie> getCategorieById(@PathVariable Integer id) {
        return categorieService.getCategorieById(id);
    }

    @PostMapping
    public Categorie createCategorie(@RequestBody Categorie categorie) {
        return categorieService.createCategorie(categorie);
    }

    @DeleteMapping("/{id}")
    public void deleteCategorie(@PathVariable Integer id) {
        categorieService.deleteCategorie(id);
    }
}
