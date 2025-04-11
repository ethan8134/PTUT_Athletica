<template>
  <v-container class="account-page">
    <div class="profile-header">
      <v-avatar size="80">
        <img src="https://via.placeholder.com/80" alt="Avatar utilisateur" />
      </v-avatar>
      <div class="profile-info">
        <h2>{{ form.pseudo }}</h2>
        <p>{{ form.email }}</p>
      </div>
      <v-btn class="edit-btn" color="primary" @click="toggleEditMode">
        {{ isEditing ? "Annuler" : "Modifier" }}
      </v-btn>
    </div>

    <div v-if="!isEditing" class="static-info">
      <p><strong>Nom complet :</strong> {{ form.pseudo }}</p>
      <p><strong>Email :</strong> {{ form.email }}</p>
      <p><strong>Préférences :</strong> {{ form.preferences }}</p>
    </div>

    <v-form v-else ref="form" class="edit-form">
      <v-row>
        <v-col cols="12" md="6">
          <v-text-field
            label="Pseudo"
            v-model="editForm.pseudo"
            outlined
          ></v-text-field>
        </v-col>
        <v-col cols="12" md="6">
          <v-text-field
            label="Email"
            v-model="editForm.email"
            outlined
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="12">
          <v-text-field
            label="Préférences"
            v-model="editForm.preferences"
            outlined
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="12" md="6">
          <v-text-field
            label="Mot de passe actuel"
            v-model="editForm.currentPassword"
            type="password"
            outlined
          ></v-text-field>
        </v-col>
        <v-col cols="12" md="6">
          <v-text-field
            label="Nouveau mot de passe"
            v-model="editForm.newPassword"
            type="password"
            outlined
          ></v-text-field>
        </v-col>
      </v-row>
      <div class="form-actions">
        <v-btn color="error" outlined @click="cancelEdit">Annuler</v-btn>
        <v-btn color="success" @click="saveChanges">Sauvegarder</v-btn>
      </div>
    </v-form>
  </v-container>
</template>

<script>
import { ref, watch } from "vue";
import Utilisateur from "C:/Users/ethan_88l0bmt/Ptut-Athletica/frontend/src/Utilisateur.js";

export default {
  name: "MonCompte",
  props: {
    userData: {
      type: Object,
      required: false,
    },
  },
  setup(props) {
    const isEditing = ref(false);

    const utilisateur = ref(
      props.userData
        ? new Utilisateur(
            props.userData.idPersonne,
            props.userData.pseudo,
            props.userData.email,
            props.userData.mdp,
            props.userData.preferences
          )
        : new Utilisateur(1, "Ali", "ali@example.com", "motdepasse", "Trail")
    );

    const form = ref({
      idPersonne: utilisateur.value?.idPersonne || null,
      pseudo: utilisateur.value?.pseudo || "",
      email: utilisateur.value?.email || "",
      currentPassword: "",
      newPassword: "",
      preferences: utilisateur.value?.preferences || "",
    });

    const editForm = ref({ ...form.value });

    watch(
      () => props.userData,
      (newValue) => {
        if (newValue) {
          utilisateur.value = new Utilisateur(
            newValue.idPersonne,
            newValue.pseudo,
            newValue.email,
            newValue.mdp,
            newValue.preferences
          );
          form.value = {
            idPersonne: utilisateur.value.idPersonne,
            pseudo: utilisateur.value.pseudo,
            email: utilisateur.value.email,
            currentPassword: "",
            newPassword: "",
            preferences: utilisateur.value.preferences,
          };
          editForm.value = { ...form.value };
        }
      },
      { immediate: true }
    );

    function toggleEditMode() {
      console.log("Avant basculement, form :", form.value);
      console.log("Avant basculement, editForm :", editForm.value);

      if (!form.value) {
        console.error("Formulaire non initialisé !");
        form.value = {
          idPersonne: utilisateur.value?.idPersonne || null,
          pseudo: utilisateur.value?.pseudo || "",
          email: utilisateur.value?.email || "",
          currentPassword: "",
          newPassword: "",
          preferences: utilisateur.value?.preferences || "",
        };
        editForm.value = { ...form.value };
        console.log("Formulaire réinitialisé :", form.value);
      }

      if (isEditing.value) {
        editForm.value = { ...form.value };
      }
      isEditing.value = !isEditing.value;

      console.log("Après basculement, form :", form.value);
      console.log("Après basculement, editForm :", editForm.value);
    }

    function cancelEdit() {
      isEditing.value = false;
      editForm.value = { ...form.value };
      console.log("Formulaire annulé, données actuelles :", form.value);
    }

    async function saveChanges() {
      if (!editForm.value.idPersonne) {
        console.error("idPersonne est undefined !");
        alert("Impossible de sauvegarder : ID utilisateur manquant.");
        return;
      }

      const url = `http://localhost:8989/api/utilisateurs/${editForm.value.idPersonne}`;
      const payload = {
        pseudo: editForm.value.pseudo,
        email: editForm.value.email,
        mdp: editForm.value.newPassword || editForm.value.currentPassword,
        preferences: editForm.value.preferences,
      };

      try {
        const response = await fetch(url, {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(payload),
        });

        if (!response.ok) {
          throw new Error("Erreur lors de la sauvegarde");
        }

        const data = await response.json();
        console.log("Modifications sauvegardées :", data);

        form.value = { ...editForm.value };
        utilisateur.value = new Utilisateur(
          form.value.idPersonne,
          form.value.pseudo,
          form.value.email,
          form.value.newPassword || form.value.currentPassword,
          form.value.preferences
        );

        isEditing.value = false;
        alert("Modifications sauvegardées avec succès !");
      } catch (error) {
        console.error("Erreur lors de la requête :", error);
        alert("Une erreur est survenue lors de la sauvegarde.");
      }

      form.value = { ...editForm.value };
      console.log("Formulaire sauvegardé, données actuelles :", form.value);
    }

    return {
      form,
      editForm,
      isEditing,
      toggleEditMode,
      cancelEdit,
      saveChanges,
    };
  },
};
</script>

<style scoped>
.account-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.profile-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 30px;
  padding: 20px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.profile-info {
  flex: 1;
  margin-left: 20px;
}

.profile-info h2 {
  margin: 0;
  font-size: 1.5rem;
  font-weight: bold;
}

.profile-info p {
  margin: 5px 0 0;
  color: #666;
}

.edit-btn {
  margin-left: auto;
}

.edit-form {
  margin-top: 20px;
  padding: 20px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.static-info p {
  margin: 10px 0;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}
</style>
