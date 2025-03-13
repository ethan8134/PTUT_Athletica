<template>
  <div>
    <FileUpload />
    <v-list-item to="/AjouterSession" class="redirect-btn">
      <v-list-item-title>Commencer un enregistrement vierge</v-list-item-title>
    </v-list-item>
  </div>
</template>

<script setup>
import FileUpload from "@/components/FileUpload.vue";
</script>

<style scoped></style>
