<template>
  <v-container class="button-container">
    <v-list-item to="/UploadPage" class="redirect-btn">
      <v-list-item-title>Enregistrer une nouvelle session</v-list-item-title>
    </v-list-item>
    <v-list-item to="/Sessions" class="redirect-btn">
      <v-list-item-title>Consulter mes Sessions</v-list-item-title>
    </v-list-item>
  </v-container>
</template>

<script setup></script>

<style scoped>
.button-container {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
  margin: 0;
  gap: 16px;
}

.redirect-btn {
  display: flex;
  width: 300px;
  height: 170px;
  justify-content: center;
  padding: 8px 16px;
  background-color: #0e0c70;
  color: #ccc;
  text-align: center;
  font-weight: bold;
  text-decoration: none;
  transition: background-color 0.3s ease;
  border-radius: 150px;
}

.redirect-btn:hover {
  background-color: #005bb5;
}
</style>
