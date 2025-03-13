-- Insertion des indicateurs de course
INSERT INTO Indicateur (id, nom, valeur, unite, categorie) VALUES
    (1, 'DISTANCE', '10', 'km', 'Course'),         -- Distance parcourue : 10 km
    (2, 'TEMPS', '42', 'min', 'Course'),         -- Temps réalisé : 42 minutes
    (3, 'VITESSE', '4.2', 'min/km', 'Course'),       -- Allure moyenne : 4,2 min/km
    (4, 'CAL', '800', 'kcal', 'Course'),          -- Calories brûlées : 800 kcal
    (5, 'RYTHME CAR', '150', 'bpm', 'Course'),          -- Fréquence cardiaque moyenne : 150 bpm
    (6, 'DENIVELE', '30', 'm', 'Course');              -- Dénivelé positif : 30 m

-- Réinitialisation du compteur d'auto-incrément (spécifique à H2)
ALTER TABLE Indicateur ALTER COLUMN id RESTART WITH 7;

-- Insertion des sessions
INSERT INTO Session (id, nom, date) VALUES
                                        (1, 'Session Running', '2025-03-12'),
                                        (2, 'Session Cycling', '2025-03-13'),
                                        (3, 'Session Swimming', '2025-03-14'),
                                        (4, 'Session Yoga', '2025-03-15');

-- Réinitialisation du compteur d'auto-incrément (spécifique à H2)
ALTER TABLE Session ALTER COLUMN id RESTART WITH 5;

