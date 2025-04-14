package isis.projet.backend.controller;

import isis.projet.backend.dao.*;
import isis.projet.backend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ImportController {

    @Autowired
    private IndicateurGlobalRepository indicateurGlobalRepository;

    @Autowired
    private IndicateurSessionRepository indicateurSessionRepository;

    @Autowired
    private CategorieRepository categorieRepository;


    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private MesureRepository mesureRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @PostMapping("/import")
    public ResponseEntity<String> importCSV(@RequestParam("file") MultipartFile file) {
        try {
            List<String> lignes = new BufferedReader(new InputStreamReader(file.getInputStream()))
                    .lines()
                    .skip(1)
                    .collect(Collectors.toList());

            Utilisateur user = utilisateurRepository.findById(1).orElse(null);
            if (user == null) return ResponseEntity.status(500).body("Utilisateur non trouvé");

            int lignesTraitees = 0;
            int doublonsIgnores = 0;

            for (String ligne : lignes) {
                String[] parts = ligne.split(",");

                if (parts.length >= 5) {
                    String nomInd = parts[0].trim();
                    String unite = parts[1].trim();
                    double valeur = Double.parseDouble(parts[2].trim());
                    LocalDate date = LocalDate.parse(parts[3].trim());
                    String nomSession = parts[4].trim();

                    // 1. Session
                    Session session = sessionRepository.findByNom(nomSession).orElseGet(() -> {
                        Session s = new Session();
                        s.setNom(nomSession);
                        s.setDateSession(date);
                        s.setUtilisateur(user);
                        return sessionRepository.save(s);
                    });

                    // 2. Indicateur de session
                    Optional<IndicateurSession> optionalIndicateur =
                            indicateurSessionRepository.findByNomAndUniteAndUtilisateurIdPersonne(nomInd, unite, user.getIdPersonne());

                    IndicateurSession indicateur = optionalIndicateur.orElseGet(() -> {
                        IndicateurSession ind = new IndicateurSession();
                        ind.setNom(nomInd);
                        ind.setUnite(unite);
                        ind.setDate(date);
                        ind.setUtilisateur(user);
                        ind.setCategorie(categorieRepository.findById(1).orElse(null));
                        return indicateurSessionRepository.save(ind);
                    });

                    // 3. Mesure
                    List<Mesure> mesures = mesureRepository.findByIndicateurSessionIdIndicateurSession(indicateur.getIdIndicateurSession());
                    boolean dejaExiste = mesures.stream().anyMatch(m ->
                            m.getValeur() == valeur &&
                                    m.getSession().getIdSession().equals(session.getIdSession()) &&
                                    m.getDateMesure().equals(date));

                    if (dejaExiste) {
                        doublonsIgnores++;
                        continue;
                    }

                    Mesure mesure = new Mesure();
                    mesure.setValeur(valeur);
                    mesure.setDateMesure(date);
                    mesure.setSession(session);
                    mesure.setIndicateurSession(indicateur);
                    mesureRepository.save(mesure);
                    lignesTraitees++;

                } else if (parts.length >= 4) {
                    // Cas indicateur global
                    String nomInd = parts[0].trim();
                    String unite = parts[1].trim();
                    double valeur = Double.parseDouble(parts[2].trim());
                    LocalDate date = LocalDate.parse(parts[3].trim());

                    Optional<IndicateurGlobal> optionalInd =
                            indicateurGlobalRepository.findByNomAndUniteAndUtilisateurIdPersonne(nomInd, unite, user.getIdPersonne());

                    IndicateurGlobal ind = optionalInd.orElseGet(() -> {
                        IndicateurGlobal g = new IndicateurGlobal();
                        g.setNom(nomInd);
                        g.setUnite(unite);
                        g.setDate(date);
                        g.setUtilisateur(user);
                        return indicateurGlobalRepository.save(g);
                    });

                    List<Mesure> mesures = mesureRepository.findByIndicateurGlobalIdIndicateurGlobal(ind.getIdIndicateurGlobal());
                    boolean dejaExiste = mesures.stream().anyMatch(m ->
                            m.getValeur() == valeur &&
                                    m.getDateMesure().equals(date));

                    if (dejaExiste) {
                        doublonsIgnores++;
                        continue;
                    }

                    Mesure mesure = new Mesure();
                    mesure.setValeur(valeur);
                    mesure.setDateMesure(date);
                    mesure.setIndicateurGlobal(ind);
                    mesureRepository.save(mesure);
                    lignesTraitees++;
                }
            }

            return ResponseEntity.ok("Fichier importé avec succès !\nRésumé : "
                    + lignesTraitees + " lignes traitées, "
                    + doublonsIgnores + " doublons ignorés.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'importation: " + e.getMessage());
        }
    }
}