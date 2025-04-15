<template>
  <v-container class="account-page">
    <h2>Mon Compte</h2>

    <div v-if="!isEditing">
      <p><strong>Pseudo :</strong> {{ userData.pseudo }}</p>
      <p><strong>Email :</strong> {{ userData.email }}</p>
      <p><strong>Mot de passe :</strong> ******</p>
      <p><strong>Préférences :</strong> {{ userData.preferences }}</p>
      <div class="form-actions">
        <v-btn color="primary" @click="enterEditMode">Modifier</v-btn>
      </div>
    </div>

    <v-form v-else>
      <v-text-field
        v-model="editableData.pseudo"
        label="Pseudo"
        outlined
        dense
      ></v-text-field>
      <v-text-field
        v-model="editableData.email"
        label="Email"
        outlined
        dense
      ></v-text-field>
      <v-text-field
        v-model="editableData.mdp"
        label="Mot de passe"
        type="password"
        outlined
        dense
      ></v-text-field>
      <v-text-field
        v-model="editableData.preferences"
        label="Préférences"
        outlined
        dense
      ></v-text-field>
      <div class="form-actions">
        <v-btn color="primary" @click="applyChanges">Appliquer</v-btn>
        <v-btn color="error" outlined @click="cancelEdit">Annuler</v-btn>
      </div>
    </v-form>
  </v-container>
</template>

<script setup>
import { ref } from "vue";

const userData = ref({
  // Initialisation des données utilisateur
  pseudo: "Ymir",
  email: "test@user.com",
  mdp: "password",
  preferences: "Course à pied",
});

const editableData = ref({ ...userData.value }); // Copie des données utilisateur pour l'édition
const isEditing = ref(false); // État d'édition

const enterEditMode = () => {
  editableData.value = { ...userData.value };
  isEditing.value = true;
};

const applyChanges = () => {
  userData.value = { ...editableData.value }; // Applique les modifications
  isEditing.value = false; // Quitte le mode édition
  alert("Modifications appliquées localement !");
};

const cancelEdit = () => {
  isEditing.value = false;
  alert("Modifications annulées !");
};
</script>

<style scoped>
.account-page {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

h2 {
  margin-bottom: 20px;
  text-align: center;
}

p {
  margin: 10px 0;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}
</style>
