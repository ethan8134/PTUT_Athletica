<template>
  <v-container class="button-container">
    <v-list-item to="/AjouterIndicateurGlobal" class="redirect-btn">
      <v-list-item-title
        >Enregistrer un nouvel indicateur global</v-list-item-title
      >
    </v-list-item>

    <v-list-item to="/AjouterIndicateur" class="redirect-btn">
      <v-list-item-title>Créer un indicateur de session</v-list-item-title>
    </v-list-item>

    <v-list-item to="/Indicateurs" class="redirect-btn">
      <v-list-item-title>Consulter mes Indicateurs</v-list-item-title>
    </v-list-item>
  </v-container>

  </v-container>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
const sessionId = route.query.sessionId;


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
  justify-content: space-evenly;
  align-items: center;
  margin: 0;
  gap: 16px;
}

.redirect-btn {
  display: flex;
  width: 300px;
  height: 170px;
  justify-content: center;
  padding: 8px 16px;
  background-color: #0e0c70;
  color: #ccc;
  text-align: center;
  font-weight: bold;
  text-decoration: none;
  transition: background-color 0.3s ease;
  border-radius: 150px;
}

.redirect-btn:hover {
  background-color: #005bb5;
}
</style>
