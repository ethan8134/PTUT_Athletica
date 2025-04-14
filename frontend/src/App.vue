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
              v-model="email"
            ></v-text-field>
            <v-text-field
              label="Mot de passe"
              type="password"
              outlined
              class="mb-4"
              v-model="password"
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
        <v-btn color="#ffffff" outlined @click="logout">Déconnexion</v-btn>
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
import { ref, onMounted, onUnmounted, watch } from "vue";

// État de connexion
const isLoggedIn = ref(false);
const drawer = ref(false);

// Champs de connexion
const email = ref("");
const password = ref("");

// Données utilisateur simulées
const validCredentials = {
  email: "test@user.com",
  password: "password",
};

// Timer pour la déconnexion automatique
let logoutTimer = null;

// Fonction pour démarrer le timer de déconnexion
function startLogoutTimer() {
  clearTimeout(logoutTimer); // Réinitialise le timer s'il existe déjà
  logoutTimer = setTimeout(() => {
    logout(); // Déconnecte l'utilisateur après 60 secondes d'inactivité
  }, 120000); // 60 secondes
}

// Fonction pour arrêter le timer de déconnexion
function stopLogoutTimer() {
  clearTimeout(logoutTimer); // Arrête le timer
}

// Fonction de déconnexion
function logout() {
  isLoggedIn.value = false;
  localStorage.removeItem("isLoggedIn"); // Supprime l'état de connexion
  stopLogoutTimer(); // Arrête le timer
  alert("Vous avez été déconnecté pour inactivité.");
}

// Fonction de connexion
function login() {
  if (
    email.value === validCredentials.email &&
    password.value === validCredentials.password
  ) {
    // Connexion réussie
    isLoggedIn.value = true;
    localStorage.setItem("isLoggedIn", "true"); // Stocke l'état de connexion
    alert("Connexion réussie !");
    startLogoutTimer(); // Démarre le timer pour la déconnexion automatique
  } else {
    // Connexion échouée
    alert("Email ou mot de passe incorrect.");
  }
}

// Gestion de l'activité utilisateur
function resetLogoutTimer() {
  if (isLoggedIn.value) {
    stopLogoutTimer(); // Arrête le timer existant
    startLogoutTimer(); // Redémarre le timer
  }
}

// Ajoute les écouteurs d'événements pour détecter l'activité utilisateur
onMounted(() => {
  const storedLoginState = localStorage.getItem("isLoggedIn");
  if (storedLoginState === "true") {
    isLoggedIn.value = true;
    startLogoutTimer(); // Démarre le timer si l'utilisateur est connecté
  }

  // Écoute les événements d'activité utilisateur
  window.addEventListener("mousemove", resetLogoutTimer);
  window.addEventListener("keydown", resetLogoutTimer);
  window.addEventListener("click", resetLogoutTimer);
});

// Supprime les écouteurs d'événements lors du démontage du composant
onUnmounted(() => {
  window.removeEventListener("mousemove", resetLogoutTimer);
  window.removeEventListener("keydown", resetLogoutTimer);
  window.removeEventListener("click", resetLogoutTimer);
  stopLogoutTimer(); // Arrête le timer si le composant est démonté
});

// Réinitialise le timer à chaque changement de `isLoggedIn`
watch(isLoggedIn, (newValue) => {
  if (newValue) {
    startLogoutTimer();
  } else {
    stopLogoutTimer();
  }
});
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
