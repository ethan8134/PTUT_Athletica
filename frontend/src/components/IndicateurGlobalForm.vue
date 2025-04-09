<template>
  <div class="indicateur-global-form">
    <h2>📊 Ajouter un indicateur global</h2>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label>Nom de l'indicateur</label>
        <input v-model="indicateur.nom" placeholder="Ex: Poids" required />
      </div>

      <div class="form-group">
        <label>Unité</label>
        <input v-model="indicateur.unite" placeholder="Ex: kg" required />
      </div>

      <div class="form-group">
        <label>Date</label>
        <input type="date" v-model="indicateur.date" required />
      </div>

      <div class="form-buttons">
        <button class="btn-primary" type="submit">✅ Valider</button>
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

const indicateur = ref({
  nom: "",
  unite: "",
  date: "",
});

const submitForm = async () => {
  if (
    !indicateur.value.nom ||
    !indicateur.value.unite ||
    !indicateur.value.date
  ) {
    alert("Veuillez remplir tous les champs.");
    return;
  }

  const body = {
    nom: indicateur.value.nom,
    unite: indicateur.value.unite,
    date: indicateur.value.date,
    utilisateur: { idPersonne: 1 },
  };

  try {
    const res = await fetch("http://localhost:8989/api/indicateurGlobals", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
    });

    if (!res.ok) throw new Error("Erreur lors de la création");
    alert("✅ Indicateur global créé !");
    router.push("/");
  } catch (err) {
    console.error(err);
    alert("❌ Une erreur s’est produite.");
  }
};

const cancelForm = () => {
  router.push("/MesIndicateurs");
};
</script>

<style scoped>
.indicateur-global-form {
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
.btn-primary {
  background-color: #28a745;
  color: white;
  padding: 10px;
  border: none;
  border-radius: 4px;
}
.btn-cancel {
  background-color: #dc3545;
  color: white;
  padding: 10px;
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
