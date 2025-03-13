<template>
  <v-container class="fill-height d-flex justify-center align-center">
    <v-card class="mx-auto pa-5 text-center accueil-card" max-width="700">
      <v-card-title class="text-h4 font-weight-bold">
        Bienvenue sur <span class="accent-text">Athletica</span> 🏃‍♂️
      </v-card-title>

      <v-card-subtitle class="text-body-1 mt-3">
        Personnalisez les données de vos entraînements pour atteindre vos
        objectifs plus rapidement !
      </v-card-subtitle>

      <v-divider class="my-4"></v-divider>

      <v-card-text class="text-body-1">
        Athletica vous accompagne dans vos entraînements en course à pied grâce
        à des outils avancés :
      </v-card-text>

      <v-list dense>
        <v-list-item>
          <v-list-item-icon
            ><v-icon color="primary">mdi-chart-line</v-icon></v-list-item-icon
          >
          <v-list-item-content
            >Suivi détaillé de vos performances</v-list-item-content
          >
        </v-list-item>
        <v-list-item>
          <v-list-item-icon
            ><v-icon color="secondary">mdi-run-fast</v-icon></v-list-item-icon
          >
          <v-list-item-content
            >Analyse en temps réel de vos sessions</v-list-item-content
          >
        </v-list-item>
        <v-list-item>
          <v-list-item-icon
            ><v-icon color="success"
              >mdi-calendar-clock</v-icon
            ></v-list-item-icon
          >
          <v-list-item-content
            >Planification et gestion des entraînements</v-list-item-content
          >
        </v-list-item>
        <v-list-item>
          <v-list-item-icon
            ><v-icon color="warning">mdi-trophy</v-icon></v-list-item-icon
          >
          <v-list-item-content
            >Définition d’objectifs personnalisés</v-list-item-content
          >
        </v-list-item>
      </v-list>

      <v-divider class="my-4"></v-divider>

      <v-card-text class="text-body-2">
        🎯 Votre progression commence ici ! Consultez vos statistiques et
        affinez vos entraînements pour maximiser vos performances.
      </v-card-text>

      <v-btn color="primary" class="mt-3" to="/dashboard" variant="flat">
        Accéder au Tableau de Bord
        <v-icon right>mdi-arrow-right</v-icon>
      </v-btn>
    </v-card>
  </v-container>
</template>

<script setup>
// Aucun script nécessaire pour ce composant statique
</script>

<style scoped>
.accent-text {
  color: #ff4081;
}

.accueil-card {
  background: linear-gradient(135deg, #f5f5f5, #e3f2fd);
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  border-radius: 12px;
}
</style>
