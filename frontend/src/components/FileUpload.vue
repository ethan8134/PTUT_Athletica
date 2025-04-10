<template>
  <div class="upload-container">
    <h2>Upload files</h2>
    <p>Select and upload the file of your choice</p>

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

    <button class="btn-secondary" @click="triggerFileSelect">
      Sélectionner un fichier
    </button>

    <button class="btn-primary" @click="handleFileUpload">Importer</button>

    <div class="divider"></div>
    <button class="btn-new-session" @click="$emit('start-session')">
      Commencer un enregistrement vierge
    </button>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";

const fileInput = ref(null);
const selectedFile = ref(null);
const showSessionForm = ref(false);
const router = useRouter();

const triggerFileSelect = () => {
  fileInput.value.click();
};

const handleFileSelect = (event) => {
  if (event.target.files && event.target.files[0]) {
    selectedFile.value = event.target.files[0];
    console.log("Fichier sélectionné :", selectedFile.value.name);
  }
};

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
const handleFileUpload = async () => {
  if (!selectedFile.value) {
    alert("Aucun fichier sélectionné.");
    return;
  }

  const formData = new FormData();
  formData.append("file", selectedFile.value);

  try {
    const response = await fetch("http://localhost:8989/api/import", {
      method: "POST",
      body: formData
    });

    if (!response.ok) throw new Error("Erreur lors de l'import");

    alert(`✅ Fichier "${selectedFile.value.name}" importé avec succès !`);

    // Reset
    selectedFile.value = null;
    fileInput.value.value = "";
  } catch (err) {
    console.error("Erreur import :", err);
    alert("❌ Une erreur est survenue pendant l'import.");
  }
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

.btn-secondary {
  background-color: #007fff;
  color: #fff;
}
.btn-secondary:hover {
  background-color: #005bb5;
}

.btn-primary {
  background-color: #4caf50;
  color: #fff;
}
.btn-primary:hover {
  background-color: #43a047;
}

.divider {
  margin: 20px 0;
  height: 1px;
  background: #ddd;
}

.btn-new-session {
  background-color: #888;
  color: #fff;
}
.btn-new-session:hover {
  background-color: #666;
}
</style>
