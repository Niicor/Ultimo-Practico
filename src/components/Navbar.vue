<!-- src/components/Navbar.vue -->
<template>
    <nav class="navbar navbar-expand-lg bg-body-tertiary"
        :class="{ 'navbar-dark bg-dark': isDarkMode, 'navbar-light bg-light': !isDarkMode }">
        <div class="container">
            <a class="navbar-brand" href="#">
                <img src="/vuelos.svg" alt="Logo" width="30" height="24" class="d-inline-block align-text-top me-2">
                Reserva de Vuelos
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation" @click="toggleNavbar">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse justify-content-end" id="navbarNav" ref="navbarCollapse">
                <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" role="switch" id="darkModeSwitch"
                        @change="$emit('toggle-dark-mode')" :checked="isDarkMode">
                    <label class="form-check-label" for="darkModeSwitch">{{ isDarkMode ? 'Modo Claro' : 'Modo Oscuro'
                        }}</label>
                </div>
            </div>
        </div>
    </nav>
</template>

<script>
import { ref, onMounted } from 'vue';
import * as bootstrap from 'bootstrap';

export default {
    props: {
        isDarkMode: Boolean,
    },
    emits: ['toggle-dark-mode'],
    setup() {
        const navbarCollapse = ref(null);
        let bsCollapse = null; // Variable para almacenar la instancia de Bootstrap Collapse

        const toggleNavbar = () => {
            if (bsCollapse) {
                bsCollapse.toggle();
            }
        };

        onMounted(() => {
            // Inicializa bsCollapse solo una vez cuando el componente se monta
            bsCollapse = new bootstrap.Collapse(navbarCollapse.value, { toggle: false });
        });

        return {
            navbarCollapse,
            toggleNavbar,
        };
    },
};
</script>