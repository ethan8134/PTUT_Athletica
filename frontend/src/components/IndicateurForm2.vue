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
        <button class="add-btn" type="submit">Valider</button>
        <button class="cancel-btn" type="button" @click="cancelForm">
          Passer cette étape
        </button>
      </div>
    </form>

    <div v-if="sessionId">
      <hr style="margin-top: 30px; margin-bottom: 20px" />

      <h3>Ajouter une mesure à un indicateur existant</h3>

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

      <div class="mt-3 text-caption text-grey">
        📅 La mesure sera enregistrée pour la date de la session sélectionnée.
      </div>

      <v-btn color="green" @click="ajouterValeurExistante">
        Ajouter la valeur
      </v-btn>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
const sessionId = route.query.sessionId;

const selectedIndicateurId = ref(null);
const mesureExistante = ref({ valeur: "" });
const indicateursExistants = ref([]);

onMounted(() => {
  // Vérification de l'ID de session dans l'URL
  fetch("http://localhost:8989/api/indicateurSessions") // Récupération des indicateurs existants
    .then((res) => res.json())
    .then((data) => {
      indicateursExistants.value = data;
    });
});

const ajouterValeurExistante = () => {
  // Fonction pour ajouter une valeur à un indicateur existant
  if (!selectedIndicateurId.value || !mesureExistante.value.valeur) {
    // Vérification des champs
    alert("Veuillez remplir tous les champs.");
    return;
  }

  const dateFromSession = new Date().toISOString().split("T")[0]; // Récupération de la date actuelle au format ISO

  const body = {
    valeur: parseFloat(mesureExistante.value.valeur),
    dateMesure: dateFromSession,
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
      alert("Valeur ajoutée à l’indicateur !");
      mesureExistante.value = { valeur: "" };
    })
    .catch((err) => {
      console.error(err);
      alert("Une erreur est survenue : " + err.message);
    });
};

const indicateur = ref({
  nom: "",
  unite: "",
  categorie: "",
});

const submitForm = async () => {
  // Fonction pour soumettre le formulaire
  if (
    !indicateur.value.nom ||
    !indicateur.value.unite ||
    !indicateur.value.categorie
  ) {
    // Vérification des champs
    alert("Veuillez remplir tous les champs.");
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

    const res = await fetch("http://localhost:8989/api/indicateurSessions", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(bodyIndicateur),
    });

    if (!res.ok) {
      const errorText = await res.text();
      throw new Error(
        `Erreur lors de la création de l'indicateur: ${res.status} ${res.statusText}\n${errorText}`
      );
    }

    await res.json();

    if (sessionId) {
      fetch("http://localhost:8989/api/indicateurSessions")
        .then((res) => res.json())
        .then((data) => {
          indicateursExistants.value = data;
        });
    }

    alert("Indicateur créé !");
    router.push("/");
  } catch (err) {
    console.error("Erreur:", err);
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
  background-color: #0e0c70;
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
