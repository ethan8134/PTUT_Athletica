<template>
  <div class="session-form-container">
    <h2>Ajouter une session</h2>
    <form @submit.prevent="submitForm">
      <!-- Champs de la session -->
      <div class="form-group">
        <label>Nom de la session</label>
        <input
          v-model="session.nom"
          placeholder="Ex: Session Running"
          required
        />
      </div>
      <div class="form-group">
        <label>Date</label>
        <input type="date" v-model="session.date" required />
      </div>

      <!-- Section Indicateur associé -->
      <div class="indicator-section">
        <h3>Indicateur associé</h3>
        <div v-if="!showIndicatorOptions && !showIndicatorForm">
          <div class="indicator-buttons">
            <button
              type="button"
              class="btn-secondary"
              @click="toggleIndicatorOptions"
            >
              Choisir un indicateur existant
            </button>
            <button
              type="button"
              class="btn-secondary"
              @click="openIndicatorForm"
            >
              Créer un nouvel indicateur
            </button>
          </div>
          <div
            v-if="session.indicateur && session.indicateur.nom"
            class="selected-indicator"
          >
            <p>Indicateur sélectionné :</p>
            <div class="indicator-fields">
              <input
                v-model="session.indicateur.nom"
                placeholder="Nom de l'indicateur"
              />
              <input v-model="session.indicateur.valeur" placeholder="Valeur" />
              <input v-model="session.indicateur.unite" placeholder="Unité" />
              <input
                v-model="session.indicateur.categorie"
                placeholder="Catégorie"
              />
            </div>
          </div>
        </div>
        <div v-else-if="showIndicatorOptions" class="indicator-options">
          <h4>Choisissez un indicateur</h4>
          <ul class="indicator-list">
            <li v-for="ind in existingIndicators" :key="ind.id">
              <button
                type="button"
                class="btn-secondary"
                @click="selectIndicator(ind)"
              >
                {{ ind.nom }}
              </button>
            </li>
          </ul>
          <button
            type="button"
            class="btn-cancel"
            @click="handleAnnulerIndicator"
          >
            Annuler
          </button>
        </div>
      </div>

      <!-- Boutons du formulaire -->
      <div class="form-buttons">
        <button class="btn-primary" type="submit">Valider</button>
        <button class="btn-cancel" type="button" @click="cancelForm">
          Annuler
        </button>
      </div>
    </form>

    <!-- Intégration locale du formulaire d'indicateur -->
    <IndicateurForm
      v-if="showIndicatorForm"
      @add-indicateur="handleIndicatorCreated"
      @close-form="closeIndicatorForm"
    />
  </div>
</template>

<script setup>
import { ref } from "vue";
import IndicateurForm from "./IndicateurForm.vue"; // Ajustez le chemin si nécessaire

// Données et méthodes (inchangées)
const session = ref({
  nom: "",
  date: "",
  indicateur: { nom: "", valeur: "", unite: "", categorie: "" },
});

const apiSessionUrl = "http://localhost:8989/api/sessions";
const apiIndicateurUrl = "http://localhost:8989/api/indicateurs";

const showIndicatorOptions = ref(false);
const existingIndicators = ref([]);
const showIndicatorForm = ref(false);

const toggleIndicatorOptions = () => {
  showIndicatorOptions.value = !showIndicatorOptions.value;
  if (showIndicatorOptions.value) {
    fetchExistingIndicators();
  }
};

const fetchExistingIndicators = () => {
  fetch(apiIndicateurUrl)
    .then((response) => response.json())
    .then((data) => {
      existingIndicators.value = data;
    })
    .catch((error) => {
      console.error("Erreur lors de la récupération des indicateurs:", error);
    });
};

const selectIndicator = (ind) => {
  session.value.indicateur = { ...ind };
  showIndicatorOptions.value = false;
};

const openIndicatorForm = () => {
  showIndicatorForm.value = true;
};

const closeIndicatorForm = () => {
  showIndicatorForm.value = false;
};

const handleIndicatorCreated = (createdIndicator) => {
  session.value.indicateur = { ...createdIndicator };
  showIndicatorForm.value = false;
};

const submitForm = () => {
  if (!session.value.nom || !session.value.date) {
    alert("Veuillez remplir tous les champs de la session correctement.");
    return;
  }
  fetch(apiSessionUrl, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(session.value),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Erreur lors de l'ajout de la session");
      }
      return response.json();
    })
    .then(() => {
      console.log("Session ajoutée avec succès");
      resetForm();
    })
    .catch((error) => {
      console.error("Erreur lors de l'ajout de la session:", error);
    });
};

const handleAnnulerIndicator = () => {
  if (showIndicatorForm.value) {
    // Si le formulaire d'indicateur est ouvert, on le ferme
    closeIndicatorForm();
  } else if (showIndicatorOptions.value) {
    // Sinon, si la liste des indicateurs existants est ouverte, on la ferme
    toggleIndicatorOptions();
  }
};

const cancelForm = () => {
  resetForm();
};

const resetForm = () => {
  session.value = {
    nom: "",
    date: "",
    indicateur: { nom: "", valeur: "", unite: "", categorie: "" },
  };
  showIndicatorOptions.value = false;
  showIndicatorForm.value = false;
};
</script>

<style scoped>
/* Conteneur principal */
.session-form-container {
  max-width: 500px;
  margin: 40px auto;
  padding: 25px 30px;
  background-color: lightgray;
  border-radius: 8px;
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.08);
  font-family: "Segoe UI", Tahoma, sans-serif;
}

/* Titre */
.session-form-container h2 {
  text-align: center;
  margin-bottom: 1.5rem;
  font-size: 1.3rem;
  color: #333;
}

/* Groupes de champs */
.form-group {
  margin-bottom: 1rem;
  display: flex;
  flex-direction: column;
}

.form-group label {
  margin-bottom: 0.4rem;
  font-weight: 500;
  color: #555;
}

.form-group input {
  padding: 0.6rem;
  border: 1px solid black;
  border-radius: 4px;
  font-size: 1rem;
  color: #333;
}

.form-group input::placeholder {
  color: #999;
}

/* Section indicateur */
.indicator-section {
  margin-bottom: 1.2rem;
  padding: 0.8rem;
  border-radius: 4px;
}

.indicator-section h3 {
  padding: 20px;
  margin: 10px;
  font-size: 1.1rem;
  color: #444;
}

.indicator-buttons {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 0.8rem;
}

.indicator-options {
  margin-top: 0.5rem;
  background-color: lightgray;
  border: 1px solid black;
  padding: 0.6rem;
  border-radius: 4px;
}

.indicator-list {
  background-color: lightgray;
  list-style: none;
  padding: 0;
  margin: 0.5rem 0;
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.indicator-list li {
  flex: 1 1 calc(50% - 0.5rem);
}

.selected-indicator p {
  margin-bottom: 0.5rem;
  color: #007fff;
  font-weight: 500;
}

.indicator-fields input {
  display: block;
  margin-bottom: 0.5rem;
  width: 100%;
}

/* Boutons */
.form-buttons {
  display: flex;
  gap: 1rem;
  justify-content: center;
  margin-top: 1rem;
}

/* Boutons génériques */
.btn-primary,
.btn-cancel,
.btn-secondary {
  padding: 0.6rem 1rem;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  font-weight: 500;
}

/* Bouton principal (Valider) */
.btn-primary {
  margin-left: auto;
  background-color: #4caf50;
  color: #fff;
}

.btn-primary:hover {
  background-color: #43a047;
}

/* Bouton annuler (Rouge) */
.btn-cancel {
  margin-right: auto;
  background-color: #f44336;
  color: #fff;
}

.btn-cancel:hover {
  background-color: #e53935;
}

/* Bouton secondaire (Bleu) */
.btn-secondary {
  background-color: #007fff;
  color: #fff;
}

.btn-secondary:hover {
  background-color: #005bb5;
}
</style>
