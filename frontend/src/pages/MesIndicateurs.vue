<template>
  <v-container class="button-container">
    <v-list-item to="/AjouterIndicateurGlobal" class="redirect-btn">
    <v-list-item-title>Enregistrer un nouvel indicateur global</v-list-item-title>
    </v-list-item>
    <v-list-item to="/Indicateurs" class="redirect-btn">
      <v-list-item-title>Consulter mes Indicateurs</v-list-item-title>
    </v-list-item>
  </v-container>
</template>

<script setup></script>

<style scoped>
.button-container {
  display: flex;
  justify-content: space-evenly; /* Répartit les éléments avec un espace égal */
  align-items: center;
  margin: 0;
  gap: 16px; /* Optionnel : ajoute un espace fixe entre les boutons */
}

/* Style personnalisé pour rendre le bouton compact et visible */
.redirect-btn {
  display: flex;
  height: 90%;
  padding: 8px 16px;
  background-color: #007fff;
  color: #fff;
  border-radius: 4px;
  text-align: center;
  font-weight: bold;
  text-decoration: none;
  transition: background-color 0.3s ease;
}

/* Changement de couleur au survol */
.redirect-btn:hover {
  background-color: #005bb5;
}
</style>
