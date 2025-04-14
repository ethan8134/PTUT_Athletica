<template>
  <v-container fluid>
    <v-row align="center">
      <v-col cols="auto" class="button-container">
        <v-btn
          color="primary"
          dark
          class="cancel-btn"
          @click="returnMesSessions"
        >
          Retour
        </v-btn>
      </v-col>

      <v-col>
        <SessionHeader />
        <SessionList />
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { useRouter } from "vue-router";
import SessionHeader from "./SessionHeader.vue";
import SessionList from "./SessionList.vue";

const router = useRouter();

const returnMesSessions = () => {
  router.push("/MesSessions");
};
</script>

<style scoped>
.button-container {
  display: flex;
  align-items: flex-start;
  margin-top: -20px;
}

.cancel-btn {
  background-color: #0e0c70;
  color: white;
  padding: 10px 16px;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  height: fit-content;
}

.cancel-btn:hover {
  background-color: #0c085a;
}
</style>
