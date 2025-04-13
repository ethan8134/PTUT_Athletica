<template>
  <div class="list-container">
    <div class="search-container">
      <div class="dropdown">
        <button @click="toggleDropdown">{{ labelType }} ▼</button>
        <div v-if="showDropdown" class="dropdown-menu">
          <button @click="selectType('')">Toutes catégories</button>
          <button @click="selectType('global')">Indicateurs Globaux</button>
          <button @click="selectType('session')">Indicateurs Session</button>
        </div>
      </div>
    </div>

    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>Nom</th>
            <th>Unité</th>
            <th>Catégorie</th>
            <th>Type</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="ind in paginatedIndicateurs" :key="ind.id">
            <template v-if="ind.editing">
              <td><input v-model="ind.nom" /></td>
              <td><input v-model="ind.unite" /></td>
              <td><input v-model="ind.categorie.nom" /></td>
              <td>{{ ind.type === "global" ? "Global" : "Session" }}</td>
              <td class="action-buttons">
                <button @click="saveEdit(ind)">💾 Enregistrer</button>
                <button @click="ind.editing = false">Annuler</button>
              </td>
            </template>
            <template v-else>
              <td>{{ ind.nom }}</td>
              <td>{{ ind.unite }}</td>
              <td>{{ ind.categorie.nom }}</td>
              <td>{{ ind.type === "global" ? "Global" : "Session" }}</td>
              <td class="action-buttons">
                <button @click="startEdit(ind)">Modifier</button>
                <button @click="deleteIndicateur(ind)">Supprimer</button>
                <button
                  v-if="ind.type === 'global'"
                  @click="ajouterValeur(ind)"
                  class="ajouter-valeur"
                >
                  ➕ Ajouter valeur
                </button>
              </td>
            </template>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="pagination-controls">
      <button :disabled="currentPage === 1" @click="currentPage--">
        ← Précédent
      </button>
      <span>Page {{ currentPage }} / {{ totalPages }}</span>
      <button :disabled="currentPage === totalPages" @click="currentPage++">
        Suivant →
      </button>
    </div>

    <v-dialog v-model="showPopup" max-width="500px">
      <v-card>
        <v-card-title class="headline">Ajouter une mesure</v-card-title>
        <v-card-text>
          <v-text-field
            label="Valeur"
            type="number"
            v-model="newMesure.valeur"
            required
          />
          <v-text-field
            label="Date"
            type="date"
            v-model="newMesure.dateMesure"
            required
          />
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn color="blue" @click="validerMesure">Valider</v-btn>
          <v-btn color="grey" @click="showPopup = false">Annuler</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";

const indicateur = ref([]);
const showDropdown = ref(false);
const selectedType = ref("");
const labelType = ref("Filtrer Catégorie");
const currentPage = ref(1);
const itemsPerPage = 5;

const showPopup = ref(false);
const selectedIndicateur = ref(null);
const newMesure = ref({ valeur: "", dateMesure: "" });

const toggleDropdown = () => {
  // Toggle le menu déroulant
  showDropdown.value = !showDropdown.value; // Ferme le menu si on clique en dehors
};

const selectType = (type) => {
  // Selection du type d'indicateur
  selectedType.value = type;
  currentPage.value = 1;
  showDropdown.value = false;

  if (type === "") {
    labelType.value = "Toutes catégories";
  } else if (type === "global") {
    labelType.value = "Indicateurs Globaux";
  } else if (type === "session") {
    labelType.value = "Indicateurs Session";
  }

  filterIndicateurs();
};

const allIndicateurs = ref([]);

function getIndicateurs() {
  // Récupération des indicateurs depuis l'API
  fetch("http://localhost:8989/api/indicateurGlobals") // Récupération des indicateurs globaux
    .then((res) => res.json())
    .then((dataGlobals) => {
      const globals = dataGlobals.map((ind) => ({
        // Création d'un tableau d'indicateurs globaux
        id: ind.idIndicateurGlobal,
        nom: ind.nom,
        unite: ind.unite,
        categorie: { nom: "Global" },
        type: "global",
      }));
      fetch("http://localhost:8989/api/indicateurSessions") // Récupération des indicateurs de session
        .then((res) => res.json())
        .then((dataSessions) => {
          const sessions = dataSessions.map((ind) => ({
            // Création d'un tableau d'indicateurs de session
            id: ind.idIndicateurSession,
            nom: ind.nom,
            unite: ind.unite,
            categorie: ind.categorie || { nom: "Session" },
            type: "session",
          }));
          allIndicateurs.value = [...globals, ...sessions]; // Fusion des deux tableaux
          filterIndicateurs(); // Filtrage des indicateurs selon le type sélectionné
        });
    });
}

function filterIndicateurs() {
  // Filtrage des indicateurs selon le type sélectionné
  indicateur.value = allIndicateurs.value.filter((ind) =>
    selectedType.value ? ind.type === selectedType.value : true
  );
}

const totalPages = computed(() =>
  // Calcul du nombre total de pages
  Math.ceil(indicateur.value.length / itemsPerPage)
);

const paginatedIndicateurs = computed(() => {
  // Pagination des indicateurs
  const start = (currentPage.value - 1) * itemsPerPage; // Calcul de l'index de départ
  return indicateur.value.slice(start, start + itemsPerPage); // Récupération des indicateurs pour la page actuelle
});

function startEdit(ind) {
  // Démarre l'édition d'un indicateur
  ind.editing = true; // Met l'indicateur en mode édition
}

function saveEdit(ind) {
  // Enregistre les modifications d'un indicateur
  const url =
    ind.type === "global"
      ? `http://localhost:8989/api/indicateurGlobals/${ind.id}`
      : `http://localhost:8989/api/indicateurSessions/${ind.id}`;

  const body = {
    nom: ind.nom,
    unite: ind.unite,
    categorie: { nom: ind.categorie.nom },
  };

  fetch(url, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  })
    .then((res) => {
      if (!res.ok) throw new Error("Erreur update");
      ind.editing = false;
      alert("Modification enregistrée !");
    })
    .catch((err) => {
      console.error(err);
      alert("Une erreur est survenue.");
    });
}

function deleteIndicateur(ind) {
  // Supprime un indicateur
  const url =
    ind.type === "global"
      ? `http://localhost:8989/api/indicateurGlobals/${ind.id}` // URL pour les indicateurs globaux
      : `http://localhost:8989/api/indicateurSessions/${ind.id}`; // URL pour les indicateurs de session

  fetch(url, { method: "DELETE" }).then((res) => {
    if (!res.ok) throw new Error();
    allIndicateurs.value = allIndicateurs.value.filter(
      // Filtre les indicateurs
      (i) => i.id !== ind.id || i.type !== ind.type
    );
    filterIndicateurs();
  });
}

function ajouterValeur(ind) {
  // Ajoute une valeur à un indicateur
  selectedIndicateur.value = ind;
  newMesure.value = { valeur: "", dateMesure: "" };
  showPopup.value = true;
}

function validerMesure() {
  // Valide la mesure ajoutée
  if (!newMesure.value.valeur || !newMesure.value.dateMesure) {
    // Vérifie si les champs sont remplis
    alert("Veuillez remplir tous les champs.");
    return;
  }

  const formattedDate = new Date(newMesure.value.dateMesure)
    .toISOString()
    .split("T")[0];

  let body;
  if (selectedIndicateur.value.type === "global") {
    body = {
      valeur: parseFloat(newMesure.value.valeur),
      dateMesure: formattedDate,
      indicateurGlobal: {
        idIndicateurGlobal: selectedIndicateur.value.id,
      },
    };
  } else {
    body = {
      valeur: parseFloat(newMesure.value.valeur),
      dateMesure: formattedDate,
      indicateurSession: {
        idIndicateurSession: selectedIndicateur.value.id,
      },
    };
  }

  console.log("Envoi des données:", JSON.stringify(body));

  fetch("http://localhost:8989/api/mesures", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  })
    .then((res) => {
      if (!res.ok) {
        return res.text().then((text) => {
          throw new Error(`Erreur API: ${text}`);
        });
      }
      return res.json();
    })
    .then(() => {
      alert("Mesure ajoutée !");
      showPopup.value = false;
    })
    .catch((err) => {
      console.error("Erreur détaillée:", err);
      alert(`Une erreur est survenue: ${err.message}`);
    });
}

onMounted(getIndicateurs);
</script>

<style scoped>
.list-container {
  border: solid;
  border-radius: 15px;
  border-color: gray;
  box-shadow: 2px 2px 2px gray;
  margin: 30px auto;
  padding: 20px;
  border-width: 2px;
  background-color: #f9f9f9;
  max-width: 1200px;
}

.search-container {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}

.dropdown {
  position: relative;
}

.dropdown button {
  padding: 10px 15px;
  font-size: 16px;
  background-color: #0e0c70;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  left: 0;
  z-index: 1000;
  display: flex;
  flex-direction: column;
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 200px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.dropdown-menu button {
  background: none;
  border: none;
  padding: 10px;
  text-align: left;
  cursor: pointer;
  width: 100%;
  color: black;
}

.dropdown-menu button:hover {
  background-color: #0e0c70;
  color: white;
}

.table-container {
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
}

th,
td {
  padding: 12px 15px;
  border: 1px solid #ddd;
  text-align: left;
}

th {
  background-color: #0e0c70;
  color: white;
  font-weight: bold;
}

tr:nth-child(even) {
  background-color: #f2f2f2;
}

.action-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.action-buttons button {
  padding: 8px 12px;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  font-size: 14px;
}

.action-buttons button:nth-child(1) {
  background-color: #ffc107;
  color: black;
}

.action-buttons button:nth-child(2) {
  background-color: #dc3545;
  color: white;
}

.action-buttons .ajouter-valeur {
  background-color: #28a745;
  color: white;
}

.action-buttons .ajouter-valeur:hover {
  background-color: #218838;
}

.pagination-controls {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 15px;
  gap: 20px;
}

.pagination-controls button {
  padding: 8px 16px;
  background-color: #0e0c70;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.pagination-controls button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.modal {
  background-color: white;
  padding: 20px 30px;
  border-radius: 8px;
  max-width: 400px;
  width: 100%;
  box-shadow: 0px 6px 10px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.modal h3 {
  margin-top: 0;
}

.modal input {
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.modal button {
  padding: 10px;
  border: none;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
}

.modal .valider {
  background-color: #28a745;
  color: white;
}

.modal .annuler {
  background-color: #dc3545;
  color: white;
}

.modal-buttons {
  display: flex;
  justify-content: space-between;
  gap: 10px;
}
</style>
