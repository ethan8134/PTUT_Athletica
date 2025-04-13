<template>
  <div class="session-form-container">
    <h2>Ajouter une session</h2>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label>Nom de la session</label>
        <input v-model="session.nom" required />
      </div>
      <div class="form-group">
        <label>Date</label>
        <input type="date" v-model="session.dateSession" required />
      </div>
      <div class="form-buttons">
        <button class="btn-primary" type="submit">Valider</button>
        <button class="btn-cancel" type="button" @click="cancelForm">
          Annuler
        </button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

const session = ref({
  nom: "",
  dateSession: "",
  utilisateur: { idPersonne: 1 },
});

const apiSessionUrl = "http://localhost:8989/api/sessions";

const submitForm = () => {
  // Soumettre le formulaire
  if (!session.value.nom || !session.value.dateSession) {
    // Vérifier si les champs sont remplis
    alert("Veuillez remplir tous les champs de la session.");
    return;
  }

  fetch(apiSessionUrl, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(session.value),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Erreur lors de la création de la session");
      }
      return response.json();
    })
    .then((createdSession) => {
      console.log("Session créée :", createdSession);
      router.push({
        path: "/AjouterIndicateur",
        query: { sessionId: createdSession.idSession },
      });
    })
    .catch((error) => {
      console.error("Erreur lors de la création de la session:", error);
    });
};

const cancelForm = () => {
  // Fermer le formulaire
  session.value = { nom: "", dateSession: "", utilisateur: { idPersonne: 1 } }; // Réinitialiser le formulaire
};
</script>

<style scoped>
.session-form-container {
  max-width: 500px;
  margin: 40px auto;
  padding: 25px 30px;
  background-color: lightgray;
  border-radius: 8px;
}
.form-group {
  margin-bottom: 1rem;
  display: flex;
  flex-direction: column;
}
.form-buttons {
  display: flex;
  justify-content: space-between;
  margin-top: 1rem;
}
.btn-primary {
  background-color: #4caf50;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
}
.btn-cancel {
  background-color: #f44336;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
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
