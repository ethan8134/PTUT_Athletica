-- 🔹 Ajouter des utilisateurs (auto-incrément)
INSERT INTO utilisateur (pseudo, email, mdp, preferences)
VALUES
    ('Yasmine', 'yasmine@example.com', 'motdepasse1', 'Running'),
    ('Ali', 'ali@example.com', 'motdepasse2', 'Trail'),
    ('Sofia', 'sofia@example.com', 'motdepasse3', 'Marathon');

-- 🔹 Repositionner la séquence de l'utilisateur
ALTER SEQUENCE utilisateur_id_personne_seq RESTART WITH 4;

-- 🔹 Ajouter des catégories
INSERT INTO categorie (nom)
VALUES
    ('Performance'),
    ('Santé');

ALTER SEQUENCE categorie_id_categorie_seq RESTART WITH 3;

-- 🔹 Ajouter des sessions
INSERT INTO session (nom, date_session, fichier, id_personne)
VALUES
    ('Matin Running', '2024-03-06', 'fichier1.txt', 1),
    ('Course Montagne', '2024-03-05', 'fichier2.txt', 2);

ALTER SEQUENCE session_id_session_seq RESTART WITH 3;

-- 🔹 Ajouter des indicateurs globaux
INSERT INTO indicateur_global (nom, unite, date, id_personne)
VALUES
    ('Poids', 'kg', '2024-03-01', 1),
    ('Heures de sommeil', 'heures', '2024-03-04', 3);

ALTER SEQUENCE indicateur_global_id_indicateur_global_seq RESTART WITH 3;

-- 🔹 Ajouter des indicateurs de session
INSERT INTO indicateur_session (nom, unite, date, id_personne, id_categorie)
VALUES
    ('Vitesse', 'km/h', '2024-03-06', 1, 1),
    ('Fréquence cardiaque', 'bpm', '2024-03-05', 2, 2);

ALTER SEQUENCE indicateur_session_id_indicateur_session_seq RESTART WITH 3;

-- 🔹 Ajouter des mesures liées aux indicateurs globaux
INSERT INTO mesure (valeur, date_mesure, id_indicateur_global)
VALUES
    (65.5, '2024-03-01', 1),
    (67.2, '2024-03-03', 1),
    (6.5,  '2024-03-04', 2),
    (7.8,  '2024-03-06', 2);

-- 🔹 Ajouter des mesures liées aux indicateurs de session
INSERT INTO mesure (valeur, date_mesure, id_indicateur_session, id_session)
VALUES
    (12.5, '2024-03-01', 1, 1),
    (14.8, '2024-03-02', 1, 1),
    (75.0, '2024-03-01', 2, 2),
    (80.0, '2024-03-03', 2, 2);

-- 🔧 Réinitialiser la séquence des mesures
ALTER SEQUENCE mesure_id_seq RESTART WITH 9;
