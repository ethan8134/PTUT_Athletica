<template>
  <div class="upload-container">
    <h2>Upload files</h2>
    <p>Select and upload the file of your choice</p>

    <!-- Zone de drag & drop -->
    <div
      class="drop-zone"
      @dragover.prevent
      @dragenter.prevent
      @drop.prevent="handleDrop"
    >
      <p>Choose a file or drag &amp; drop it here</p>
      <p class="formats">CSV, JSON formats, up to 50MB</p>
      <input
        class="file-input"
        type="file"
        ref="fileInput"
        @change="handleFileSelect"
        accept=".csv,.json"
      />
    </div>

    <!-- Bouton pour déclencher la sélection manuelle -->
    <button class="btn-secondary" @click="triggerFileSelect">
      Sélectionner un fichier
    </button>

    <!-- Bouton pour lancer l'import (upload) -->
    <button class="btn-primary" @click="handleFileUpload">Importer</button>

    <!-- Bouton pour rediriger vers le formulaire session -->
    <div class="divider"></div>
    <button class="btn-new-session" @click="redirectToSessionForm">
      Commencer un enregistrement vierge
    </button>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";

// Refs pour le fichier sélectionné et l'input
const fileInput = ref(null);
const selectedFile = ref(null);

// Pour la navigation
const router = useRouter();

/**
 * Ouvrir le sélecteur de fichier
 */
const triggerFileSelect = () => {
  fileInput.value.click();
};

/**
 * Gestion de la sélection via <input type="file">
 */
const handleFileSelect = (event) => {
  if (event.target.files && event.target.files[0]) {
    selectedFile.value = event.target.files[0];
    console.log("Fichier sélectionné :", selectedFile.value.name);
  }
};

/**
 * Gestion du drag & drop
 */
const handleDrop = (event) => {
  const file = event.dataTransfer.files[0];
  if (file) {
    selectedFile.value = file;
    console.log("Fichier déposé :", file.name);
  }
};

/**
 * Logique d'import (upload)
 */
const handleFileUpload = () => {
  if (!selectedFile.value) {
    alert("Aucun fichier sélectionné.");
    return;
  }
  // Exemple minimal : on log simplement le nom du fichier
  // Vous pouvez appeler votre API pour l'import
  console.log("Import du fichier :", selectedFile.value.name);
  alert(`Fichier "${selectedFile.value.name}" importé avec succès !`);
  // Réinitialiser
  selectedFile.value = null;
  fileInput.value.value = "";
};

/**
 * Redirection vers le formulaire Session
 */
const redirectToSessionForm = () => {
  // Ajustez la route si nécessaire, par exemple '/sessions/new'
  router.push("/AjouterSession");
};
</script>

<style scoped>
.upload-container {
  max-width: 500px;
  margin: 40px auto;
  padding: 20px 25px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.08);
  text-align: center;
  font-family: "Segoe UI", Tahoma, sans-serif;
}

.upload-container h2 {
  margin-bottom: 0.5rem;
  font-size: 1.3rem;
  color: #333;
}

.drop-zone {
  margin: 20px 0;
  padding: 30px;
  border: 2px dashed #ccc;
  border-radius: 8px;
  background: #f9f9f9;
  cursor: pointer;
  transition: background 0.3s ease-in-out;
}

.drop-zone:hover {
  background: #f0f0f0;
}

.drop-zone p {
  margin: 0.3rem 0;
  color: #666;
}

.formats {
  font-size: 0.9rem;
  color: #999;
}

.file-input {
  display: none;
}

.btn-secondary,
.btn-primary,
.btn-new-session {
  display: inline-block;
  margin: 8px;
  padding: 10px 16px;
  border: none;
  border-radius: 4px;
  font-weight: 500;
  cursor: pointer;
  font-size: 1rem;
}

/* Bouton pour sélectionner le fichier (bleu) */
.btn-secondary {
  background-color: #007fff;
  color: #fff;
}
.btn-secondary:hover {
  background-color: #005bb5;
}

/* Bouton pour importer (vert) */
.btn-primary {
  background-color: #4caf50;
  color: #fff;
}
.btn-primary:hover {
  background-color: #43a047;
}

/* Ligne de séparation */
.divider {
  margin: 20px 0;
  height: 1px;
  background: #ddd;
}

/* Bouton pour démarrer un enregistrement vierge (gris) */
.btn-new-session {
  background-color: #888;
  color: #fff;
}
.btn-new-session:hover {
  background-color: #666;
}
</style>
