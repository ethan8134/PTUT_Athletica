<template>
  <v-container class="fill-height d-flex justify-center align-center">
    <v-card class="mx-auto pa-5 text-center accueil-card" max-width="700">
      <!-- Titre principal -->
      <v-card-title class="text-h4 font-weight-bold">
        Bienvenue sur <span class="accent-text">Athletica</span> 🏃‍♂️
      </v-card-title>

      <!-- Sous-titre motivant -->
      <v-card-subtitle class="text-body-1 mt-3">
        "Chaque pas compte ! Athletica est là pour vous aider à atteindre vos
        objectifs et à dépasser vos limites."
      </v-card-subtitle>

      <v-divider class="my-4"></v-divider>

      <!-- Texte explicatif -->
      <v-card-text class="text-body-1">
        Découvrez des outils avancés pour optimiser vos entraînements et suivre
        vos progrès :
      </v-card-text>

      <!-- Liste des fonctionnalités -->
      <v-list dense>
        <v-list-item>
          <v-list-item-icon>
            <v-icon color="primary">mdi-chart-line</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            Suivi détaillé de vos performances
          </v-list-item-content>
        </v-list-item>
        <v-list-item>
          <v-list-item-icon>
            <v-icon color="secondary">mdi-run-fast</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            Analyse en temps réel de vos sessions
          </v-list-item-content>
        </v-list-item>
        <v-list-item>
          <v-list-item-icon>
            <v-icon color="success">mdi-calendar-clock</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            Planification et gestion des entraînements
          </v-list-item-content>
        </v-list-item>
        <v-list-item>
          <v-list-item-icon>
            <v-icon color="warning">mdi-trophy</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            Définition d’objectifs personnalisés
          </v-list-item-content>
        </v-list-item>
      </v-list>

      <v-divider class="my-4"></v-divider>

      <!-- Texte motivant -->
      <v-card-text class="text-body-2">
        🎯 "Votre progression commence ici ! Prenez le contrôle de vos
        entraînements et transformez vos efforts en succès."
      </v-card-text>

      <!-- Boutons de redirection -->
      <v-btn color="primary" class="mt-3" to="/MesProgressions" variant="flat">
        Accéder à mes Progressions
        <v-icon right>mdi-chart-box</v-icon>
      </v-btn>
      <v-btn color="success" class="mt-3" to="/AjouterSession" variant="flat">
        Ajouter une session
        <v-icon right>mdi-plus-circle</v-icon>
      </v-btn>
      <v-btn
        color="warning"
        class="mt-3"
        to="/AjouterIndicateur"
        variant="flat"
      >
        Ajouter un indicateur
        <v-icon right>mdi-chart-bar"></v-icon>
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

.v-btn {
  font-weight: bold;
}
</style>
