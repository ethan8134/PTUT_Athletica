<template>
  <v-container class="progressions-container">
    <h2 class="text-h5 font-weight-bold mb-6 text-center">📊 Mes Progressions</h2>

    <v-select
      v-model="selectedIndicateurId"
      :items="indicateurSessions"
      item-title="nom"
      item-value="id"
      label="Choisir un indicateur"
      class="mb-6"
      clearable
    />

    <v-row v-if="indicateurSelectionne">
      <v-col cols="12" sm="8" md="6" class="mx-auto">
        <v-card elevation="2" class="pa-4 rounded-xl" color="grey-lighten-4">
          <ApexCharts
            width="100%"
            height="300"
            type="bar"
            :options="getChartConfig(indicateurSelectionne).options"
            :series="getChartConfig(indicateurSelectionne).series"
          />
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>


<script setup>
import { ref, onMounted, computed } from 'vue'
import ApexCharts from 'vue3-apexcharts'

const indicateurSessions = ref([])
const mesuresParIndicateur = ref({})
const selectedIndicateurId = ref(null)

const fetchMesuresForIndicateur = async (id) => {
  try {
    const res = await fetch(`http://localhost:8989/api/mesures/indicateur/${id}`)
    const data = await res.json()
    mesuresParIndicateur.value[id] = data.sort((a, b) =>
      new Date(a.dateMesure) - new Date(b.dateMesure)
    )
  } catch (err) {
    console.error("Erreur fetch mesures :", err)
  }
}

onMounted(() => {
  fetch('http://localhost:8989/api/indicateurSessions')
    .then(res => res.json())
    .then(data => {
      indicateurSessions.value = data.map(ind => ({
        ...ind,
        id: ind.idIndicateurSession
      }))
      indicateurSessions.value.forEach(ind => fetchMesuresForIndicateur(ind.id))
    })
})

const indicateurSelectionne = computed(() => {
  return indicateurSessions.value.find(i => i.id === selectedIndicateurId.value)
})

const getChartConfig = (ind) => {
  const mesures = mesuresParIndicateur.value[ind.id] || []


  return {
    series: [{
      name: ind.nom,
      data: mesures.map(m => ({
        x: m.dateMesure,
        y: m.valeur
      }))
    }],
    options: {
      chart: { type: 'bar' },
      title: {
        text: `${ind.nom} - Session : ${ind.session?.nom || 'Inconnue'}`,
        align: 'center'
      },
      xaxis: { title: { text: 'Date' }, type: 'category' },
      yaxis: { title: { text: 'Valeur mesurée' } },
      dataLabels: { enabled: true }
    }
  }
}
</script>

<style scoped>
.progressions-container {
  padding-top: 32px;
}

</style>
