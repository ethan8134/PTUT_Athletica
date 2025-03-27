package isis.projet.backend.controller;

import isis.projet.backend.dao.MesureRepository;
import isis.projet.backend.entity.Mesure;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesures")
public class MesureController {

    private final MesureRepository mesureRepository;

    public MesureController(MesureRepository mesureRepository) {
        this.mesureRepository = mesureRepository;
    }

    @GetMapping("/indicateur/{idIndicateurSession}")
    public List<Mesure> getByIndicateurSession(@PathVariable Integer idIndicateurSession) {
        return mesureRepository.findByIndicateurSessionIdIndicateurSession(idIndicateurSession);
    }

    @PostMapping
    public Mesure addMesure(@RequestBody Mesure mesure) {
        return mesureRepository.save(mesure);
    }
}
