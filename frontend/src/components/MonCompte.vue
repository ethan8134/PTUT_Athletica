<script>
import { ref, watch } from "vue";
import Utilisateur from "C:/Users/ethan_88l0bmt/Ptut-Athletica/frontend/src/Utilisateur.js";

export default {
  name: "MonCompte",
  props: {
    userData: {
      type: Object,
      required: false, // Rendre la prop facultative
    },
  },
  setup(props, { emit }) {
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
        : new Utilisateur(1, "Ali", "ali@example.com", "motdepasse", "Trail") // Utilisateur de test
    );

    const form = ref({
      idPersonne: utilisateur.value?.idPersonne || null,
      pseudo: utilisateur.value?.pseudo || "",
      email: utilisateur.value?.email || "",
      currentPassword: "",
      newPassword: "",
      preferences: utilisateur.value?.preferences || "",
    });

    // Surveille les changements de userData
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
        }
      },
      { immediate: true }
    );

    function toggleEditMode() {
      if (!form.value) {
        console.error("Formulaire non initialisé !");
        return;
      }
      isEditing.value = !isEditing.value;
    }

    function cancelEdit() {
      isEditing.value = false;
      form.value = {
        idPersonne: utilisateur.value.idPersonne,
        pseudo: utilisateur.value.pseudo,
        email: utilisateur.value.email,
        currentPassword: "",
        newPassword: "",
        preferences: utilisateur.value.preferences,
      };
    }

    async function saveChanges() {
      if (!form.value.idPersonne) {
        console.error("idPersonne est undefined !");
        alert("Impossible de sauvegarder : ID utilisateur manquant.");
        return;
      }

      const url = `http://localhost:8989/api/utilisateurs/${form.value.idPersonne}`;
      const payload = {
        pseudo: form.value.pseudo,
        email: form.value.email,
        mdp: form.value.newPassword || form.value.currentPassword,
        preferences: form.value.preferences,
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
    }

    return {
      form,
      isEditing,
      toggleEditMode,
      cancelEdit,
      saveChanges,
    };
  },
};
</script>

<template>
  <v-container class="account-page">
    <!-- Section Profil -->
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

    <!-- Formulaire de modification -->
    <v-form v-if="isEditing" ref="form" class="edit-form">
      <v-row>
        <v-col cols="12" md="6">
          <v-text-field
            label="Pseudo"
            v-model="form.pseudo"
            outlined
          ></v-text-field>
        </v-col>
        <v-col cols="12" md="6">
          <v-text-field
            label="Email"
            v-model="form.email"
            outlined
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="12" md="6">
          <v-text-field
            label="Mot de passe actuel"
            v-model="form.currentPassword"
            type="password"
            outlined
          ></v-text-field>
        </v-col>
        <v-col cols="12" md="6">
          <v-text-field
            label="Nouveau mot de passe"
            v-model="form.newPassword"
            type="password"
            outlined
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="12">
          <v-text-field
            label="Préférences"
            v-model="form.preferences"
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
<style scoped>
/* Structure générale */
.account-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* En-tête du profil */
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

/* Formulaire de modification */
.edit-form {
  margin-top: 20px;
  padding: 20px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}
</style>
