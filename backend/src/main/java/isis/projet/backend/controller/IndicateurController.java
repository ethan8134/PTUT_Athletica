package isis.projet.backend.controller;

import isis.projet.backend.entity.Indicateur;
import isis.projet.backend.service.IndicateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@RestController
@RequestMapping("/api/indicateurs")
@Slf4j
public class IndicateurController {

    private final IndicateurService indicateurService;

    public IndicateurController(IndicateurService indicateurService) {
        this.indicateurService = indicateurService;
    }

    // GET : Récupérer tous les indicateurs
    @GetMapping
    public ResponseEntity<List<Indicateur>> getAllIndicateurs() {
        log.info("Récupération de tous les indicateurs");
        List<Indicateur> indicateurs = indicateurService.getAllIndicateurs();
        return ResponseEntity.ok(indicateurs);
    }

    // GET : Récupérer un indicateur par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Indicateur> getIndicateurById(@PathVariable Integer id) {
        log.info("Récupération de l'indicateur avec l'id: {}", id);
        Indicateur indicateur = indicateurService.getIndicateurById(id);
        if (indicateur != null) {
            return ResponseEntity.ok(indicateur);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST : Créer un nouvel indicateur
    @PostMapping
    public ResponseEntity<Indicateur> createIndicateur(@RequestBody Indicateur indicateur) {
        log.info("Création d'un nouvel indicateur");
        Indicateur createdIndicateur = indicateurService.createIndicateur(indicateur);
        return ResponseEntity.ok(createdIndicateur);
    }

    // PUT : Mettre à jour un indicateur existant
    @PutMapping("/{id}")
    public ResponseEntity<Indicateur> updateIndicateur(@PathVariable Integer id, @RequestBody Indicateur indicateur) {
        log.info("Mise à jour de l'indicateur avec l'id: {}", id);
        Indicateur updatedIndicateur = indicateurService.updateIndicateur(id, indicateur);
        if (updatedIndicateur != null) {
            return ResponseEntity.ok(updatedIndicateur);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE : Supprimer un indicateur par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndicateur(@PathVariable Integer id) {
        log.info("Suppression de l'indicateur avec l'id: {}", id);
        boolean deleted = indicateurService.deleteIndicateur(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // GET : Compter le nombre total d'indicateurs
    @GetMapping("/count")
    public ResponseEntity<Long> countIndicateurs() {
        log.info("Comptage des indicateurs");
        Long count = indicateurService.countIndicateurs();
        return ResponseEntity.ok(count);
    }
}
