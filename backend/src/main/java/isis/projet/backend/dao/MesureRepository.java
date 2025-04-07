package isis.projet.backend.dao;

import isis.projet.backend.entity.Mesure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import java.util.List;

public interface MesureRepository extends JpaRepository<Mesure, Integer> {

    // Mesures liées à un indicateur de session
    List<Mesure> findByIndicateurSessionIdIndicateurSession(Integer idIndicateurSession);

    // Mesures liées à un indicateur global
    List<Mesure> findByIndicateurGlobalIdIndicateurGlobal(Integer idIndicateurGlobal);

    // 💥 Supprimer toutes les mesures liées à une session
    @Modifying
    @Transactional
    @Query("DELETE FROM Mesure m WHERE m.session.idSession = :sessionId")
    void deleteBySessionId(@Param("sessionId") Integer sessionId);
}
