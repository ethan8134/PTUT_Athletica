package isis.projet.backend.controller;

import isis.projet.backend.entity.IndicateurSession;
import isis.projet.backend.service.IndicateurSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/indicateurs-session")
@Slf4j
public class IndicateurSessionController {

    private final IndicateurSessionService indicateurSessionService;

    public IndicateurSessionController(IndicateurSessionService indicateurSessionService) {
        this.indicateurSessionService = indicateurSessionService;
    }

    @GetMapping
    public ResponseEntity<List<IndicateurSession>> getAllIndicateurs() {
        return ResponseEntity.ok(indicateurSessionService.getAllIndicateurs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IndicateurSession> getIndicateurById(@PathVariable Integer id) {
        return indicateurSessionService.getIndicateurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<IndicateurSession> createIndicateur(@RequestBody IndicateurSession indicateur) {
        return ResponseEntity.ok(indicateurSessionService.createIndicateur(indicateur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IndicateurSession> updateIndicateur(@PathVariable Integer id, @RequestBody IndicateurSession indicateur) {
        IndicateurSession updated = indicateurSessionService.updateIndicateur(id, indicateur);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndicateur(@PathVariable Integer id) {
        boolean deleted = indicateurSessionService.deleteIndicateur(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
