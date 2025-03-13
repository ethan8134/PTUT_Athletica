package isis.projet.backend.service;

import isis.projet.backend.dao.IndicateurRepository;
import isis.projet.backend.entity.Indicateur;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class IndicateurService {

    private final IndicateurRepository indicateurRepository;

    public IndicateurService(IndicateurRepository indicateurRepository) {
        this.indicateurRepository = indicateurRepository;
    }

    // Retourne le nombre total d'indicateurs
    public long countIndicateurs() {
        return indicateurRepository.count();
    }

    // Récupère la liste de tous les indicateurs
    public List<Indicateur> getAllIndicateurs() {
        return indicateurRepository.findAll();
    }

    // Récupère un indicateur par son identifiant
    public Indicateur getIndicateurById(Integer id) {
        return indicateurRepository.findById(id).orElse(null);
    }

    // Crée un nouvel indicateur
    public Indicateur createIndicateur(Indicateur indicateur) {
        return indicateurRepository.save(indicateur);
    }

    // Met à jour un indicateur existant identifié par son id
    public Indicateur updateIndicateur(Integer id, Indicateur indicateur) {
        return indicateurRepository.findById(id)
                .map(existing -> {
                    existing.setNom(indicateur.getNom());
                    existing.setValeur(indicateur.getValeur());
                    existing.setUnite(indicateur.getUnite());
                    existing.setCategorie(indicateur.getCategorie());
                    return indicateurRepository.save(existing);
                }).orElse(null);
    }

    // Supprime un indicateur par son id
    public boolean deleteIndicateur(Integer id) {
        if(indicateurRepository.existsById(id)) {
            indicateurRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
