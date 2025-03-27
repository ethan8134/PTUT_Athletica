-- 🔹 Ajouter des utilisateurs
INSERT INTO utilisateur (id_personne, pseudo, email, mdp, preferences)
VALUES
    (1, 'Yasmine', 'yasmine@example.com', 'motdepasse1', 'Running'),
    (2, 'Ali', 'ali@example.com', 'motdepasse2', 'Trail'),
    (3, 'Sofia', 'sofia@example.com', 'motdepasse3', 'Marathon');

-- 🔹 Ajouter des catégories
INSERT INTO categorie (id_categorie, nom)
VALUES (1, 'Performance'),
       (2, 'Santé');


-- 🔹 Ajouter des sessions
INSERT INTO session (id_session, nom, date_session, fichier, id_personne)
VALUES (1, 'Matin Running', '2024-03-06', 'fichier1.txt', 1),
       (2, 'Course Montagne', '2024-03-05', 'fichier2.txt', 2);

-- 🔹 Ajouter des indicateurs globaux
INSERT INTO indicateur_global (id_indicateur_global, nom, unite, date, id_personne)
VALUES (1, 'Poids', 'kg', '2024-03-01', 1),
       (2, 'Heures de sommeil', 'heures', '2024-03-04', 3);


-- 🔹 Ajouter des indicateurs session
INSERT INTO indicateur_session (id_indicateur_session, nom, unite, date, id_personne, id_session, id_categorie)
VALUES (1, 'Vitesse', 'km/h', '2024-03-06', 1, 1, 1),
       (2, 'Fréquence cardiaque', 'bpm', '2024-03-05', 2, 2, 2);

INSERT INTO mesure (valeur, date_mesure, id_indicateur_session)
VALUES (12.5, '2024-03-01', 1),
       (14.8, '2024-03-02', 1),
       (75, '2024-03-01', 2),
       (80, '2024-03-03', 2);
