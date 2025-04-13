<template>
  <v-card class="pa-4">
    <v-card-title class="d-flex align-center">
      {{ indicateur.nom }} – {{ indicateur.valeur }} {{ indicateur.unite }}
      <v-spacer></v-spacer>
      <v-btn small color="primary" @click="dialog = true">
        Ajouter mesure
      </v-btn>
    </v-card-title>

    <v-dialog v-model="dialog" max-width="400">
      <v-card>
        <v-card-title>Ajouter une nouvelle mesure</v-card-title>
        <v-card-text>
          <v-text-field v-model="newMesure" label="Valeur" type="number" />
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red" text @click="dialog = false">Annuler</v-btn>
          <v-btn color="green" text @click="submitMesure">Valider</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-card>
</template>

<script setup>
import { ref } from "vue";

const props = defineProps({
  indicateur: {
    type: Object,
    required: true,
  },
});

const dialog = ref(false);
const newMesure = ref("");

const submitMesure = () => {
  // Vérifie si la valeur est valide
  if (!newMesure.value || isNaN(newMesure.value)) {
    // Vérifie si la valeur est vide ou non numérique
    alert("Veuillez saisir une valeur numérique valide.");
    return;
  }

  const body = {
    // Crée le corps de la requête
    valeur: parseFloat(newMesure.value), // Convertit la valeur en nombre flottant
    dateMesure: new Date().toISOString().split("T")[0], // Récupère la date actuelle au format ISO
    indicateurSession: {
      // Définit l'indicateur de session
      idIndicateurSession: props.indicateur.idIndicateurSession, // Récupère l'ID de l'indicateur de session
    },
  };

  fetch("http://localhost:8989/api/mesures", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  })
    .then((res) => {
      if (!res.ok) throw new Error("Erreur lors de l'ajout de la mesure");
      return res.json();
    })
    .then((data) => {
      console.log("Mesure ajoutée :", data);
      dialog.value = false; // Ferme la boîte de dialogue
      newMesure.value = ""; // Réinitialise la valeur de la mesure
    })
    .catch((err) => {
      console.error(err);
      alert("Une erreur est survenue lors de l'ajout de la mesure.");
    });
};
</script>

<style scoped></style>
