<template>
  <div class="session-details">
    <v-container>
      <v-btn color="primary" class="mb-4" @click="goBack">
        <v-icon>mdi-arrow-left</v-icon> Retour aux sessions
      </v-btn>

      <v-card class="mb-6" v-if="session">
        <v-card-title class="text-h5 d-flex align-center">
          <v-icon class="mr-2">mdi-calendar-text</v-icon>
          Session: {{ session.nom }}
        </v-card-title>

        <v-card-text>
          <p><strong>Date:</strong> {{ formatDate(session.dateSession) }}</p>
          <div class="d-flex align-center mt-2">
            <v-chip color="primary" class="mr-2">
              <v-icon left>mdi-chart-line</v-icon>
              {{ mesures.length }} mesures
            </v-chip>
          </div>
        </v-card-text>
      </v-card>

      <h3 class="text-h5 mb-4">Indicateurs et mesures associés</h3>

      <div v-if="loading" class="d-flex justify-center my-6">
        <v-progress-circular indeterminate color="primary"></v-progress-circular>
      </div>

      <div v-else-if="mesures.length === 0" class="text-center my-6">
        <v-icon size="64" color="grey lighten-1">mdi-chart-timeline-variant</v-icon>
        <p class="text-body-1 mt-2">Aucune mesure enregistrée pour cette session</p>
        <v-btn color="primary" class="mt-4" @click="showAddMesureDialog">
          Ajouter une mesure
        </v-btn>
      </div>

      <v-row v-else>
        <v-col cols="12">
          <v-card>
            <v-data-table
              :headers="headers"
              :items="mesures"
              :items-per-page="10"
              class="elevation-1"
            >
              <template v-slot:item.dateMesure="{ item }">
                {{ formatDate(item.dateMesure) }}
              </template>

              <template v-slot:item.actions="{ item }">
                <v-btn icon small color="error" @click="deleteMesure(item.id)">
                  <v-icon>mdi-delete</v-icon>
                </v-btn>
              </template>
            </v-data-table>
          </v-card>
        </v-col>
      </v-row>

      <v-btn
        fab
        color="primary"
        bottom
        right
        fixed
        @click="showAddMesureDialog"
        class="mb-10"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </v-container>

    <!-- Dialogue d'ajout de mesure -->
    <v-dialog v-model="addMesureDialog" max-width="500px">
      <v-card>
        <v-card-title class="text-h5">Ajouter une nouvelle mesure</v-card-title>

        <v-card-text>
          <v-select
            v-model="newMesure.indicateurId"
            :items="indicateurs"
            item-text="nom"
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
            :value="today"
          ></v-text-field>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="addMesureDialog = false">Annuler</v-btn>
          <v-btn color="blue darken-1" text @click="addMesure">Enregistrer</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter();
const route = useRoute();
const sessionId = route.params.id; // Changé de route.query.id à route.params.id

const session = ref(null);
const mesures = ref([]);
const indicateurs = ref([]);
const loading = ref(true);
const addMesureDialog = ref(false);
const newMesure = ref({
  indicateurId: null,
  valeur: '',
  dateMesure: new Date().toISOString().split('T')[0]
});

const headers = [
  { text: 'Indicateur', value: 'indicateurNom', align: 'start' },
  { text: 'Valeur', value: 'valeur' },
  { text: 'Unité', value: 'unite' },
  { text: 'Date', value: 'dateMesure' },
  { text: 'Actions', value: 'actions', sortable: false }
];

const today = computed(() => {
  return new Date().toISOString().split('T')[0];
});

onMounted(async () => {
  try {
    // Charger les détails de la session
    const sessionRes = await fetch(`http://localhost:8989/api/sessions/${sessionId}`);
    session.value = await sessionRes.json();

    // Charger les indicateurs disponibles
    const indicateursRes = await fetch('http://localhost:8989/api/indicateurSessions');
    indicateurs.value = await indicateursRes.json();

    // Charger les mesures liées à cette session
    await loadMesures();
  } catch (error) {
    console.error('Erreur lors du chargement des données:', error);
  } finally {
    loading.value = false;
  }
});

const loadMesures = async () => {
  try {
    // Récupérer toutes les mesures
    const mesuresRes = await fetch('http://localhost:8989/api/mesures');
    const allMesures = await mesuresRes.json();

    // Filtrer les mesures liées à cette session
    mesures.value = allMesures
      .filter(m => m.session && m.session.idSession == sessionId)
      .map(m => {
        // Enrichir avec les informations d'indicateur
        const indicateur = m.indicateurSession || {};
        return {
          id: m.id,
          indicateurNom: indicateur.nom || 'Inconnu',
          valeur: m.valeur,
          unite: indicateur.unite || '',
          dateMesure: m.dateMesure,
          indicateurId: indicateur.idIndicateurSession
        };
      });
  } catch (error) {
    console.error('Erreur lors du chargement des mesures:', error);
  }
};

const showAddMesureDialog = () => {
  newMesure.value = {
    indicateurId: null,
    valeur: '',
    dateMesure: new Date().toISOString().split('T')[0]
  };
  addMesureDialog.value = true;
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
        idSession: parseInt(sessionId)
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

    addMesureDialog.value = false;
    await loadMesures();
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

    await loadMesures();
    alert('Mesure supprimée avec succès!');
  } catch (error) {
    console.error('Erreur:', error);
    alert('Une erreur est survenue lors de la suppression.');
  }
};

const formatDate = (dateString) => {
  if (!dateString) return 'Date non spécifiée';

  const date = new Date(dateString);
  return date.toLocaleDateString('fr-FR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric'
  });
};

const goBack = () => {
  router.push('/Sessions');
};
</script>

<style scoped>
.session-details {
  padding-bottom: 80px;
}
</style>
