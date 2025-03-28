<template>
  <v-container class="progressions-container">
    <h2 class="text-h5 font-weight-bold mb-6 text-center">📈 Mes Progressions</h2>

    <v-row>
      <v-col
        v-for="ind in indicateurSessions"
        :key="ind.id"
        cols="12"
        sm="6"
        md="4"
      >
        <v-card elevation="2" class="pa-4 rounded-xl">
          <div class="text-subtitle-1 font-weight-bold mb-1">{{ ind.nom }}</div>
          <div class="text-caption text-grey mb-3">
            {{ ind.categorie?.nom || ind.categorie }}
          </div>

          <v-list density="compact" class="progress-list">
            <v-list-item
              v-for="mesure in mesuresParIndicateur[ind.id]"
              :key="mesure.id"
              class="d-flex flex-column"
            >
              <div class="d-flex justify-space-between align-center mb-1">
                <span class="text-caption">{{ mesure.dateMesure }}</span>
                <span class="text-caption font-weight-medium">{{ mesure.valeur }}</span>
              </div>
              <v-progress-linear
                :model-value="mesure.valeur"
                height="6"
                color="primary"
                rounded
              />
            </v-list-item>
          </v-list>

          <div
            v-if="!mesuresParIndicateur[ind.id] || mesuresParIndicateur[ind.id].length === 0"
            class="text-caption text-grey mt-2"
          >
            Aucune mesure enregistrée.
          </div>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const indicateurSessions = ref([]);
const mesuresParIndicateur = ref({});

const fetchMesuresForIndicateur = async (id) => {
  try {
    const response = await fetch(`http://localhost:8989/api/mesures/indicateur/${id}`);
    const data = await response.json();

    const triées = data.sort((a, b) =>
      new Date(a.dateMesure || a.date_mesure) - new Date(b.dateMesure || b.date_mesure)
    );

    mesuresParIndicateur.value[id] = triées;
  } catch (err) {
    console.error('Erreur fetch mesures pour indicateur', id, err);
  }
};

onMounted(() => {
  fetch('http://localhost:8989/api/indicateurSessions')
    .then((res) => res.json())
    .then((data) => {
      indicateurSessions.value = data.map((ind) => ({
        ...ind,
        id: ind.idIndicateurSession,
      }));

      indicateurSessions.value.forEach((ind) => {
        fetchMesuresForIndicateur(ind.id);
      });
    })
    .catch((err) => console.error('Erreur récupération indicateurs session:', err));
});
</script>

<style scoped>
.progressions-container {
  padding-top: 32px;
}

.progress-list {
  background-color: transparent;
}
</style>
