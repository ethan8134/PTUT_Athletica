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

            Utilisateur user = utilisateurRepository.findById(1).orElse(null); // à adapter si besoin

            for (String ligne : lignes) {
                String[] parts = ligne.split(",");

                if (parts.length >= 5) {
                    // 🔄 Traitement indicateur de session
                    String nomInd = parts[0].trim();
                    String unite = parts[1].trim();
                    double valeur = Double.parseDouble(parts[2].trim());
                    LocalDate date = LocalDate.parse(parts[3].trim());
                    String nomSession = parts[4].trim();

                    // Trouver ou créer la session
                    Session session = sessionRepository.findByNom(nomSession)
                            .orElseGet(() -> {
                                Session s = new Session();
                                s.setNom(nomSession);
                                s.setDateSession(date);
                                return sessionRepository.save(s);
                            });

                    IndicateurSession indicateur = new IndicateurSession();
                    indicateur.setNom(nomInd);
                    indicateur.setUnite(unite);
                    Categorie cat = categorieRepository.findById(1).orElse(null);
                    indicateur.setCategorie(cat);

                    indicateur.setUtilisateur(user);
                    indicateurSessionRepository.save(indicateur);

                    Mesure mesure = new Mesure();
                    mesure.setValeur(valeur);
                    mesure.setDateMesure(date);
                    mesure.setSession(session);
                    mesure.setIndicateurSession(indicateur);

                    mesureRepository.save(mesure);

                } else if (parts.length >= 4) {
                    // ✅ Traitement indicateur global
                    String nomInd = parts[0].trim();
                    String unite = parts[1].trim();
                    LocalDate date = LocalDate.parse(parts[3].trim());

                    IndicateurGlobal ind = new IndicateurGlobal();
                    ind.setNom(nomInd);
                    ind.setUnite(unite);
                    ind.setDate(date);
                    ind.setUtilisateur(user);
                    indicateurGlobalRepository.save(ind);
                }
            }

            return ResponseEntity.ok("✅ Fichier importé avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Erreur lors de l'importation.");
        }
    }
}
