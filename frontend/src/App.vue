<template>
  <v-app>
    <!-- une barre de navigation -->
    <v-app-bar color="#0e0c70" density="compact">
      <!-- mettre l'icone dans le 'slot prepend' de 'v-app-bar' soit au début à gauche -->
      <template v-slot:prepend>
        <!-- un clic sur l'icone cache/affiche la zone de menu de gauche -->
        <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
      </template>
      <!-- le titre de la barre de navigation -->
      <v-app-bar-title>Athletica</v-app-bar-title>
      <!-- un bouton avec un menu -->

      <!-- un vers la page "about" -->
      <v-btn to="/PageAide">Aide</v-btn>
      <!-- mettre l'icone dans le 'slot append' de 'v-app-bar' soit au fond à droite -->
      <template v-slot:append>
        <v-btn id="menu-droite" icon="mdi-dots-vertical"></v-btn>
        <!-- le menu s'affichera qd l'élément d'id 'menu-droite' ici 'v-btn' sera activé-->
        <v-menu activator="#menu-droite">
          <v-list>
            <v-list-item to="/PageChoix1">
              <!-- navigation vers la page "choix1" -->
              <v-list-item-title>Choix1</v-list-item-title>
            </v-list-item>
            <v-list-item to="/PageChoix2">
              <!-- navigation vers la page "choixé" -->
              <v-list-item-title>Choix2</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      </template>
    </v-app-bar>
    <!-- zone de menu de gauche caché / visible en fonction du booléen 'drawer'-->
    <v-navigation-drawer app v-model="drawer">
      <v-divider></v-divider>
      <v-list dense nav>
        <v-list-item to="/">
          <v-list-item-title>Accueil</v-list-item-title>
        </v-list-item>
        <v-list-item to="/MesIndicateurs">
          <!-- navigation vers la page "item1" -->
          <v-list-item-title>Mes Indicateurs</v-list-item-title>
        </v-list-item>
        <v-list-item to="/MesSessions">
          <!-- navigation vers la page "item2" -->
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
    <!-- la zone où sera affichée les différentes "pages"-->
    <v-main>
      <v-container class="page">
        <!-- les "composant view" s'afficheront ici -->
        <RouterView />
      </v-container>
    </v-main>
  </v-app>
</template>
<script setup>
import { ref } from "vue";
const drawer = ref(false); // booléen pour afficher/cacher la zone de gauche
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
