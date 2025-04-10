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
            <template v-if="editingSessionId === ses.idSession">
              <td><input v-model="editedSession.nom" /></td>
              <td><input type="date" v-model="editedSession.date" /></td>
              <td class="action-buttons">
                <button @click="saveEdit">💾 Enregistrer</button>
                <button @click="cancelEdit">Annuler</button>
              </td>
            </template>
            <template v-else>
              <td>{{ ses.nom }}</td>
              <td>{{ ses.date }}</td>
              <td class="action-buttons">
                <button @click="startEdit(ses)">Modifier</button>
                <button @click="deleteSession(ses.idSession)">Supprimer</button>
                <button @click="openAddMesureDialog(ses)">
                  Ajouter une valeur
                </button>
              </td>
            </template>
          </tr>
        </tbody>
      </table>
    </div>

    <v-dialog v-model="showAddMesure" max-width="500px">
      <v-card>
        <v-card-title
          >Ajouter une mesure à {{ selectedSession?.nom }}</v-card-title
        >
        <v-card-text>
          <v-select
            label="Indicateur de session"
            :items="indicateursSession"
            item-title="nom"
            item-value="idIndicateurSession"
            v-model="selectedIndicateurId"
            required
          />
          <v-text-field
            label="Valeur"
            v-model="newMesure.valeur"
            type="number"
          />
          <v-text-field
            label="Date"
            v-model="newMesure.dateMesure"
            type="date"
          />
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn color="primary" @click="submitMesure">Valider</v-btn>
          <v-btn color="grey" @click="showAddMesure = false">Annuler</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from "vue";

const searchTerm = ref("");
const sessions = ref([]);
const allSessions = ref([]);
const editingSessionId = ref(null);
const editedSession = ref({});

const showAddMesure = ref(false);
const selectedSession = ref(null);
const selectedIndicateurId = ref(null);
const newMesure = ref({ valeur: "", dateMesure: "" });
const indicateursSession = ref([]);

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
  if (!window.confirm("Êtes-vous sûr de vouloir supprimer cette session ?"))
    return;

  fetch(`${apiBaseUrl}/${id}`, { method: "DELETE" })
    .then((response) => {
      if (!response.ok) throw new Error("Erreur lors de la suppression");
      fetchSessions();
    })
    .catch((error) => {
      console.error("Erreur lors de la suppression:", error);
    });
};

const startEdit = (session) => {
  editingSessionId.value = session.idSession;
  editedSession.value = { ...session };
};

const cancelEdit = () => {
  editingSessionId.value = null;
  editedSession.value = {};
};

const saveEdit = () => {
  const id = editingSessionId.value;
  fetch(`${apiBaseUrl}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(editedSession.value),
  })
    .then((res) => {
      if (!res.ok) throw new Error("Erreur lors de la mise à jour");
      alert("Session mise à jour !");
      cancelEdit();
      fetchSessions();
    })
    .catch((err) => {
      console.error(err);
      alert("Erreur lors de la mise à jour.");
    });
};

const openAddMesureDialog = (session) => {
  selectedSession.value = session;
  fetch("http://localhost:8989/api/indicateurSessions")
    .then((res) => res.json())
    .then((data) => {
      indicateursSession.value = data;
      showAddMesure.value = true;
    });
};

const submitMesure = () => {
  if (
    !selectedIndicateurId.value ||
    !newMesure.value.valeur ||
    !newMesure.value.dateMesure
  ) {
    alert("Tous les champs sont obligatoires.");
    return;
  }

  const body = {
    valeur: parseFloat(newMesure.value.valeur),
    dateMesure: newMesure.value.dateMesure,
    indicateurSession: { idIndicateurSession: selectedIndicateurId.value },
    session: { idSession: selectedSession.value.idSession },
  };

  fetch("http://localhost:8989/api/mesures", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  })
    .then((res) => {
      if (!res.ok) throw new Error("Erreur API");
      alert("Mesure enregistrée !");
      showAddMesure.value = false;
    })
    .catch((err) => {
      console.error(err);
      alert("Une erreur est survenue.");
    });
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
  background-color: #0e0c70;
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
  background-color: #0e0c70;
  color: white;
  cursor: pointer;
  transition: background 0.3s;
}
.action-buttons button:hover {
  background-color: darkblue;
}
</style>
