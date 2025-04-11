package isis.projet.backend.service;

import isis.projet.backend.dao.IndicateurGlobalRepository;
import isis.projet.backend.entity.IndicateurGlobal;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class IndicateurGlobalService {

    private final IndicateurGlobalRepository indicateurGlobalRepository;

    public IndicateurGlobalService(IndicateurGlobalRepository indicateurGlobalRepository) {
        this.indicateurGlobalRepository = indicateurGlobalRepository;
    }

    public List<IndicateurGlobal> getAllIndicateurs() {
        return indicateurGlobalRepository.findAll();
    }

    public Optional<IndicateurGlobal> getIndicateurById(Integer id) {
        return indicateurGlobalRepository.findById(id);
    }

    public IndicateurGlobal createIndicateur(IndicateurGlobal indicateur) {
        return indicateurGlobalRepository.save(indicateur);
    }

    public IndicateurGlobal updateIndicateur(Integer id, IndicateurGlobal indicateur) {
        return indicateurGlobalRepository.findById(id)
                .map(existing -> {
                    existing.setNom(indicateur.getNom());
                    existing.setUnite(indicateur.getUnite());
                    existing.setDate(indicateur.getDate());
                    existing.setUtilisateur(indicateur.getUtilisateur());
                    return indicateurGlobalRepository.save(existing);
                }).orElse(null);
    }

    public boolean deleteIndicateur(Integer id) {
        if (indicateurGlobalRepository.existsById(id)) {
            indicateurGlobalRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
