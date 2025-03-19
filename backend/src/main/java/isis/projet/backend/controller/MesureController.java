package isis.projet.backend.controller;

import isis.projet.backend.entity.Mesure;
import isis.projet.backend.service.MesureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mesures")
@Slf4j
public class MesureController {

    private final MesureService mesureService;

    public MesureController(MesureService mesureService) {
        this.mesureService = mesureService;
    }

    @GetMapping
    public ResponseEntity<List<Mesure>> getAllMesures() {
        log.info("Récupération de toutes les mesures");
        return ResponseEntity.ok(mesureService.getAllMesures());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mesure> getMesureById(@PathVariable Integer id) {
        log.info("Récupération de la mesure avec l'id : {}", id);
        return mesureService.getMesureById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Mesure> createMesure(@RequestBody Mesure mesure) {
        log.info("Création d'une nouvelle mesure");
        return ResponseEntity.ok(mesureService.createMesure(mesure));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mesure> updateMesure(@PathVariable Integer id, @RequestBody Mesure mesure) {
        log.info("Mise à jour de la mesure avec l'id : {}", id);
        Mesure updated = mesureService.updateMesure(id, mesure);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMesure(@PathVariable Integer id) {
        log.info("Suppression de la mesure avec l'id : {}", id);
        boolean deleted = mesureService.deleteMesure(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
