<template>
  <div class="indicateur-form-container">
    <h2>✨ Ajouter un indicateur</h2>
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
          Annuler
        </button>
        <button
          class="associate-btn"
          type="button"
          @click="showAssocierDialog = true"
        >
          Associer à une session
        </button>
      </div>
    </form>

    <v-dialog v-model="showAssocierDialog" max-width="500px">
      <v-card>
        <v-card-title>Associer à une session</v-card-title>
        <v-card-text>
          <v-select
            label="Session"
            :items="sessions"
            item-title="nom"
            item-value="idSession"
            v-model="selectedSessionId"
            required
          />

          <v-text-field
            label="Valeur"
            v-model="mesureExistante.valeur"
            type="number"
            required
          />

          <p class="text-caption mt-2">
            📅 La date de la session sera automatiquement utilisée.
          </p>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="showAssocierDialog = false">Fermer</v-btn>
          <v-btn color="green" @click="ajouterIndicateurEtMesure"
            >Ajouter la valeur</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const showAssocierDialog = ref(false);
const selectedSessionId = ref(null);
const mesureExistante = ref({ valeur: "" });
const sessions = ref([]);

const indicateur = ref({ nom: "", unite: "", categorie: "" }); // Définition de l'indicateur

onMounted(() => {
  // Récupération des sessions
  fetch("http://localhost:8989/api/sessions")
    .then((res) => res.json())
    .then((data) => (sessions.value = data));
});

const submitForm = async () => {
  if (
    !indicateur.value.nom ||
    !indicateur.value.unite ||
    !indicateur.value.categorie
  ) {
    alert("Veuillez remplir tous les champs.");
    return;
  }

  const body = {
    nom: indicateur.value.nom,
    unite: indicateur.value.unite,
    date: new Date().toISOString().split("T")[0],
    categorie: { idCategorie: 1 },
    utilisateur: { idPersonne: 1 },
  };

  const res = await fetch("http://localhost:8989/api/indicateurSessions", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });

  if (!res.ok) {
    const errorText = await res.text();
    console.error(errorText);
    alert("Erreur lors de la création de l'indicateur");
    return;
  }

  alert("Indicateur créé avec succès !");
  router.push("/MesIndicateurs"); // Redirection vers la page des indicateurs
};

const cancelForm = () => {
  router.push("/MesIndicateurs"); // Redirection vers la page des indicateurs
};

const ajouterIndicateurEtMesure = async () => {
  // Fonction pour ajouter l'indicateur et la mesure
  if (!selectedSessionId.value || !mesureExistante.value.valeur) {
    // Vérification des champs
    alert("Veuillez remplir tous les champs.");
    return;
  }

  const selectedSession = sessions.value.find(
    // Vérification de la session
    (s) => s.idSession === selectedSessionId.value // Récupération de la session sélectionnée
  );
  if (!selectedSession) {
    // Vérification de l'existence de la session
    alert("Session introuvable.");
    return;
  }

  // Créer l’indicateur
  const indicateurBody = {
    nom: indicateur.value.nom,
    unite: indicateur.value.unite,
    date: selectedSession.dateSession,
    categorie: { idCategorie: 1 },
    utilisateur: { idPersonne: 1 },
  };

  try {
    const resIndicateur = await fetch(
      "http://localhost:8989/api/indicateurSessions",
      {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(indicateurBody),
      }
    );

    if (!resIndicateur.ok) {
      const msg = await resIndicateur.text();
      throw new Error(msg);
    }

    const indicateurCree = await resIndicateur.json();

    // Ajouter la mesure liée
    const mesureBody = {
      valeur: parseFloat(mesureExistante.value.valeur),
      dateMesure: selectedSession.dateSession,
      indicateurSession: {
        idIndicateurSession: indicateurCree.idIndicateurSession,
      },
      session: { idSession: selectedSessionId.value },
    };

    const resMesure = await fetch("http://localhost:8989/api/mesures", {
      // Créer la mesure
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(mesureBody),
    });

    if (!resMesure.ok) {
      const err = await resMesure.text();
      throw new Error(err);
    }

    alert("Indicateur et mesure ajoutés avec succès !");
    showAssocierDialog.value = false; // Fermer la boîte de dialogue
    mesureExistante.value = { valeur: "" }; // Réinitialiser la valeur de la mesure
  } catch (err) {
    console.error(err);
    alert("Erreur lors de l’ajout : " + err.message);
  }
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
  margin-top: 20px;
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
.associate-btn {
  background-color: #007bff;
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
.text-caption {
  color: #555;
  font-size: 0.9rem;
}
</style>
