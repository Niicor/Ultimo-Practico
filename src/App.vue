<!-- src/App.vue -->
<template>
  <div class="main-container" :class="{ 'dark-mode': isDarkMode }"> <!-- Contenedor principal -->
    <Navbar @toggle-dark-mode="toggleDarkMode" :is-dark-mode="isDarkMode" />
    <div class="form-wrapper"> <!-- Contenedor del formulario -->
      <Formulario :is-dark-mode="isDarkMode" />
    </div>
    <Footer :is-dark-mode="isDarkMode" />
  </div>
</template>

<script>
import Formulario from './components/Formulario.vue';
import Navbar from './components/Navbar.vue';
import Footer from './components/Footer.vue';
import { ref, onMounted } from 'vue';

export default {
  components: {
    Formulario,
    Navbar,
    Footer,
  },
  setup() {
    const isDarkMode = ref(false);

    const toggleDarkMode = () => {
      isDarkMode.value = !isDarkMode.value;
      localStorage.setItem('darkMode', isDarkMode.value);

      // Aplicar la clase dark-mode al body
      document.body.classList.toggle('dark-mode', isDarkMode.value);
    };

    onMounted(() => {
      const storedDarkMode = localStorage.getItem('darkMode');
      if (storedDarkMode !== null) {
        isDarkMode.value = JSON.parse(storedDarkMode);
        document.body.classList.toggle('dark-mode', isDarkMode.value); // Aplicar al cargar
      }
    });

    return {
      isDarkMode,
      toggleDarkMode,
    };
  },
};
</script>

<style scoped>
/* Estilos para el fondo y el wrapper del formulario */
.main-container {
  background: linear-gradient(to bottom, #e2e8f0, #f8fafc);
  /* Gradiente claro */
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.dark-mode .main-container {
  background: linear-gradient(to bottom, #1a202c, #2d3748);
  /* Gradiente oscuro */
}

.form-wrapper {
  background-color: rgba(255, 255, 255, 0.9);
  /* Fondo semi-transparente para modo claro */
  padding: 20px;
  border-radius: 10px;
  margin: 20px auto;
  max-width: 90%;
  box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.2);
}

.dark-mode .form-wrapper {
  background-color: rgba(52, 58, 64, 0.9);
  /* Fondo semi-transparente para modo oscuro */
  color: white;
}

/* Media query para ajustar el ancho máximo en pantallas más pequeñas */
@media (max-width: 768px) {
  .form-wrapper {
    max-width: 95%;
    margin: 10px auto;
  }
}
</style>