<template>
  <v-card class="pa-4">
    <v-card-title class="d-flex align-center">
      {{ indicateur.nom }} – {{ indicateur.valeur }} {{ indicateur.unite }}
      <v-spacer></v-spacer>
      <v-btn small color="primary" @click="dialog = true">
        Ajouter mesure
      </v-btn>
    </v-card-title>

    <!-- Dialogue pour ajouter une nouvelle mesure -->
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
import { ref } from 'vue';

// Tu peux recevoir l'indicateur en prop depuis le parent
const props = defineProps({
  indicateur: {
    type: Object,
    required: true
  }
});

const dialog = ref(false);
const newMesure = ref('');

// Fonction pour envoyer la nouvelle mesure à l'API
const submitMesure = () => {
  if (!newMesure.value || isNaN(newMesure.value)) {
    alert("Veuillez saisir une valeur numérique valide.");
    return;
  }

  const body = {
    valeur: parseFloat(newMesure.value),
    dateMesure: new Date().toISOString().split('T')[0],
    indicateurSession: { idIndicateurSession: props.indicateur.idIndicateurSession }
    // Tu peux ajouter d'autres champs si nécessaire
  };

  fetch('http://localhost:8989/api/mesures', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(body)
  })
    .then(res => {
      if (!res.ok) throw new Error("Erreur lors de l'ajout de la mesure");
      return res.json();
    })
    .then(data => {
      console.log("Mesure ajoutée :", data);
      dialog.value = false;
      newMesure.value = '';
      // Optionnel : déclencher une mise à jour du parent pour rafraîchir les mesures affichées
    })
    .catch(err => {
      console.error(err);
      alert("Une erreur est survenue lors de l'ajout de la mesure.");
    });
};
</script>

<style scoped>
/* Tu peux ajuster le style selon ton design */
</style>
