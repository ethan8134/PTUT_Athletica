<template>
  <v-container class="help-page">
    <div class="help-content">
      <v-icon class="help-icon" size="64">mdi-headset</v-icon>
      <h1>Contactez-nous !</h1>
      <p>
        Nous voulons répondre à vos questions, entendre vos critiques et
        suggestions
      </p>
      <v-btn class="contact-btn" color="orange" outlined>
        <v-icon left>mdi-whatsapp</v-icon>
        Entrer en contact
      </v-btn>
    </div>
    <footer class="footer"></footer>
  </v-container>
</template>

<script setup></script>

<style scoped>
.help-page {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  min-height: 100vh;
  background-color: #0e0c70;
  color: white;
  text-align: center;
  padding: 20px;
}

.help-content {
  margin-top: auto;
}

.help-icon {
  color: white;
  margin-bottom: 16px;
}

h1 {
  font-size: 24px;
  margin-bottom: 8px;
}

p {
  font-size: 16px;
  margin-bottom: 24px;
}

.contact-btn {
  font-size: 16px;
  font-weight: bold;
  color: white;
  background-color: orange;
  border-radius: 8px;
}

.contact-btn:hover {
  background-color: #ff8c00;
}

.footer {
  margin-top: auto;
  background-color: black;
  color: white;
  padding: 10px;
  width: 100%;
  text-align: center;
}
</style>
