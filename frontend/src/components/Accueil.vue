<template>
  <v-container
    class="fill-height d-flex flex-column justify-center align-center accueil-container"
  >
    <v-row justify="center" class="mb-5">
      <v-col cols="12" md="8" class="text-center">
        <h1 class="text-h3 font-weight-bold">
          Bienvenue sur <span class="accent-text">Athletica</span>
        </h1>
        <p class="text-body-1 mt-2">
          "Chaque pas compte ! Athletica vous accompagne pour atteindre vos
          objectifs et dépasser vos limites."
        </p>
      </v-col>
    </v-row>

    <v-row justify="center" class="mb-5">
      <v-col cols="12" md="10">
        <v-card class="pa-4 accueil-card">
          <v-row>
            <v-col cols="12" md="6" class="text-center">
              <v-icon size="48" color="primary">mdi-chart-line</v-icon>
              <h3 class="text-h6 font-weight-bold mt-2">
                Suivi des performances
              </h3>
              <p class="text-body-2">
                Analysez vos progrès avec des graphiques détaillés et des
                statistiques claires.
              </p>
            </v-col>
            <v-col cols="12" md="6" class="text-center">
              <v-icon size="48" color="secondary">mdi-run-fast</v-icon>
              <h3 class="text-h6 font-weight-bold mt-2">Vos Sessions</h3>
              <p class="text-body-2">
                Sentez vous libre d'organiser et de personnaliser vos sessions
                pour rester fidèle à vous même.
              </p>
            </v-col>
          </v-row>
          <v-divider class="my-4"></v-divider>
          <v-row>
            <v-col cols="12" md="6" class="text-center">
              <v-icon size="48" color="success">mdi-calendar-clock</v-icon>
              <h3 class="text-h6 font-weight-bold mt-2">Vos Indicateurs</h3>
              <p class="text-body-2">
                Profitez de l'aspect personnalisable que possède Athletica pour
                rendre votre expérience la plus détaillée.
              </p>
            </v-col>
            <v-col cols="12" md="6" class="text-center">
              <v-icon size="48" color="warning">mdi-trophy</v-icon>
              <h3 class="text-h6 font-weight-bold mt-2">Esprit sportif</h3>
              <p class="text-body-2">
                Utilisez cette outil pour parvenir à vos objectifs et devenir la
                meilleure version de vous même.
              </p>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>

    <v-row justify="center">
      <v-col cols="12" md="8" class="text-center">
        <p class="text-body-1 mb-4">
          "Prenez le contrôle de vos entraînements dès aujourd'hui !"
        </p>
        <v-btn
          color="primary"
          class="mx-2"
          to="/MesProgressions"
          variant="elevated"
        >
          Mes Progressions
          <v-icon right>mdi-chart-box</v-icon>
        </v-btn>
        <v-btn
          color="success"
          class="mx-2"
          to="/AjouterSession"
          variant="elevated"
        >
          Ajouter une session
          <v-icon right>mdi-plus-circle</v-icon>
        </v-btn>
        <v-btn
          color="warning"
          class="mx-2"
          to="/AjouterIndicateur"
          variant="elevated"
        >
          Ajouter un indicateur
          <v-icon right>mdi-chart-bar</v-icon>
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup></script>

<style scoped>
.accent-text {
  color: #ff4081;
}

.accueil-container {
  background: linear-gradient(135deg, #ffffff, #f3f4f6);
  padding: 20px;
}

.accueil-card {
  background: #ffffff;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  border-radius: 12px;
}

.v-btn {
  font-weight: bold;
  text-transform: uppercase;
  margin: 10px 0;
}

h1,
h3 {
  color: #0e0c70;
}

.text-body-1 {
  color: #4a4a4a;
}
</style>
