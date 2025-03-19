package isis.projet.backend.controller;

import isis.projet.backend.entity.IndicateurGlobal;
import isis.projet.backend.service.IndicateurGlobalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/indicateurs-globaux")
@Slf4j
public class IndicateurGlobalController {

    private final IndicateurGlobalService indicateurGlobalService;

    public IndicateurGlobalController(IndicateurGlobalService indicateurGlobalService) {
        this.indicateurGlobalService = indicateurGlobalService;
    }

    @GetMapping
    public ResponseEntity<List<IndicateurGlobal>> getAllIndicateurs() {
        return ResponseEntity.ok(indicateurGlobalService.getAllIndicateurs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IndicateurGlobal> getIndicateurById(@PathVariable Integer id) {
        return indicateurGlobalService.getIndicateurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<IndicateurGlobal> createIndicateur(@RequestBody IndicateurGlobal indicateur) {
        return ResponseEntity.ok(indicateurGlobalService.createIndicateur(indicateur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IndicateurGlobal> updateIndicateur(@PathVariable Integer id, @RequestBody IndicateurGlobal indicateur) {
        IndicateurGlobal updated = indicateurGlobalService.updateIndicateur(id, indicateur);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndicateur(@PathVariable Integer id) {
        boolean deleted = indicateurGlobalService.deleteIndicateur(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
