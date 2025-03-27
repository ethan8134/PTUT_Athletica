<template>
  <div class="list-container">
    <div class="search-container">
      <input
        type="text"
        v-model="searchTerm"
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

          <td>{{ ind.type === 'global' ? 'Global' : 'Session' }}</td>

          <td class="action-buttons">
            <button v-if="!ind.editing" @click="startEdit(ind)">Modifier</button>
            <button v-else @click="updateIndicateur(ind)">Valider</button>
            <button v-if="ind.editing" @click="cancelEdit(ind)">Annuler</button>
            <button @click="() => deleteIndicateur(ind)">Supprimer</button>
          </td>
        </tr>
        </tbody>
      </table>
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
const originalIndicateur = ref({});

function startEdit(ind) {
  ind.editing = true;
}

function cancelEdit(ind) {
  ind.editing = false;
  getIndicateurs(); // Pour remettre à jour après annulation
}
function getIndicateurs() {
  const fetchOptions = { method: "GET" };

  // Fetch indicateurs globaux
  fetch("http://localhost:8989/api/indicateurGlobals", fetchOptions)
    .then(response => response.json())
    .then(dataGlobals => {
      const globals = dataGlobals.map(ind => ({
        id: ind.idIndicateurGlobal,
        nom: ind.nom,
        unite: ind.unite,
        categorie: { nom: "Global" }, // Pas de catégorie précise côté global, tu peux adapter
        type: "global"
      }));

      // Fetch indicateurs sessions
      fetch("http://localhost:8989/api/indicateurSessions", fetchOptions)
        .then(response => response.json())
        .then(dataSessions => {
          const sessions = dataSessions.map(ind => ({
            id: ind.idIndicateurSession,
            nom: ind.nom,
            unite: ind.unite,
            categorie: ind.categorie || { nom: "Session" }, // Si pas de catégorie côté backend, adapter ici
            type: "session"
          }));

          // Fusionner les deux listes
          allIndicateurs.value = [...globals, ...sessions];
          filterIndicateurs();
        });
    })
    .catch(error => console.error("Erreur récupération indicateurs :", error));
}


function filterIndicateurs() {
  indicateur.value = allIndicateurs.value.filter(ind => {
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
    date: new Date().toISOString().split('T')[0],
    utilisateur: { idPersonne: 1 },
    categorie: ind.type === "session" ? { idCategorie: ind.categorie?.idCategorie || 1 } : null
  };

  const url = ind.type === "global"
    ? `http://localhost:8989/api/indicateurGlobals/${ind.id}`
    : `http://localhost:8989/api/indicateurSessions/${ind.id}`;

  const fetchOptions = {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(updatedInd)
  };

  fetch(url, fetchOptions)
    .then((response) => {
      if (!response.ok) throw new Error("Erreur mise à jour");
      return response.json();
    })
    .then(() => {
      ind.editing = false;
      getIndicateurs(); // Recharge la liste
      alert("Indicateur modifié !");
    })
    .catch((error) => console.error("Erreur mise à jour :", error));
}

function deleteIndicateur(ind) {
  if (!window.confirm("Êtes-vous sûr de vouloir supprimer cet indicateur ?")) {
    return;
  }
  const url =
    ind.type === "global"
      ? `http://localhost:8989/api/indicateurGlobals/${ind.id}`
      : `http://localhost:8989/api/indicateurSessions/${ind.id}`;

  const fetchOptions = { method: "DELETE" };
  fetch(url, fetchOptions)
    .then((response) => {
      if (!response.ok) throw new Error("Erreur suppression");
      // Supprimer localement
      allIndicateurs.value = allIndicateurs.value.filter((i) => i.id !== ind.id);
      filterIndicateurs();
      alert("Indicateur supprimé !");
    })
    .catch((error) => console.error("Erreur suppression :", error));
}

watch(searchTerm, filterIndicateurs);
onMounted(getIndicateurs);

</script>


<style scoped>
body {
  font-family: Arial, sans-serif;
  background-color: #f4f4f4;
  margin: 0;
  padding: 0;
}

.container {
  width: 90%;
  max-width: 1200px;
  margin: 20px auto;
  padding: 20px;
  background: white;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
}

button {
  background-color: #007fff;
  color: white;
  border: none;
  padding: 10px 15px;
  cursor: pointer;
  border-radius: 5px;
  transition: background 0.3s ease-in-out;
}

table {
  width: 100%;

  background: lightgray;
}

.table-container td input {
  padding: 5px;
  border: 1px solid black;
  border-radius: 4px;
  font-size: 16px;
}

th,
td {
  border: 1px solid #ddd;
  padding: 10px;
  text-align: left;
  color: black;
  vertical-align: middle;
  height: 60px;
}

th {
  background-color: #007fff;
  color: white;
}

tr:nth-child(even) {
  background: #f9f9f9;
}

img {
  border-radius: 5px;
  max-width: 50px;
}

.action-buttons {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 5px;
  height: 100px;
}

.action-buttons button {
  flex: 1;
  padding: 8px 12px;
  margin: auto 0px;
  height: 40px;
  width: 60px;
  text-align: center;
  border: solid;
  border-color: black;
  border-width: 1px;
}

.action-buttons button:hover {
  background-color: darkblue;
}

.button-container {
  margin-top: 15px;
  display: flex;
  justify-content: center;
}

.button-container button {
  width: 100%;
  max-width: 100%;
  padding: 12px;
  font-size: 18px;
  background-color: #007fff;
  border-radius: 5px;
  border: none;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.3s ease-in-out;
}

.button-container button:hover {
  background-color: darkblue;
}

.search-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  margin: 20px auto;
  width: 100%;
}

.search-container input {
  flex: 1;
  max-width: 400px;
  padding: 10px;
  border: 1px solid black;
  border-radius: 5px;
  font-size: 16px;
  transition: border 0.3s ease-in-out;
}

.search-container input:focus {
  outline: none;
}

.search-container button {
  padding: 10px 15px;
  font-size: 16px;
  background-color: #007fff;
  border: none;
  border-radius: 5px;
  color: white;
  cursor: pointer;
  transition: background 0.3s ease-in-out;
}

.search-container button:hover {
  background-color: darkblue;
}

.dropdown {
  position: relative;
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  left: 0;
  background: white;
  border: 1px solid #ddd;
  border-radius: 5px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
  z-index: 10;
  display: flex;
  flex-direction: column;
  width: 200px;
}

.dropdown-menu button {
  background: white;
  color: black;
  padding: 10px;
  border: none;
  text-align: left;
  width: 100%;
  cursor: pointer;
  transition: background 0.3s ease-in-out;
}

.dropdown-menu button:hover {
  background: #007fff;
  color: white;
}

.list-container {
  border: solid;
  border-radius: 15px;
  border-color: gray;
  box-shadow: 2px 2px 2px gray;
  margin-top: 15px;
  padding: 5px;
  border-width: 2px;
  background-color: lightgray;
}
</style>
