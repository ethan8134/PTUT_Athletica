<template>
  <div class="list-container">
    <div class="search-container">
      <input
        type="text"
        v-model="searchTerm"
        placeholder="Rechercher une session..."
      />
    </div>

    <div class="table-container">
      <table>
        <thead>
        <tr>
          <th>Nom</th>
          <th>Date</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="ses in sessions" :key="ses.idSession">
        <td>{{ ses.nom }}</td>
          <td>{{ ses.date }}</td>
          <td class="action-buttons">
            <button @click="redirectToEdit(ses.idSession)">Modifier</button>
            <button @click="deleteSession(ses.idSession)">Supprimer</button>
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
const sessions = ref([]);
const allSessions = ref([]);
const sessionToEdit = ref(null);


// URL de l'API pour les sessions
const apiBaseUrl = "http://localhost:8989/api/sessions";

const fetchSessions = () => {
  fetch(apiBaseUrl)
    .then((response) => response.json())
    .then((data) => {
      allSessions.value = data;
      filterSessions();
    })
    .catch((error) => {
      console.error("Erreur lors de la récupération des sessions:", error);
    });
};

const filterSessions = () => {
  sessions.value = allSessions.value.filter((ses) =>
    searchTerm.value
      ? ses.nom.toLowerCase().includes(searchTerm.value.toLowerCase())
      : true
  );
};

watch(searchTerm, filterSessions);
onMounted(fetchSessions);

const deleteSession = (id) => {
  if (!window.confirm("Êtes-vous sûr de vouloir supprimer cette session ?")) {
    return;
  }
  fetch(`${apiBaseUrl}/${id}`, { method: "DELETE" })
    .then((response) => {
      if (!response.ok) throw new Error("Erreur lors de la suppression");
      return Promise.resolve();
    })
    .then(() => fetchSessions())
    .catch((error) => {
      console.error("Erreur lors de la suppression:", error);
    });
};

const redirectToEdit = (id) => {
  const session = sessions.value.find(s => s.id === id);
  if (session) {
    sessionToEdit.value = { ...session }; // on copie pour éviter la mutation directe
    showForm.value = true;
  }
};

</script>

<style scoped>
.list-container {
  border: solid 2px gray;
  border-radius: 15px;
  box-shadow: 2px 2px 2px gray;
  margin-top: 15px;
  padding: 5px;
  background-color: lightgray;
}
.search-container {
  display: flex;
  justify-content: center;
  align-items: center;
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
}
.table-container {
  overflow-x: auto;
}
table {
  width: 100%;
  background: lightgray;
  border-collapse: collapse;
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
.action-buttons {
  display: flex;
  gap: 5px;
}
.action-buttons button {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid black;
  border-radius: 5px;
  background-color: #007fff;
  color: white;
  cursor: pointer;
  transition: background 0.3s;
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
  padding: 12px;
  font-size: 18px;
  background-color: #007fff;
  border-radius: 5px;
  border: none;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.3s;
}
.button-container button:hover {
  background-color: darkblue;
}
</style>
