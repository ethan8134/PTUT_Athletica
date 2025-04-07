package isis.projet.backend.controller;

import isis.projet.backend.dao.MesureRepository;
import isis.projet.backend.entity.Mesure;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesures")
@CrossOrigin(origins = "http://localhost:3000") // adapte si besoin
public class MesureController {

    private final MesureRepository mesureRepository;

    public MesureController(MesureRepository mesureRepository) {
        this.mesureRepository = mesureRepository;
    }

    @GetMapping("/indicateur-global/{id}")
    public List<Mesure> getByIndicateurGlobal(@PathVariable Integer id) {
        return mesureRepository.findByIndicateurGlobalIdIndicateurGlobal(id);
    }

    @GetMapping("/indicateur-session/{id}")
    public List<Mesure> getByIndicateurSession(@PathVariable Integer id) {
        return mesureRepository.findByIndicateurSessionIdIndicateurSession(id);
    }

    @GetMapping
    public List<Mesure> getAllMesures() {
        return mesureRepository.findAll();
    }

    @PostMapping
    public Mesure addMesure(@RequestBody Mesure mesure) {
        return mesureRepository.save(mesure);
    }
}