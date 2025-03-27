package isis.projet.backend.service;

import isis.projet.backend.entity.Mesure;
import isis.projet.backend.dao.MesureRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MesureService {

    private final MesureRepository mesureRepository;

    public MesureService(MesureRepository mesureRepository) {
        this.mesureRepository = mesureRepository;
    }

    public List<Mesure> getAllMesures() {
        return mesureRepository.findAll();
    }

    public Optional<Mesure> getMesureById(Integer id) {
        return mesureRepository.findById(id);
    }

    public Mesure createMesure(Mesure mesure) {
        return mesureRepository.save(mesure);
    }

    public void deleteMesure(Integer id) {
        mesureRepository.deleteById(id);
    }
}
