<template>
  <div class="indicateur-form-container">
    <h2>📝 Ajouter un indicateur</h2>
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
        <label>Valeur</label>
        <input v-model="indicateur.valeur" placeholder="Ex: 96" required />
      </div>
      <div class="form-group">
        <label>Unité</label>
        <input v-model="indicateur.unite" placeholder="Ex: bpm" required />
      </div>
      <div class="form-group">
        <label>Categorie</label>
        <input
          v-model="indicateur.categorie"
          placeholder="Ex: Indicateur de santé"
          required
        />
      </div>
      <div class="form-buttons">
        <button class="add-btn" type="submit">✅ Valider</button>
        <button class="cancel-btn" type="button" @click="cancelForm">
          ❌ Annuler
        </button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, defineEmits } from "vue";

const emit = defineEmits(["add-indicateur", "close-form"]);

const indicateur = ref({
  nom: "",
  valeur: "",
  unite: "",
  categorie: "",
});

const apiBaseUrl = "http://localhost:8989/api/indicateurs";

const submitForm = () => {
  if (
    !indicateur.value.nom ||
    !indicateur.value.valeur ||
    !indicateur.value.unite ||
    !indicateur.value.categorie
  ) {
    alert("Veuillez remplir tous les champs correctement.");
    return;
  }
  fetch(apiBaseUrl, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(indicateur.value),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Erreur lors de l'ajout de l'indicateur");
      }
      return response.json();
    })
    .then(() => {
      console.log("Indicateur ajouté avec succès");
      resetForm();
      // Vous pouvez rediriger l'utilisateur ou afficher un message de succès ici.
    })
    .catch((error) => {
      console.error("Erreur lors de l'ajout de l'indicateur:", error);
    });
};

const cancelForm = () => {
  resetForm();
  emit("close-form");
  // Ici, vous pouvez rediriger l'utilisateur vers la liste des indicateurs si nécessaire.
};

const resetForm = () => {
  indicateur.value = { nom: "", valeur: "", unite: "", categorie: "" };
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
</style>
