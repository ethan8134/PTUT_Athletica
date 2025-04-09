package isis.projet.backend.dao;

import isis.projet.backend.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Integer> {
    Optional<Session> findByNom(String nom);

}
