<template>
  <v-container class="button-container">
    <router-link to="/AjouterIndicateurGlobal" class="redirect-btn">
      Enregistrer un nouvel indicateur global
    </router-link>

    <router-link to="/AjouterIndicateur" class="redirect-btn">
      Enregistrer un nouvel indicateur sessions
    </router-link>

    <router-link to="/Indicateurs" class="redirect-btn">
      Consulter mes Indicateurs
    </router-link>
    <v-btn color="grey-darken-1" @click="goBack" class="mt-4">
      🔙 Retour
    </v-btn>

  </v-container>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
const sessionId = route.query.sessionId;
const goBack = () => {
  router.back()
}


const indicateur = ref({
  nom: "",
  unite: "",
  categorie: ""
});

const mesure = ref({
  valeur: "",
  dateMesure: ""
});

const submitForm = async () => {
  if (!indicateur.value.nom || !indicateur.value.unite || !indicateur.value.categorie) {
    alert("Remplis bien tous les champs !");
    return;
  }

  // ✅ Création sans session
  const bodyIndicateur = {
    nom: indicateur.value.nom,
    unite: indicateur.value.unite,
    date: new Date().toISOString().split("T")[0],
    categorie: { idCategorie: 1 },
    utilisateur: { idPersonne: 1 }
  };

  try {
    const res = await fetch("http://localhost:8989/api/indicateurSessions", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(bodyIndicateur)
    });

    if (!res.ok) throw new Error("Erreur indicateur");

    const created = await res.json();

    // 💡 Ajout de mesure liée à une session
    if (mesure.value.valeur && mesure.value.dateMesure && sessionId) {
      const bodyMesure = {
        valeur: parseFloat(mesure.value.valeur),
        dateMesure: mesure.value.dateMesure,
        indicateurSession: { idIndicateurSession: created.idIndicateurSession },
        session: { idSession: parseInt(sessionId) }
      };

      const resMesure = await fetch("http://localhost:8989/api/mesures", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(bodyMesure)
      });

      if (!resMesure.ok) throw new Error("Erreur mesure");
    }

    alert("✅ Indicateur créé !");
    router.push("/");
  } catch (err) {
    console.error("Erreur :", err);
    alert("Une erreur s’est produite.");
  }
};

</script>



<style scoped>
.button-container {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 20px;
  padding: 40px 0;
}

/* Style des boutons */
.redirect-btn {
  padding: 14px 24px;
  background-color: #007fff;
  color: white;
  border-radius: 8px;
  text-decoration: none;
  font-size: 16px;
  font-weight: bold;
  transition: background-color 0.3s ease;
}

.redirect-btn:hover {
  background-color: #005bb5;
}
</style>
