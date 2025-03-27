<template>
  <div>
    <SessionForm
      v-if="showForm"
      @close-form="showForm = false"
    />
    <FileUpload
      v-else
      @start-session="showForm = true"
    />
  </div>
</template>

<script setup>
import { ref } from "vue";
import FileUpload from "@/components/FileUpload.vue";
import SessionForm from "@/components/SessionForm.vue";

const showForm = ref(false);
</script>
