package isis.projet.backend.controller;

import isis.projet.backend.entity.Session;
import isis.projet.backend.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@Slf4j
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    // GET : Récupérer toutes les sessions
    @GetMapping
    public ResponseEntity<List<Session>> getAllSessions() {
        log.info("Récupération de toutes les sessions");
        List<Session> sessions = sessionService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }

    // GET : Récupérer une session par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable Integer id) {
        log.info("Récupération de la session avec l'id : {}", id);
        return sessionService.getSessionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST : Créer une nouvelle session
    @PostMapping
    public ResponseEntity<Session> createSession(@RequestBody Session session) {
        log.info("Création d'une nouvelle session");
        Session created = sessionService.createSession(session);
        return ResponseEntity.ok(created);
    }

    // PUT : Mettre à jour une session existante
    @PutMapping("/{id}")
    public ResponseEntity<Session> updateSession(@PathVariable Integer id, @RequestBody Session session) {
        log.info("Mise à jour de la session avec l'id : {}", id);
        Session updated = sessionService.updateSession(id, session);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE : Supprimer une session par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Integer id) {
        log.info("Suppression de la session avec l'id : {}", id);
        boolean deleted = sessionService.deleteSession(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
