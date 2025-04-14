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
            <td>{{ formatDate(ses.dateSession) }}</td>
            <td class="action-buttons">
              <button @click="startEdit(ses)">Modifier</button>
              <button @click="deleteSession(ses.idSession)">Supprimer</button>
              <button class="btn-details" @click="viewSessionDetails(ses.idSession)">
                <v-icon small>mdi-information-outline</v-icon> Voir détails
              </button>
            </td>
          </template>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Dialogue des détails de session -->
    <v-dialog v-model="showDetails" max-width="800px">
      <v-card>
        <v-card-title>
          Détails de la session : {{ selectedSession?.nom || '' }}
        </v-card-title>
        <v-card-text v-if="selectedSession">
          <p><strong>Date :</strong> {{ formatDate(selectedSession.dateSession) }}</p>
          <v-divider class="my-4"></v-divider>

          <h3 class="mb-3">Mesures associées</h3>
          <v-simple-table v-if="sessionMesures.length > 0">
            <template v-slot:default>
              <thead>
              <tr>
                <th>Indicateur</th>
                <th>Valeur</th>
                <th>Date</th>
                <th>Actions</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="mesure in sessionMesures" :key="mesure.id">
                <td>{{ mesure.indicateurNom }}</td>
                <td>{{ mesure.valeur }} {{ mesure.unite }}</td>
                <td>{{ formatDate(mesure.dateMesure) }}</td>
                <td>
                  <v-btn icon small color="error" @click="deleteMesure(mesure.id)">
                    <v-icon>mdi-delete</v-icon>
                  </v-btn>
                </td>
              </tr>
              </tbody>
            </template>
          </v-simple-table>
          <div v-else class="text-center my-4">
            <p>Aucune mesure associée à cette session</p>
          </div>

          <v-btn color="primary" class="mt-4" @click="showAddMesureDialog = true">
            Ajouter une mesure
          </v-btn>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="showDetails = false">
            Fermer
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Dialogue pour ajouter une mesure -->
    <v-dialog v-model="showAddMesureDialog" max-width="500px">
      <v-card>
        <v-card-title>Ajouter une mesure</v-card-title>
        <v-card-text>
          <v-select
            v-model="newMesure.indicateurId"
            :items="indicateurs"
            item-title="nom"
            item-value="idIndicateurSession"
            label="Indicateur"
            required
          ></v-select>

          <v-text-field
            v-model="newMesure.valeur"
            label="Valeur"
            type="number"
            required
          ></v-text-field>

          <v-text-field
            v-model="newMesure.dateMesure"
            label="Date"
            type="date"
            required
          ></v-text-field>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="grey" text @click="showAddMesureDialog = false">Annuler</v-btn>
          <v-btn color="primary" text @click="addMesure">Enregistrer</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const searchTerm = ref("");
const sessions = ref([]);
const allSessions = ref([]);
const editingSessionId = ref(null);
const editedSession = ref({});

// Variables pour le dialogue de détails
const showDetails = ref(false);
const selectedSession = ref(null);
const sessionMesures = ref([]);
const indicateurs = ref([]);
const showAddMesureDialog = ref(false);
const newMesure = ref({
  indicateurId: null,
  valeur: '',
  dateMesure: new Date().toISOString().split('T')[0]
});

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
  // Filtre les sessions en fonction du terme de recherche
  sessions.value = allSessions.value.filter(
    (ses) =>
      searchTerm.value
        ? ses.nom.toLowerCase().includes(searchTerm.value.toLowerCase())
        : true
  );
};

watch(searchTerm, filterSessions);
onMounted(fetchSessions);

const deleteSession = (id) => {
  // Supprime une session
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
  // Démarre l'édition d'une session
  editingSessionId.value = session.idSession;
  editedSession.value = { ...session };
};

const cancelEdit = () => {
  // Annule l'édition
  editingSessionId.value = null;
  editedSession.value = {};
};

const saveEdit = () => {
  // Enregistre les modifications
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

const viewSessionDetails = async (sessionId) => {
  try {
    // Charger les détails de la session
    const sessionRes = await fetch(`http://localhost:8989/api/sessions/${sessionId}`);
    selectedSession.value = await sessionRes.json();

    // Charger les indicateurs disponibles
    const indicateursRes = await fetch('http://localhost:8989/api/indicateurSessions');
    indicateurs.value = await indicateursRes.json();

    // Charger les mesures liées à cette session
    await loadSessionMesures(sessionId);

    // Afficher la boîte de dialogue
    showDetails.value = true;
  } catch (error) {
    console.error('Erreur lors du chargement des données:', error);
    alert('Erreur lors du chargement des détails de la session');
  }
};

const loadSessionMesures = async (sessionId) => {
  try {
    const mesuresRes = await fetch('http://localhost:8989/api/mesures');
    const allMesures = await mesuresRes.json();

    sessionMesures.value = allMesures
      .filter(m => m.session && m.session.idSession == sessionId)
      .map(m => {
        const indicateurId = m.indicateurSession?.idIndicateurSession;
        const indicateurComplet = indicateurs.value.find(i => i.idIndicateurSession === indicateurId);
        return {
          id: m.id,
          indicateurNom: indicateurComplet?.nom || 'Inconnu',
          valeur: m.valeur,
          unite: indicateurComplet?.unite || '',
          dateMesure: m.dateMesure,
          indicateurId: indicateurId
        };
      });
  } catch (error) {
    console.error('Erreur lors du chargement des mesures:', error);
    sessionMesures.value = [];
  }
};


const addMesure = async () => {
  if (!newMesure.value.indicateurId || !newMesure.value.valeur || !newMesure.value.dateMesure) {
    alert('Veuillez remplir tous les champs.');
    return;
  }

  try {
    const body = {
      valeur: parseFloat(newMesure.value.valeur),
      dateMesure: newMesure.value.dateMesure,
      indicateurSession: {
        idIndicateurSession: newMesure.value.indicateurId
      },
      session: {
        idSession: selectedSession.value.idSession
      }
    };

    const response = await fetch('http://localhost:8989/api/mesures', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(body)
    });

    if (!response.ok) {
      throw new Error('Erreur lors de l\'ajout de la mesure');
    }

    await loadSessionMesures(selectedSession.value.idSession);
    showAddMesureDialog.value = false;
    newMesure.value = {
      indicateurId: null,
      valeur: '',
      dateMesure: new Date().toISOString().split('T')[0]
    };
    alert('Mesure ajoutée avec succès!');
  } catch (error) {
    console.error('Erreur:', error);
    alert('Une erreur est survenue lors de l\'ajout de la mesure.');
  }
};

const deleteMesure = async (mesureId) => {
  if (!confirm('Êtes-vous sûr de vouloir supprimer cette mesure?')) {
    return;
  }

  try {
    const response = await fetch(`http://localhost:8989/api/mesures/${mesureId}`, {
      method: 'DELETE'
    });

    if (!response.ok) {
      throw new Error('Erreur lors de la suppression');
    }

    await loadSessionMesures(selectedSession.value.idSession);
    alert('Mesure supprimée avec succès!');
  } catch (error) {
    console.error('Erreur:', error);
    alert('Une erreur est survenue lors de la suppression.');
  }
};

const formatDate = (dateString) => {
  if (!dateString) return 'Non spécifiée';

  const date = new Date(dateString);
  return date.toLocaleDateString('fr-FR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric'
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
.btn-details {
  background-color: #4CAF50 !important; /* Vert pour le bouton de détails */
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}
.btn-details:hover {
  background-color: #3e8e41 !important;
}

.v-card-title {
  font-weight: bold;
  font-size: 1.3rem;
  background-color: #f5f5f5;
  border-bottom: 1px solid #ddd;
  padding-bottom: 12px;
}

.v-simple-table thead th {
  background-color: #0e0c70;
  color: white;
  font-weight: bold;
  font-size: 15px;
  padding: 12px;
  border: none;
  text-align: left;
}

.v-simple-table tbody td {
  padding: 14px 16px;
  font-size: 14px;
  background-color: #fefefe;
  border-bottom: 1px solid #ddd;
}

.v-simple-table tbody tr:nth-child(even) td {
  background-color: #f9f9f9;
}

.v-btn[icon] {
  background-color: #b00020 !important;
  color: white !important;
  border-radius: 50% !important;
  box-shadow: 0 2px 5px rgba(0,0,0,0.15);
  transition: transform 0.2s ease;
}
.v-btn[icon]:hover {
  transform: scale(1.1);
  background-color: #9b001b !important;
}

.v-btn.primary {
  margin-left: auto;
  margin-top: 16px;
}

.v-dialog .v-card {
  border-radius: 16px;
  overflow: hidden;
}

.text-center.my-4 p {
  color: #777;
  font-style: italic;
}

</style>
