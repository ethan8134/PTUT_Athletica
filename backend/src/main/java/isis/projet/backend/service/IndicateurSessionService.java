package isis.projet.backend.service;

import isis.projet.backend.dao.IndicateurSessionRepository;
import isis.projet.backend.entity.IndicateurSession;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class IndicateurSessionService {

    private final IndicateurSessionRepository indicateurSessionRepository;

    public IndicateurSessionService(IndicateurSessionRepository indicateurSessionRepository) {
        this.indicateurSessionRepository = indicateurSessionRepository;
    }

    public List<IndicateurSession> getAllIndicateurs() {
        return indicateurSessionRepository.findAll();
    }

    public Optional<IndicateurSession> getIndicateurById(Integer id) {
        return indicateurSessionRepository.findById(id);
    }

    public IndicateurSession createIndicateur(IndicateurSession indicateur) {
        return indicateurSessionRepository.save(indicateur);
    }

    public IndicateurSession updateIndicateur(Integer id, IndicateurSession indicateur) {
        return indicateurSessionRepository.findById(id)
                .map(existing -> {
                    existing.setNom(indicateur.getNom());
                    existing.setUnite(indicateur.getUnite());
                    existing.setDate(indicateur.getDate());
                    return indicateurSessionRepository.save(existing);
                }).orElse(null);
    }

    public boolean deleteIndicateur(Integer id) {
        if (indicateurSessionRepository.existsById(id)) {
            indicateurSessionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
