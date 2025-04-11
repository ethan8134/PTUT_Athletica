package isis.projet.backend.controller;

import isis.projet.backend.entity.IndicateurGlobal;
import isis.projet.backend.dao.IndicateurGlobalRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/indicateurGlobals")
@CrossOrigin(origins = "http://localhost:3000")
public class IndicateurGlobalController {

    private final IndicateurGlobalRepository indicateurGlobalRepository;

    public IndicateurGlobalController(IndicateurGlobalRepository indicateurGlobalRepository) {
        this.indicateurGlobalRepository = indicateurGlobalRepository;
    }

    @GetMapping
    public List<IndicateurGlobal> getAllIndicateursGlobaux() {
        return indicateurGlobalRepository.findAll();
    }

    @PostMapping
    public IndicateurGlobal createIndicateurGlobal(@RequestBody IndicateurGlobal indicateur) {
        return indicateurGlobalRepository.save(indicateur);
    }

    @PutMapping("/{id}")
    public IndicateurGlobal updateIndicateurGlobal(@PathVariable Integer id, @RequestBody IndicateurGlobal updatedIndicateur) {
        return indicateurGlobalRepository.findById(id).map(ind -> {
            ind.setNom(updatedIndicateur.getNom());
            ind.setUnite(updatedIndicateur.getUnite());
            ind.setDate(updatedIndicateur.getDate());
            return indicateurGlobalRepository.save(ind);
        }).orElseThrow(() -> new RuntimeException("Indicateur Global non trouvé"));
    }

    @DeleteMapping("/{id}")
    public void deleteIndicateurGlobal(@PathVariable Integer id) {
        indicateurGlobalRepository.deleteById(id);
    }
}
