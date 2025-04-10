<template>
  <div class="indicateur-form-container">
    <h2>✍️ Ajouter un indicateur</h2>
    <form @submit.prevent="submitForm">
      <div class="form-group">

        <label>Nom de l'indicateur</label>
        <input
          v-model="indicateur.nom"
          placeholder="Ex: Rythme Cardiaque"
          required
        />
      </div>

      <div class="form-group">
        <label>Unité</label>
        <input v-model="indicateur.unite" placeholder="Ex: bpm" required />
      </div>

      <div class="form-group">
        <label>Catégorie</label>
        <input
          v-model="indicateur.categorie"
          placeholder="Ex: Indicateur de santé"
          required
        />
      </div>

      <div class="form-buttons">
        <button class="add-btn" type="submit">✅ Valider</button>
        <button class="cancel-btn" type="button" @click="cancelForm">
          Annuler
        </button>
      </div>
    </form>
    <div v-if="sessionId">
      <hr style="margin-top: 30px; margin-bottom: 20px" />

      <h3>📌 Ajouter une mesure à un indicateur existant</h3>

      <v-select
        label="Indicateur existant"
        :items="indicateursExistants"
        item-title="nom"
        item-value="idIndicateurSession"
        v-model="selectedIndicateurId"
        required
      />

      <v-text-field
        label="Valeur"
        v-model="mesureExistante.valeur"
        type="number"
      />

      <v-text-field
        label="Date"
        v-model="mesureExistante.dateMesure"
        type="date"
      />

      <v-btn color="green" @click="ajouterValeurExistante"
        >✅ Ajouter la valeur</v-btn
      >
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
const sessionId = route.query.sessionId; //
import { onMounted } from "vue";

const selectedIndicateurId = ref(null);
const mesureExistante = ref({ valeur: "", dateMesure: "" });
const indicateursExistants = ref([]);

onMounted(() => {
  fetch("http://localhost:8989/api/indicateurSessions")
    .then((res) => res.json())
    .then((data) => {
      indicateursExistants.value = data;
    });
});

const ajouterValeurExistante = () => {
  if (
    !selectedIndicateurId.value ||
    !mesureExistante.value.valeur ||
    !mesureExistante.value.dateMesure
  ) {
    alert("Veuillez remplir tous les champs.");
    return;
  }

  const body = {
    valeur: parseFloat(mesureExistante.value.valeur),
    dateMesure: mesureExistante.value.dateMesure,
    indicateurSession: { idIndicateurSession: selectedIndicateurId.value },
    session: { idSession: parseInt(sessionId) },
  };

  fetch("http://localhost:8989/api/mesures", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  })
    .then((res) => {
      if (!res.ok) throw new Error("Erreur lors de l'ajout de la mesure");
      alert("✅ Valeur ajoutée à l’indicateur !");
      mesureExistante.value = { valeur: "", dateMesure: "" };
    })
    .catch((err) => {
      console.error(err);
      alert("❌ Une erreur est survenue : " + err.message);
    });
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
  if (
    !indicateur.value.nom ||
    !indicateur.value.unite ||
    !indicateur.value.categorie
  ) {
    alert("Remplis bien tous les champs !");
    return;
  }

  try {
    const bodyIndicateur = {
      nom: indicateur.value.nom,
      unite: indicateur.value.unite,
      date: new Date().toISOString().split("T")[0],
      categorie: { idCategorie: 1 },
      utilisateur: { idPersonne: 1 },
      session: sessionId ? { idSession: parseInt(sessionId) } : null,
    };

    console.log("Sending indicator data:", bodyIndicateur);

    const res = await fetch("http://localhost:8989/api/indicateurSessions", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(bodyIndicateur),
    });

    if (!res.ok) {
      const errorText = await res.text();
      console.error("Server response:", errorText);
      throw new Error(
        `Error creating indicator: ${res.status} ${res.statusText}`
      );
    }

    const created = await res.json();
    console.log("Created indicator:", created);

    if (sessionId) {
      fetch("http://localhost:8989/api/indicateurSessions")
        .then((res) => res.json())
        .then((data) => {
          indicateursExistants.value = data;
        });
    }

    alert("✅ Indicateur (et mesure si possible) créé !");
    router.push("/");
  } catch (err) {
    console.error("Erreur complète:", err);
    alert(`Une erreur s'est produite: ${err.message}`);
  }
};

const cancelForm = () => {
  router.push("/MesIndicateurs");
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

input {
  border: 1px solid black;
  border-radius: 4px;
  padding: 8px;
  font-size: 14px;
  width: 100%;
  box-sizing: border-box;
}

input:focus {
  border-color: #007bff;
  outline: none;
  box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
}
</style>
