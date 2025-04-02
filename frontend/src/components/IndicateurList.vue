<template>
  <div class="list-container">
    <div class="search-container">
      <input
        v-model="searchTerm"
        type="text"
        placeholder="Rechercher un indicateur..."
      />

      <div class="dropdown">
        <button @click="toggleDropdown">Filtrer Catégorie ▼</button>
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
          <tr v-for="ind in indicateur" :key="ind.id">
            <td v-if="!ind.editing">{{ ind.nom }}</td>
            <td v-else><input v-model="ind.nom" /></td>

            <td v-if="!ind.editing">{{ ind.unite }}</td>
            <td v-else><input v-model="ind.unite" /></td>

            <td v-if="!ind.editing">{{ ind.categorie.nom }}</td>
            <td v-else><input v-model="ind.categorie.nom" /></td>

            <td>{{ ind.type === "global" ? "Global" : "Session" }}</td>

            <td class="action-buttons">
              <button v-if="!ind.editing" @click="startEdit(ind)">
                Modifier
              </button>
              <button v-else @click="updateIndicateur(ind)">Valider</button>
              <button v-if="ind.editing" @click="cancelEdit(ind)">
                Annuler
              </button>
              <button @click="() => deleteIndicateur(ind)">Supprimer</button>
              <button
                v-if="ind.type === 'session'"
                @click="openMesureModal(ind)"
              >
                ➕ Ajouter valeur
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal pour ajouter une mesure -->
    <div v-if="showModal" class="modal-overlay">
      <div class="modal">
        <h3>Ajouter une valeur à {{ currentIndicateur.nom }}</h3>
        <input
          type="number"
          v-model="nouvelleMesure.valeur"
          placeholder="Valeur"
        />
        <input type="date" v-model="nouvelleMesure.dateMesure" />
        <div class="modal-buttons">
          <button @click="ajouterMesure">Enregistrer</button>
          <button @click="closeModal">Annuler</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from "vue";

const searchTerm = ref("");
const indicateur = ref([]);
const allIndicateurs = ref([]);
const showDropdown = ref(false);
const selectedType = ref("");
const showModal = ref(false);
const currentIndicateur = ref(null);
const nouvelleMesure = ref({ valeur: "", dateMesure: "" });

function startEdit(ind) {
  ind.editing = true;
}

function cancelEdit(ind) {
  ind.editing = false;
  getIndicateurs();
}

function getIndicateurs() {
  const fetchOptions = { method: "GET" };

  fetch("http://localhost:8989/api/indicateurGlobals", fetchOptions)
    .then((response) => response.json())
    .then((dataGlobals) => {
      const globals = dataGlobals.map((ind) => ({
        id: ind.idIndicateurGlobal,
        nom: ind.nom,
        unite: ind.unite,
        categorie: { nom: "Global" },
        type: "global",
      }));

      fetch("http://localhost:8989/api/indicateurSessions", fetchOptions)
        .then((response) => response.json())
        .then((dataSessions) => {
          const sessions = dataSessions.map((ind) => ({
            id: ind.idIndicateurSession,
            nom: ind.nom,
            unite: ind.unite,
            categorie: ind.categorie || { nom: "Session" },
            type: "session",
          }));

          allIndicateurs.value = [...globals, ...sessions];
          filterIndicateurs();
        });
    })
    .catch((error) => console.error("Erreur récupération indicateurs:", error));
}

function filterIndicateurs() {
  indicateur.value = allIndicateurs.value.filter((ind) => {
    const matchesSearch = searchTerm.value
      ? ind.nom.toLowerCase().includes(searchTerm.value.toLowerCase())
      : true;
    const matchesType = selectedType.value
      ? ind.type === selectedType.value
      : true;
    return matchesSearch && matchesType;
  });
}

function toggleDropdown() {
  showDropdown.value = !showDropdown.value;
}

function selectType(type) {
  selectedType.value = type;
  showDropdown.value = false;
  filterIndicateurs();
}

function updateIndicateur(ind) {
  const updatedInd = {
    nom: ind.nom,
    unite: ind.unite,
    date: new Date().toISOString().split("T")[0],
    utilisateur: { idPersonne: 1 },
    categorie:
      ind.type === "session"
        ? { idCategorie: ind.categorie?.idCategorie || 1 }
        : null,
  };

  const url =
    ind.type === "global"
      ? `http://localhost:8989/api/indicateurGlobals/${ind.id}`
      : `http://localhost:8989/api/indicateurSessions/${ind.id}`;

  const fetchOptions = {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(updatedInd),
  };

  fetch(url, fetchOptions)
    .then((response) => {
      if (!response.ok) throw new Error("Erreur mise à jour");
      return response.json();
    })
    .then(() => {
      ind.editing = false;
      getIndicateurs();
      alert("Indicateur modifié !");
    })
    .catch((error) => console.error("Erreur mise à jour:", error));
}

function deleteIndicateur(ind) {
  if (!window.confirm("Êtes-vous sûr de vouloir supprimer cet indicateur ?")) {
    return;
  }
  const url =
    ind.type === "global"
      ? `http://localhost:8989/api/indicateurGlobals/${ind.id}`
      : `http://localhost:8989/api/indicateurSessions/${ind.id}`;

  fetch(url, { method: "DELETE" })
    .then((response) => {
      if (!response.ok) throw new Error("Erreur suppression");
      allIndicateurs.value = allIndicateurs.value.filter(
        (i) => i.id !== ind.id
      );
      filterIndicateurs();
      alert("Indicateur supprimé !");
    })
    .catch((error) => console.error("Erreur suppression:", error));
}

function openMesureModal(ind) {
  currentIndicateur.value = ind;
  nouvelleMesure.value = { valeur: "", dateMesure: "" };
  showModal.value = true;
}

function closeModal() {
  showModal.value = false;
  currentIndicateur.value = null;
}

function ajouterMesure() {
  if (!nouvelleMesure.value.valeur || !nouvelleMesure.value.dateMesure) {
    alert("Remplis bien tous les champs de la mesure.");
    return;
  }
  const mesure = {
    valeur: parseFloat(nouvelleMesure.value.valeur),
    dateMesure: nouvelleMesure.value.dateMesure,
    indicateurSession: { idIndicateurSession: currentIndicateur.value.id },
  };

  fetch("http://localhost:8989/api/mesures", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(mesure),
  })
    .then((res) => {
      if (!res.ok) throw new Error("Erreur lors de l'ajout de la mesure");
      alert("Mesure ajoutée !");
      closeModal();
    })
    .catch((err) => console.error("Erreur mesure:", err));
}

watch(searchTerm, filterIndicateurs);
onMounted(getIndicateurs);
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}
.modal {
  background: white;
  padding: 20px;
  border-radius: 10px;
  min-width: 300px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.modal-buttons {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}
.list-container {
  margin: 40px auto;
  max-width: 1000px;
  padding: 30px;
  background-color: #f9f9f9;
  border-radius: 12px;
  box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
}

.search-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  margin-bottom: 20px;
  gap: 10px;
}

.search-container input {
  flex: 1;
  min-width: 250px;
  padding: 10px;
  border-radius: 6px;
  border: 1px solid #ccc;
}

.dropdown button {
  background-color: #0e0c70;
  color: white;
  border: none;
  padding: 10px 14px;
  border-radius: 6px;
  cursor: pointer;
}

.dropdown-menu {
  margin-top: 5px;
  background: white;
  border: 1px solid #ddd;
  border-radius: 6px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  position: absolute;
  z-index: 10;
  width: 200px;
}

.dropdown-menu button {
  padding: 10px;
  width: 100%;
  border: none;
  background: none;
  text-align: left;
  cursor: pointer;
}

.table-container table {
  width: 100%;
  border-collapse: collapse;
  background: white;
}

.table-container th,
.table-container td {
  padding: 14px;
  text-align: center;
  border-bottom: 1px solid #ddd;
}

.table-container th {
  background-color: #0e0c70;
  color: white;
}

.table-container tr:nth-child(even) {
  background-color: #f3f7ff;
}

.action-buttons {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.action-buttons button {
  padding: 6px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
}

.action-buttons button:first-child {
  background-color: #ffc107;
  color: black;
}

.action-buttons button:nth-child(2) {
  background-color: #28a745;
  color: white;
}

.action-buttons button:nth-child(3) {
  background-color: #6c757d;
  color: white;
}

.action-buttons button:last-child {
  background-color: #dc3545;
  color: white;
}

.action-buttons button:hover {
  opacity: 0.85;
}
</style>
