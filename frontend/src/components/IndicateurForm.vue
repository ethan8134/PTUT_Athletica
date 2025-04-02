<template>
  <div class="indicateur-form-container">
    <h2>📝 Ajouter un indicateur</h2>
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

      <!-- Champ Type amélioré -->
      <div class="form-group">
        <label>Type</label>
        <select v-model="indicateur.type" required>
          <option disabled value="">Sélectionner un type</option>
          <option value="global">Indicateur Global</option>
          <option value="session">Indicateur Session</option>
        </select>
      </div>
      <!-- Si indicateur session, on propose d’ajouter une 1ère mesure -->
      <div v-if="indicateur.type === 'session'">
        <div class="form-group">
          <label>Valeur initiale</label>
          <input v-model="mesure.valeur" type="number" placeholder="Ex: 12.5" />
        </div>
        <div class="form-group">
          <label>Date de la mesure</label>
          <input v-model="mesure.dateMesure" type="date" />
        </div>
      </div>


      <div class="form-buttons">
        <button class="add-btn" type="submit">✅ Valider</button>
        <button class="cancel-btn" type="button" @click="cancelForm"> Annuler</button>
      </div>
    </form>
  </div>
</template>
<script setup>

import { ref, defineEmits } from "vue";
const mesure = ref({
  valeur: '',
  dateMesure: '',
});

const emit = defineEmits(["add-indicateur", "close-form"]);

const indicateur = ref({
  nom: "",
  unite: "",
  categorie: "",
  type: "", // Nouveau champ type
});

const apiBaseUrl = "http://localhost:8989/api/indicateurs";

const submitForm = () => {
  if (
    !indicateur.value.nom ||
    !indicateur.value.unite ||
    !indicateur.value.categorie ||
    !indicateur.value.type
  ) {
    alert("Veuillez remplir tous les champs correctement.");
    return;
  }

  let apiUrl = "";
  if (indicateur.value.type === "global") {
    apiUrl = "http://localhost:8989/api/indicateurGlobals";
  } else if (indicateur.value.type === "session") {
    apiUrl = "http://localhost:8989/api/indicateurSessions";
  }

  const bodyToSend = {
    nom: indicateur.value.nom,
    unite: indicateur.value.unite,
    date: new Date().toISOString().split('T')[0],
    categorie: { idCategorie: 1 },
    utilisateur: { idPersonne: 1 }
  };

  fetch(apiUrl, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(bodyToSend),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Erreur lors de l'ajout");
      }
      return response.json();
    })
    .then((createdIndicateur) => {
      if (
        indicateur.value.type === "session" &&
        mesure.value.valeur &&
        mesure.value.dateMesure
      ) {
        const mesureBody = {
          valeur: parseFloat(mesure.value.valeur),
          dateMesure: mesure.value.dateMesure,
          indicateurSession: {
            idIndicateurSession: createdIndicateur.idIndicateurSession,
          },
        };

        return fetch("http://localhost:8989/api/mesures", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(mesureBody),
        }).then((res) => {
          if (!res.ok) throw new Error("Erreur lors de l'ajout de la mesure");
          return res.json();
        });
      }
    })
    .then(() => {
      alert("Indicateur (et mesure si présente) ajouté !");
      resetForm();
    })
    .catch((error) => {
      console.error("Erreur :", error);
    });
};


const cancelForm = () => {
  emit("close-form");
};

</script>


<style scoped>
.indicateur-form-container {
  max-width: 600px;
  margin: 20px auto;
  padding: 20px;
  background-color: lightgrey;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.indicateur-form-container h2 {
  text-align: center;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
  display: flex;
  flex-direction: column;
}

.form-group label {
  margin-bottom: 5px;
  font-weight: bold;
  color: #333;
}

.form-group input {
  padding: 10px;
  border: 1px solid black;
  border-radius: 4px;
  font-size: 16px;
}

.form-buttons {
  display: flex;
  gap: 10px;
  justify-content: space-between;
  margin-top: 20px;
}

.add-btn,
.cancel-btn {
  flex: 1;
  padding: 12px;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}

.add-btn {
  background-color: #28a745;
  color: white;
}

.add-btn:hover {
  background-color: #218838;
}

.cancel-btn {
  background-color: #dc3545;
  color: white;
}

.cancel-btn:hover {
  background-color: #c82333;
}
.form-group select {
  padding: 10px;
  border: 1px solid black;
  border-radius: 4px;
  font-size: 16px;
}

</style>
