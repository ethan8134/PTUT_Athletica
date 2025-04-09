<template>
  <v-app>
    <v-app-bar color="#0e0c70" density="compact">
      <template v-slot:prepend>
        <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
      </template>
      <v-app-bar-title>Athletica</v-app-bar-title>
      <v-btn to="/PageAide">Aide</v-btn>
    </v-app-bar>
    <v-navigation-drawer app v-model="drawer">
      <v-divider></v-divider>
      <v-list dense nav>
        <v-list-item to="/">
          <v-list-item-title>Accueil</v-list-item-title>
        </v-list-item>
        <v-list-item to="/MesIndicateurs">
          <v-list-item-title>Mes Indicateurs</v-list-item-title>
        </v-list-item>
        <v-list-item to="/MesSessions">
          <v-list-item-title>Mes sessions</v-list-item-title>
        </v-list-item>
      </v-list>
      <v-list-item to="/MesProgressions">
        <v-list-item-title>Mes Progressions</v-list-item-title>
      </v-list-item>
      <v-list-item to="/PageMonCompte">
        <v-list-item-title>Mon Compte</v-list-item-title>
      </v-list-item>
    </v-navigation-drawer>
    <v-main>
      <v-container class="page">
        <RouterView />
      </v-container>
    </v-main>
  </v-app>
</template>
<script setup>
import { ref } from "vue";
const drawer = ref(false);
</script>
<style scoped>
.page {
  min-height: 100vh;
  text-align: center;
}

.v-navigation-drawer {
  background-color: #0e0c70;
}

.v-list-item-title {
  color: white;
}
</style>
