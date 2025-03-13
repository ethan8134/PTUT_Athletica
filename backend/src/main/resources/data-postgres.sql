-- Initialisation des tables
-- Initialisation des tables
INSERT INTO Indicateur(id, nom, valeur, unite, categorie) VALUES
                                                              (1, 'DIST', '10', 'km', 'Course'),         -- Distance parcourue : 10 km
                                                              (2, 'TEMPS', '42', 'min', 'Course'),         -- Temps réalisé : 42 minutes
                                                              (3, 'PACE', '4.2', 'min/km', 'Course'),       -- Allure moyenne : 4,2 min/km
                                                              (4, 'CAL', '800', 'kcal', 'Course'),          -- Calories brûlées : 800 kcal
                                                              (5, 'FREQ', '150', 'bpm', 'Course'),          -- Fréquence cardiaque moyenne : 150 bpm
    (6, 'ELE', '30', 'm', 'Course');              -- Dénivelé positif : 30 m
-- Si on fixe les clés auto-générées, il faut réinitialiser le compteur
-- Attention, cette commande n'est pas standard SQL
-- ici la syntaxe pour PostgreSQL
ALTER SEQUENCE indicateur_id_seq RESTART WITH 4;

-- Insertion des sessions
INSERT INTO Session (id, nom, date) VALUES
                                        (1, 'Session Running', '2025-03-12'),
                                        (2, 'Session Cycling', '2025-03-13'),
                                        (3, 'Session Swimming', '2025-03-14'),
                                        (4, 'Session Yoga', '2025-03-15');

-- Réinitialisation du compteur d'auto-incrément (spécifique à H2)
ALTER SEQUENCE session_id_seq RESTART WITH 5;
