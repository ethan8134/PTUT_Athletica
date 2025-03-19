package isis.projet.backend.service;

import isis.projet.backend.dao.MesureRepository;
import isis.projet.backend.entity.Mesure;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
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

    public Mesure updateMesure(Integer id, Mesure mesure) {
        return mesureRepository.findById(id)
                .map(existing -> {
                    existing.setValeur(mesure.getValeur());
                    existing.setDateMesure(mesure.getDateMesure());
                    return mesureRepository.save(existing);
                }).orElse(null);
    }

    public boolean deleteMesure(Integer id) {
        if (mesureRepository.existsById(id)) {
            mesureRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
