<template>
  <v-container class="progressions-container">
    <h2 class="text-h5 font-weight-bold mb-6 text-center">
      📊 Mes Progressions
    </h2>

    <v-select
      v-model="selectedIndicateurIds"
      :items="indicateurs"
      item-title="nom"
      item-value="id"
      label="Choisir un ou plusieurs indicateurs"
      class="mb-6"
      multiple
      clearable
    />

    <v-row>
      <v-col
        v-for="ind in indicateursSelectionnes"
        :key="ind.id"
        cols="12"
        sm="8"
        md="6"
        class="mx-auto mb-4"
      >
        <v-card elevation="2" class="pa-4 rounded-xl" color="grey-lighten-4">
          <ApexCharts
            width="100%"
            height="300"
            :options="getChartConfig(ind).options"
            :series="getChartConfig(ind).series"
          />

          <v-expansion-panels
            class="mt-4"
            variant="accordion"
            v-if="(mesuresParIndicateur[ind.id] || []).length"
          >
            <v-expansion-panel>
              <v-expansion-panel-title class="font-weight-bold">
                📍 Mesures ({{ mesuresParIndicateur[ind.id].length }})
              </v-expansion-panel-title>

              <v-expansion-panel-text>
                <v-list density="comfortable" class="bg-transparent">
                  <v-list-item
                    v-for="m in mesuresParIndicateur[ind.id]"
                    :key="m.id"
                    class="px-2 d-flex justify-space-between align-center rounded-lg mb-2 hoverable"
                  >
                    <div class="d-flex align-center gap-3">
                      <span class="font-weight-medium">
                        📅
                        {{ new Date(m.dateMesure).toLocaleDateString("fr-FR") }}
                        - 🔢 Valeur : <strong>{{ m.valeur }}</strong>
                      </span>
                    </div>
                    <v-btn
                      icon
                      color="red"
                      variant="text"
                      @click="supprimerMesure(ind, m.id)"
                    >
                      <v-icon>mdi-delete</v-icon>
                    </v-btn>
                  </v-list-item>
                </v-list>
              </v-expansion-panel-text>
            </v-expansion-panel>
          </v-expansion-panels>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, onMounted, computed, watch } from "vue";
import { useRoute } from "vue-router";
import ApexCharts from "vue3-apexcharts";

const route = useRoute();
const indicateurs = ref([]);
const mesuresParIndicateur = ref({});
const selectedIndicateurIds = ref([]);

const fetchMesuresForIndicateur = async (ind) => {
  // Fonction pour récupérer les mesures d'un indicateur
  try {
    const endpoint =
      ind.type === "global"
        ? `http://localhost:8989/api/mesures/indicateur-global/${ind.rawId}` // URL pour les indicateurs globaux
        : `http://localhost:8989/api/mesures/indicateur-session/${ind.rawId}`; // URL pour les indicateurs de session

    const res = await fetch(endpoint); // Requête pour récupérer les mesures
    if (!res.ok) throw new Error();

    const data = await res.json(); // Conversion de la réponse en JSON
    mesuresParIndicateur.value[ind.id] = data.sort(
      // Tri des mesures par date
      (a, b) => new Date(a.dateMesure) - new Date(b.dateMesure)
    );
  } catch (err) {
    console.error(`Erreur fetch mesures ${ind.nom}:`, err); // Gestion des erreurs
    mesuresParIndicateur.value[ind.id] = []; // Réinitialisation des mesures en cas d'erreur
  }
};

const supprimerMesure = async (ind, idMesure) => {
  // Fonction pour supprimer une mesure
  const confirmDelete = confirm(
    "Souhaites-tu vraiment supprimer cette mesure ?"
  );
  if (!confirmDelete) return;

  const endpoint = `http://localhost:8989/api/mesures/${idMesure}`; // URL pour supprimer la mesure
  try {
    const res = await fetch(endpoint, { method: "DELETE" });
    if (res.ok) {
      alert("Mesure supprimée avec succès");
      mesuresParIndicateur.value[ind.id] = mesuresParIndicateur.value[
        ind.id
      ].filter((m) => m.id !== idMesure);
    } else {
      alert(`Erreur suppression (${res.status})`);
    }
  } catch (err) {
    console.error("Erreur suppression :", err);
    alert("Erreur lors de la suppression");
  }
};

onMounted(async () => {
  const [globals, sessions] = await Promise.all([
    fetch("http://localhost:8989/api/indicateurGlobals").then((res) =>
      res.json()
    ),
    fetch("http://localhost:8989/api/indicateurSessions").then((res) =>
      res.json()
    ),
  ]);

  const mappedGlobals = globals.map((ind) => ({
    // Mappage des indicateurs globaux
    id: `global-${ind.idIndicateurGlobal}`,
    nom: ind.nom + " (Global)",
    type: "global",
    rawId: ind.idIndicateurGlobal,
  }));

  const mappedSessions = sessions.map((ind) => ({
    // Mappage des indicateurs de session
    id: `session-${ind.idIndicateurSession}`,
    nom: ind.nom + " (Session)",
    type: "session",
    rawId: ind.idIndicateurSession,
  }));

  indicateurs.value = [...mappedGlobals, ...mappedSessions]; // Fusion des deux types d'indicateurs

  const idFromQuery = route.query.indicateurId; // Récupération de l'ID de l'indicateur depuis la requête
  if (idFromQuery) {
    // Si un ID est présent dans la requête
    selectedIndicateurIds.value = [idFromQuery]; // Sélection de l'indicateur
    const selected = indicateurs.value.find((i) => i.id === idFromQuery); // Recherche de l'indicateur
    if (selected) await fetchMesuresForIndicateur(selected); // Récupération des mesures
  }
});

watch(selectedIndicateurIds, async (newIds) => {
  // Surveillance des indicateurs sélectionnés
  for (const id of newIds) {
    // Vérification de chaque ID
    if (!mesuresParIndicateur.value[id]) {
      // Si les mesures ne sont pas déjà récupérées
      const ind = indicateurs.value.find((i) => i.id === id); // Recherche de l'indicateur
      if (ind) await fetchMesuresForIndicateur(ind); // Récupération des mesures
    }
  }
});

const indicateursSelectionnes = computed(
  () =>
    indicateurs.value.filter((i) => selectedIndicateurIds.value.includes(i.id)) // Filtrage des indicateurs sélectionnés
);

const getChartConfig = (ind) => {
  const mesures = mesuresParIndicateur.value[ind.id] || []; // Récupération des mesures de l'indicateur

  return {
    series: [
      {
        name: ind.nom,
        data: mesures.map((m) => ({
          x:
            ind.type === "session"
              ? `${new Date(m.dateMesure).toLocaleDateString("fr-FR")} - ${
                  m.session?.nom || "Session inconnue"
                }`
              : new Date(m.dateMesure).toLocaleDateString("fr-FR"),
          y: m.valeur,
        })),
      },
    ],
    options: {
      chart: { type: "line", toolbar: { show: false } },
      stroke: { show: false },
      markers: {
        size: 6,
        colors: ["#007bff"],
        strokeColors: "#fff",
        strokeWidth: 2,
        hover: { size: 8 },
      },
      title: {
        text: `${ind.nom}`,
        align: "center",
        style: { fontSize: "16px" },
      },
      xaxis: {
        title: { text: "Date" },
        type: "category",
        labels: {
          rotate: -45,
          style: { fontSize: "12px", colors: "#333" },
        },
      },
      yaxis: { title: { text: "Valeur mesurée" } },
      dataLabels: { enabled: true },
    },
  };
};
</script>

<style scoped>
.progressions-container {
  padding-top: 32px;
}
.v-card {
  border-radius: 16px !important;
}
.v-list-item {
  transition: background-color 0.2s ease;
}
.v-list-item.hoverable:hover {
  background-color: #e3f2fd;
}
.gap-3 {
  gap: 12px;
}
</style>
