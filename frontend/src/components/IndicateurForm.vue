<template>
  <div class="indicateur-form-container">
    <h2>✍️ Ajouter un indicateur</h2>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label>Nom de l'indicateur</label>
        <input v-model="indicateur.nom" placeholder="Ex: Rythme Cardiaque" required />
      </div>

      <div class="form-group">
        <label>Unité</label>
        <input v-model="indicateur.unite" placeholder="Ex: bpm" required />
      </div>

      <div class="form-group">
        <label>Catégorie</label>
        <input v-model="indicateur.categorie" placeholder="Ex: Indicateur de santé" required />
      </div>

      <!-- Valeur initiale -->
      <div class="form-group">
        <label>Valeur initiale</label>
        <input v-model="mesure.valeur" type="number" placeholder="Ex: 12.5" />
      </div>

      <div class="form-group">
        <label>Date de la mesure</label>
        <input v-model="mesure.dateMesure" type="date" />
      </div>

      <div class="form-buttons">
        <button class="add-btn" type="submit">✅ Valider</button>
        <button class="cancel-btn" type="button" @click="cancelForm"> Annuler</button>
      </div>
    </form>
  </div>

  <!-- Associer un indicateur existant -->
  <div v-if="sessionId" class="existing-indicators">
    <h3>Ou associer un indicateur existant</h3>
    <ul>
      <li
        v-for="ind in indicateursSessionExistants"
        :key="ind.idIndicateurSession"
        class="indicator-item"
      >
        <span>{{ ind.nom }} ({{ ind.unite }})</span>
        <input v-model="ind.newValeur" placeholder="Valeur" type="number" class="measure-input" />
        <input v-model="ind.newDate" placeholder="Date" type="date" class="measure-input" />
        <button @click="associerIndicateurExist(ind)">Associer</button>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
const sessionId = route.query.sessionId;

const indicateursSessionExistants = ref([]);

onMounted(() => {
  if (sessionId) fetchIndicateursSessions();
});

const fetchIndicateursSessions = () => {
  fetch("http://localhost:8989/api/indicateurSessions")
    .then(res => res.json())
    .then(data => {
      indicateursSessionExistants.value = data
        .filter(i => !i.session)
        .map(i => ({ ...i, newValeur: "", newDate: "" }));
    });
};

const associerIndicateurExist = async (ind) => {
  if (!ind.newValeur || !ind.newDate) {
    alert("Remplis bien la valeur et la date !");
    return;
  }

  try {
    await fetch(`http://localhost:8989/api/indicateurSessions/${ind.idIndicateurSession}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        ...ind,
        session: { idSession: parseInt(sessionId) },
      }),
    });

    await fetch("http://localhost:8989/api/mesures", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        valeur: parseFloat(ind.newValeur),
        dateMesure: ind.newDate,
        indicateurSession: { idIndicateurSession: ind.idIndicateurSession },
      }),
    });

    alert("✅ Indicateur associé avec mesure !");
    fetchIndicateursSessions();

  } catch (err) {
    console.error("Erreur association :", err);
    alert("❌ Échec association !");
  }
};

const indicateur = ref({
  nom: "",
  unite: "",
  categorie: "",
});

const mesure = ref({
  valeur: "",
  dateMesure: "",
});

const submitForm = async () => {
  if (!indicateur.value.nom || !indicateur.value.unite || !indicateur.value.categorie) {
    alert("Remplis bien tous les champs !");
    return;
  }

  const bodyIndicateur = {
    nom: indicateur.value.nom,
    unite: indicateur.value.unite,
    date: new Date().toISOString().split('T')[0],
    categorie: { idCategorie: 1 },
    utilisateur: { idPersonne: 1 },
    session: sessionId ? { idSession: parseInt(sessionId) } : null,
  };

  if (!bodyIndicateur.session) {
    alert("Aucune session associée, impossible de continuer.");
    return;
  }

  try {
    const res = await fetch("http://localhost:8989/api/indicateurSessions", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(bodyIndicateur),
    });

    if (!res.ok) throw new Error("Erreur indicateur");

    const created = await res.json();

    if (mesure.value.valeur && mesure.value.dateMesure) {
      const bodyMesure = {
        valeur: parseFloat(mesure.value.valeur),
        dateMesure: mesure.value.dateMesure,
        indicateurSession: { idIndicateurSession: created.idIndicateurSession },
      };

      const resMesure = await fetch("http://localhost:8989/api/mesures", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(bodyMesure),
      });

      if (!resMesure.ok) throw new Error("Erreur mesure");
    }

    alert("✅ Indicateur (et mesure) créé !");
    router.push("/");
  } catch (err) {
    console.error("Erreur :", err);
    alert("Une erreur s’est produite.");
  }
};

const cancelForm = () => {
  router.push("/");
};
</script>

<style scoped>
.indicateur-form-container {
  max-width: 600px;
  margin: 20px auto;
  padding: 20px;
  background-color: lightgrey;
  border-radius: 8px;
}
.form-group {
  margin-bottom: 15px;
  display: flex;
  flex-direction: column;
}
.form-buttons {
  display: flex;
  gap: 10px;
  justify-content: space-between;
}
.add-btn {
  background-color: #28a745;
  color: white;
  padding: 10px;
  border: none;
}
.cancel-btn {
  background-color: #dc3545;
  color: white;
  padding: 10px;
  border: none;
}
.existing-indicators {
  margin-top: 30px;
  padding: 15px;
  background: #f5f5f5;
  border-radius: 6px;
  border: 1px solid #ccc;
}
.indicator-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 8px 0;
  gap: 10px;
}
.indicator-item button {
  background-color: #007fff;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
}
.indicator-item button:hover {
  background-color: #005bb5;
}
.measure-input {
  padding: 6px;
  border-radius: 4px;
  border: 1px solid #999;
  width: 110px;
}
</style>
