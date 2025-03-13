package isis.projet.backend.service;

import isis.projet.backend.dao.SessionRepository;
import isis.projet.backend.entity.Session;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Optional<Session> getSessionById(Integer id) {
        return sessionRepository.findById(id);
    }

    public Session createSession(Session session) {
        return sessionRepository.save(session);
    }

    public Session updateSession(Integer id, Session session) {
        return sessionRepository.findById(id)
                .map(existing -> {
                    existing.setNom(session.getNom());
                    existing.setDate(session.getDate());
                    return sessionRepository.save(existing);
                }).orElse(null);
    }

    public boolean deleteSession(Integer id) {
        if (sessionRepository.existsById(id)) {
            sessionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
