<template>
  <v-app>
    <template v-if="!isLoggedIn">
      <div class="login-page">
        <div class="login-container">
          <h1 class="title">Athletica</h1>
          <p class="subtitle">Bienvenue</p>
          <p class="description">
            Veuillez saisir vos données de connexion ci-dessous
          </p>
          <v-form>
            <v-text-field
              label="Utilisateur (Email)"
              outlined
              class="mb-4"
            ></v-text-field>
            <v-text-field
              label="Mot de passe"
              type="password"
              outlined
              class="mb-4"
            ></v-text-field>
            <v-btn color="primary" block @click="login">Se connecter</v-btn>
            <v-btn color="secondary" block outlined class="mt-2"
              >S'inscrire</v-btn
            >
          </v-form>
        </div>
      </div>
    </template>

    <template v-else>
      <v-app-bar color="#0e0c70" density="compact">
        <template v-slot:prepend>
          <v-app-bar-nav-icon
            @click.stop="drawer = !drawer"
          ></v-app-bar-nav-icon>
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
            <v-list-item-title>Mes Sessions</v-list-item-title>
          </v-list-item>
          <v-list-item to="/MesProgressions">
            <v-list-item-title>Mes Progressions</v-list-item-title>
          </v-list-item>
          <v-list-item to="/PageMonCompte">
            <v-list-item-title>Mon Compte</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-navigation-drawer>
      <v-main>
        <v-container class="page">
          <RouterView />
        </v-container>
      </v-main>
    </template>
  </v-app>
</template>

<script setup>
import { ref } from "vue";

const isLoggedIn = ref(false);
const drawer = ref(false);

function login() {
  isLoggedIn.value = true;
}
</script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(to bottom, #0e0c70, #1a1a1a);
  color: white;
}

.login-container {
  background-color: rgba(255, 255, 255, 0.1);
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
  text-align: center;
  width: 100%;
  max-width: 400px;
}

.title {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 10px;
}

.subtitle {
  font-size: 1.2rem;
  margin-bottom: 20px;
}

.description {
  font-size: 0.9rem;
  margin-bottom: 30px;
}

.v-btn {
  font-size: 1rem;
}

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
