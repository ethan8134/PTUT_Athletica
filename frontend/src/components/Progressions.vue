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
                        {{
                          new Date(m.dateMesure).toLocaleDateString("fr-FR")
                        }}
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
import ApexCharts from "vue3-apexcharts";

const indicateurs = ref([]);
const mesuresParIndicateur = ref({});
const selectedIndicateurIds = ref([]);

const fetchMesuresForIndicateur = async (ind) => {
  // Fetch les mesures pour un indicateur
  try {
    // Vérifie si l'indicateur est déjà présent dans le tableau
    const endpoint = // Définit l'endpoint en fonction du type d'indicateur
      ind.type === "global"
        ? `http://localhost:8989/api/mesures/indicateur-global/${ind.rawId}`
        : `http://localhost:8989/api/mesures/indicateur-session/${ind.rawId}`;

    const res = await fetch(endpoint);

    if (!res.ok) {
      // Vérifie si la réponse est correcte
      console.error(`Erreur API ${res.status} pour ${ind.nom}`);
      mesuresParIndicateur.value[ind.id] = [];
      return;
    }

    const data = await res.json();
    mesuresParIndicateur.value[ind.id] = data.sort(
      // Trie les mesures par date
      (a, b) => new Date(a.dateMesure) - new Date(b.dateMesure)
    );
  } catch (err) {
    console.error(`Erreur fetch mesures ${ind.nom}:`, err);
    mesuresParIndicateur.value[ind.id] = [];
  }
};

const supprimerMesure = async (ind, idMesure) => {
  // Supprime une mesure
  const confirmDelete = confirm(
    "Souhaites-tu vraiment supprimer cette mesure ? Cette action est irréversible."
  );
  if (!confirmDelete) return;

  const endpoint = `http://localhost:8989/api/mesures/${idMesure}`; // Définit l'endpoint pour la suppression

  try {
    const res = await fetch(endpoint, { method: "DELETE" });

    if (res.ok) {
      alert("Mesure supprimée avec succès");
      mesuresParIndicateur.value[ind.id] = mesuresParIndicateur.value[
        ind.id
      ].filter((m) => m.id !== idMesure);
    } else {
      alert(`Erreur de suppression (code ${res.status})`);
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
    // Mappe les indicateurs globaux
    id: `g-${ind.idIndicateurGlobal}`,
    nom: ind.nom + " (Global)",
    type: "global",
    rawId: ind.idIndicateurGlobal,
  }));

  const mappedSessions = sessions.map((ind) => ({
    // Mappe les indicateurs de session
    id: `s-${ind.idIndicateurSession}`,
    nom: ind.nom + " (Session)",
    type: "session",
    rawId: ind.idIndicateurSession,
  }));

  indicateurs.value = [...mappedGlobals, ...mappedSessions].filter(
    // Filtre les indicateurs valides
    (ind) => ind.nom && ind.nom.trim() !== ""
  );
});

watch(selectedIndicateurIds, async (newIds) => {
  // Vérifie les indicateurs sélectionnés
  for (const id of newIds) {
    // Parcourt les IDs sélectionnés
    if (!mesuresParIndicateur.value[id]) {
      // Vérifie si les mesures existent déjà
      const ind = indicateurs.value.find((i) => i.id === id); // Trouve l'indicateur correspondant
      if (ind) await fetchMesuresForIndicateur(ind); // Récupère les mesures
    }
  }
});

const indicateursSelectionnes = computed(() =>
  // Filtre les indicateurs sélectionnés
  indicateurs.value.filter((i) => selectedIndicateurIds.value.includes(i.id))
);

const getChartConfig = (ind) => {
  const mesures = mesuresParIndicateur.value[ind.id] || [];

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
      chart: {
        type: "line", // On garde "line" pour que les points restent bien positionnés
        toolbar: { show: false },
      },
      stroke: {
        show: false, // ❌ PAS de ligne entre les points
      },
      markers: {
        size: 6,
        colors: ["#007bff"],
        strokeColors: "#fff",
        strokeWidth: 2,
        hover: {
          size: 8,
        },
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
          style: {
            fontSize: "12px",
            colors: "#333",
          },
        },
      },
      yaxis: {
        title: { text: "Valeur mesurée" },
      },
      dataLabels: {
        enabled: true,
        style: {
          fontSize: "12px",
          colors: ["#000"],
        },
      },
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
