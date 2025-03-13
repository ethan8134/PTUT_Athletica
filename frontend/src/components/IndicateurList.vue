<template>
  <div class="list-container">
    <div class="search-container">
      <input
        type="text"
        v-model="searchTerm"
        placeholder="Rechercher un indicateur..."
      />

      <div class="dropdown">
        <button @click="toggleDropdown">Filtrer ▼</button>
        <div v-if="showDropdown" class="dropdown-menu">
          <button @click="selectCateg('')">Toutes catégories</button>
          <button
            v-for="categ in categorieUnique"
            :key="categ"
            @click="selectCateg(categ)"
          >
            {{ categ }}
          </button>
        </div>
      </div>
    </div>

    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>Nom</th>
            <th>Valeur</th>
            <th>Unité</th>
            <th>Categorie</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="ind in indicateur" :key="ind.id">
            <td v-if="!ind.editing">{{ ind.nom }}</td>
            <td v-else><input v-model="ind.nom" /></td>

            <td v-if="!ind.editing">{{ ind.valeur }}</td>
            <td v-else><input v-model="ind.valeur" /></td>

            <td v-if="!ind.editing">{{ ind.unite }}</td>
            <td v-else><input v-model="ind.unite" /></td>

            <td v-if="!ind.editing">{{ ind.categorie }}</td>
            <td v-else><input v-model="ind.categorie" /></td>

            <td class="action-buttons">
              <button v-if="!ind.editing" @click="startEdit(ind)">
                Modifier
              </button>
              <button v-else @click="updateIndicateur(ind)">Valider</button>
              <button v-if="ind.editing" @click="cancelEdit(ind)">
                Annuler
              </button>
              <button @click="() => deleteIndicateur(ind.id)">Supprimer</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from "vue";

const searchTerm = ref("");
const indicateur = ref([]);
const allIndicateurs = ref([]);
const showDropdown = ref(false);
const selectedCateg = ref("");
const categorieUnique = ref([]);
const showForm = ref(false);
const originalIndicateur = ref({});

// URL de l'API (les endpoints du backend sont configurés sur /api/indicateurs)
const apiBaseUrl = "http://localhost:8989/api/indicateurs";

// Récupère tous les indicateurs depuis le backend
const fetchIndicateurs = () => {
  fetch(apiBaseUrl)
    .then((response) => response.json())
    .then((data) => {
      allIndicateurs.value = data; // affecte la réponse complète
      // Met à jour les catégories uniques à partir des données récupérées
      categorieUnique.value = [
        ...new Set(allIndicateurs.value.map((ind) => ind.categorie)),
      ];
      filterIndicateurs();
    })
    .catch((error) => {
      console.error("Erreur lors de la récupération des indicateurs: ", error);
    });
};

const filterIndicateurs = () => {
  indicateur.value = allIndicateurs.value.filter((ind) => {
    const matchesSearch = searchTerm.value
      ? ind.nom.toLowerCase().includes(searchTerm.value.toLowerCase())
      : true;
    const matchesCateg = selectedCateg.value
      ? ind.categorie === selectedCateg.value
      : true;
    return matchesSearch && matchesCateg;
  });
};

const selectCateg = (categorie) => {
  selectedCateg.value = categorie;
  showDropdown.value = false;
  filterIndicateurs();
};

const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value;
};

const updateIndicateur = (ind) => {
  // Prépare l'objet à envoyer avec les bons noms de propriété
  const updatedInd = {
    id: ind.id,
    nom: ind.nom,
    valeur: ind.valeur,
    unite: ind.unite,
    categorie: ind.categorie,
  };

  // Notez que l'URL inclut l'ID pour correspondre à @PutMapping("/{id}")
  fetch(`${apiBaseUrl}/${ind.id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(updatedInd),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Erreur lors de la mise à jour de l'indicateur");
      }
      return response.json();
    })
    .then(() => {
      ind.editing = false;
      fetchIndicateurs();
    })
    .catch((error) => {
      console.error("Erreur lors de la mise à jour de l'indicateur: ", error);
    });
};

const deleteIndicateur = (id) => {
  if (
    !window.confirm(
      "Êtes-vous sûr de vouloir supprimer cet indicateur ? Cette action est irréversible."
    )
  ) {
    return;
  }

  fetch(`${apiBaseUrl}/${id}`, { method: "DELETE" })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Erreur lors de la suppression de l'indicateur");
      }
      // On peut supposer que la suppression réussit si response.ok est vrai
      return response.json();
    })
    .then(() => {
      // Met à jour la liste en retirant l'indicateur supprimé
      allIndicateurs.value = allIndicateurs.value.filter(
        (ind) => ind.id !== id
      );
      filterIndicateurs();
    })
    .catch((error) => {
      console.error("Erreur lors de la suppression de l'indicateur: ", error);
    });
};

const startEdit = (ind) => {
  originalIndicateur.value[ind.id] = { ...ind };
  ind.editing = true;
};

const cancelEdit = (ind) => {
  Object.assign(ind, originalIndicateur.value[ind.id]);
  ind.editing = false;
};

watch(searchTerm, filterIndicateurs);
onMounted(fetchIndicateurs);
</script>

<style scoped>
body {
  font-family: Arial, sans-serif;
  background-color: #f4f4f4;
  margin: 0;
  padding: 0;
}

.container {
  width: 90%;
  max-width: 1200px;
  margin: 20px auto;
  padding: 20px;
  background: white;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
}

button {
  background-color: #007fff;
  color: white;
  border: none;
  padding: 10px 15px;
  cursor: pointer;
  border-radius: 5px;
  transition: background 0.3s ease-in-out;
}

table {
  width: 100%;

  background: lightgray;
}

.table-container td input {
  padding: 5px;
  border: 1px solid black;
  border-radius: 4px;
  font-size: 16px;
}

th,
td {
  border: 1px solid #ddd;
  padding: 10px;
  text-align: left;
  color: black;
  vertical-align: middle;
  height: 60px;
}

th {
  background-color: #007fff;
  color: white;
}

tr:nth-child(even) {
  background: #f9f9f9;
}

img {
  border-radius: 5px;
  max-width: 50px;
}

.action-buttons {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 5px;
  height: 100px;
}

.action-buttons button {
  flex: 1;
  padding: 8px 12px;
  margin: auto 0px;
  height: 40px;
  width: 60px;
  text-align: center;
  border: solid;
  border-color: black;
  border-width: 1px;
}

.action-buttons button:hover {
  background-color: darkblue;
}

.button-container {
  margin-top: 15px;
  display: flex;
  justify-content: center;
}

.button-container button {
  width: 100%;
  max-width: 100%;
  padding: 12px;
  font-size: 18px;
  background-color: #007fff;
  border-radius: 5px;
  border: none;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.3s ease-in-out;
}

.button-container button:hover {
  background-color: darkblue;
}

.search-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  margin: 20px auto;
  width: 100%;
}

.search-container input {
  flex: 1;
  max-width: 400px;
  padding: 10px;
  border: 1px solid black;
  border-radius: 5px;
  font-size: 16px;
  transition: border 0.3s ease-in-out;
}

.search-container input:focus {
  outline: none;
}

.search-container button {
  padding: 10px 15px;
  font-size: 16px;
  background-color: #007fff;
  border: none;
  border-radius: 5px;
  color: white;
  cursor: pointer;
  transition: background 0.3s ease-in-out;
}

.search-container button:hover {
  background-color: darkblue;
}

.dropdown {
  position: relative;
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  left: 0;
  background: white;
  border: 1px solid #ddd;
  border-radius: 5px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
  z-index: 10;
  display: flex;
  flex-direction: column;
  width: 200px;
}

.dropdown-menu button {
  background: white;
  color: black;
  padding: 10px;
  border: none;
  text-align: left;
  width: 100%;
  cursor: pointer;
  transition: background 0.3s ease-in-out;
}

.dropdown-menu button:hover {
  background: #007fff;
  color: white;
}

.list-container {
  border: solid;
  border-radius: 15px;
  border-color: gray;
  box-shadow: 2px 2px 2px gray;
  margin-top: 15px;
  padding: 5px;
  border-width: 2px;
  background-color: lightgray;
}
</style>
