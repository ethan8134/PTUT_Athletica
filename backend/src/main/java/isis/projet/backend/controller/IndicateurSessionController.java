package isis.projet.backend.controller;

import isis.projet.backend.entity.IndicateurSession;
import isis.projet.backend.dao.IndicateurSessionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/indicateurSessions")
public class IndicateurSessionController {

    private final IndicateurSessionRepository indicateurSessionRepository;

    public IndicateurSessionController(IndicateurSessionRepository indicateurSessionRepository) {
        this.indicateurSessionRepository = indicateurSessionRepository;
    }

    // GET : récupérer tous les indicateurs
    @GetMapping
    public List<IndicateurSession> getAllIndicateurs() {
        return indicateurSessionRepository.findAll();
    }
    @PostMapping
    public IndicateurSession createIndicateurSession(@RequestBody IndicateurSession indicateur) {
        return indicateurSessionRepository.save(indicateur);
    }


    // PUT : mise à jour d'un indicateur
    @PutMapping("/{id}")
    public IndicateurSession updateIndicateur(@PathVariable Integer id, @RequestBody IndicateurSession updatedIndicateur) {
        return indicateurSessionRepository.findById(id).map(ind -> {
            ind.setNom(updatedIndicateur.getNom());
            ind.setUnite(updatedIndicateur.getUnite());
            ind.setDate(updatedIndicateur.getDate());
            // Ici, tu peux mettre à jour d'autres champs si besoin
            return indicateurSessionRepository.save(ind);
        }).orElseThrow(() -> new RuntimeException("Indicateur non trouvé"));
    }

    // DELETE : suppression d'un indicateur
    @DeleteMapping("/{id}")
    public void deleteIndicateur(@PathVariable Integer id) {
        indicateurSessionRepository.deleteById(id);
    }
}
