<!-- src/App.vue -->
<template>
  <Navbar @toggle-dark-mode="toggleDarkMode" :is-dark-mode="isDarkMode" />
  <Formulario :is-dark-mode="isDarkMode" />
  <Footer :is-dark-mode="isDarkMode" />
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