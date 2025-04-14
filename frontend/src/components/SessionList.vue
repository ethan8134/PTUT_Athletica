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
              <button @click="saveEdit">📎 Enregistrer</button>
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

          <v-btn color="primary" class="mt-4" @click="prepareAddMesure">Ajouter une mesure</v-btn>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="showDetails = false">Fermer</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

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

          <div class="mt-3 text-caption text-grey">
            🗓️ La mesure sera enregistrée pour la date :
            <strong>{{ formatDate(newMesure.dateMesure) }}</strong>
          </div>
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
import { ref, watch, onMounted } from 'vue';

const searchTerm = ref('');
const sessions = ref([]);
const allSessions = ref([]);
const editingSessionId = ref(null);
const editedSession = ref({});

const showDetails = ref(false);
const selectedSession = ref(null);
const sessionMesures = ref([]);
const indicateurs = ref([]);
const showAddMesureDialog = ref(false);
const newMesure = ref({
  indicateurId: null,
  valeur: '',
  dateMesure: ''
});

const apiBaseUrl = 'http://localhost:8989/api/sessions';

const fetchSessions = () => {
  fetch(apiBaseUrl)
    .then(res => res.json())
    .then(data => {
      allSessions.value = data;
      filterSessions();
    });
};

const filterSessions = () => {
  sessions.value = allSessions.value.filter(ses =>
    searchTerm.value ? ses.nom.toLowerCase().includes(searchTerm.value.toLowerCase()) : true
  );
};

watch(searchTerm, filterSessions);
onMounted(fetchSessions);

const deleteSession = (id) => {
  if (!confirm("Supprimer cette session ?")) return;
  fetch(`${apiBaseUrl}/${id}`, { method: 'DELETE' })
    .then(res => res.ok && fetchSessions());
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
  fetch(`${apiBaseUrl}/${editingSessionId.value}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(editedSession.value)
  })
    .then(res => res.ok && fetchSessions());
  cancelEdit();
};

const viewSessionDetails = async (sessionId) => {
  const res = await fetch(`${apiBaseUrl}/${sessionId}`);
  selectedSession.value = await res.json();

  const indicateursRes = await fetch('http://localhost:8989/api/indicateurSessions');
  indicateurs.value = await indicateursRes.json();

  await loadSessionMesures(sessionId);
  showDetails.value = true;
};

const loadSessionMesures = async (sessionId) => {
  const mesuresRes = await fetch('http://localhost:8989/api/mesures');
  const allMesures = await mesuresRes.json();

  sessionMesures.value = allMesures.filter(m => m.session?.idSession == sessionId).map(m => {
    const indicateur = indicateurs.value.find(i => i.idIndicateurSession === m.indicateurSession?.idIndicateurSession);
    return {
      id: m.id,
      indicateurNom: indicateur?.nom || 'Inconnu',
      valeur: m.valeur,
      unite: indicateur?.unite || '',
      dateMesure: m.dateMesure,
      indicateurId: m.indicateurSession?.idIndicateurSession
    };
  });
};

const prepareAddMesure = () => {
  if (selectedSession.value?.dateSession) {
    newMesure.value.dateMesure = selectedSession.value.dateSession;
  }
  showAddMesureDialog.value = true;
};

const addMesure = async () => {
  if (!newMesure.value.indicateurId || !newMesure.value.valeur) {
    alert('Veuillez remplir tous les champs.');
    return;
  }

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

  const res = await fetch('http://localhost:8989/api/mesures', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body)
  });

  if (res.ok) {
    await loadSessionMesures(selectedSession.value.idSession);
    showAddMesureDialog.value = false;
    newMesure.value = { indicateurId: null, valeur: '', dateMesure: '' };
    alert('Mesure ajoutée avec succès!');
  }
};

const deleteMesure = async (id) => {
  if (!confirm('Supprimer cette mesure ?')) return;
  const res = await fetch(`http://localhost:8989/api/mesures/${id}`, { method: 'DELETE' });
  if (res.ok) await loadSessionMesures(selectedSession.value.idSession);
};

const formatDate = (dateStr) => {
  if (!dateStr) return 'Non spécifiée';
  const date = new Date(dateStr);
  return date.toLocaleDateString('fr-FR', { day: '2-digit', month: '2-digit', year: 'numeric' });
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
.text-caption {
  font-size: 14px;
}
.text-grey {
  color: #666;
}
.text-caption {
  font-size: 14px;
}
.text-grey {
  color: #666;
}

</style>
