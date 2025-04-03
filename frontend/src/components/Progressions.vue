<template>
  <v-container class="progressions-container">
    <h2 class="text-h5 font-weight-bold mb-6 text-center">📈 Mes Progressions</h2>

    <v-row v-if="indicateurSessions.length">
      <v-col
        v-for="ind in indicateurSessions"
        :key="ind.id"
        cols="12"
        sm="6"
        md="6"
        class="mb-6"
      >
        <v-card elevation="2" class="pa-4 rounded-xl" color="grey-lighten-4">
          <ApexChart
            width="100%"
            height="300"
            type="bar"
            :options="getChartConfig(ind).options"
            :series="getChartConfig(ind).series"
          />
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import ApexChart from 'vue3-apexcharts';

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
        id: ind.idIndicateurSession
      }));

      indicateurSessions.value.forEach((ind) => fetchMesuresForIndicateur(ind.id));
    })
    .catch((err) => console.error('Erreur récupération indicateurs session:', err));
});

const getChartConfig = (ind) => {
  if (!ind || !mesuresParIndicateur.value[ind.id]) return { series: [], options: {} };

  const mesures = mesuresParIndicateur.value[ind.id];

  const series = [
    {
      name: ind.nom,
      data: mesures.map((m) => ({
        x: m.dateMesure,
        y: m.valeur
      }))
    }
  ];

  const options = {
    chart: { type: 'bar' },
    title: {
      text: `${ind.nom} - Session : ${ind.session?.nom || 'Non défini'}`,
      align: 'center'
    },
    xaxis: { title: { text: 'Date' }, type: 'category' },
    yaxis: { title: { text: 'Valeur mesurée' } },
    dataLabels: { enabled: true }
  };

  return { series, options };
};
</script>

<style scoped>
.progressions-container {
  padding-top: 32px;
}
</style>
