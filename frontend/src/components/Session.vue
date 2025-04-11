<template>
  <v-container fluid>
    <v-row align="center">
      <v-col cols="auto">
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
.cancel-btn {
  height: fit-content;
  margin-right: 20px;
}
</style>
