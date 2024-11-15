<!-- src/components/Formulario.vue -->
<template>
    <div class="container" :class="{ 'dark-mode': isDarkMode }">
        <button @click="toggleDarkMode" class="btn btn-secondary mt-3 mb-3">
            {{ isDarkMode ? 'Modo Claro' : 'Modo Oscuro' }}
        </button>

        <form @submit.prevent="submitForm">
            <div class="row">
                <div class="col-md-4"> <!-- Pasajeros - Columna de 4 en pantallas medianas y grandes -->
                    <PasajerosForm v-model="pasajeroData" />
                </div>
                <div class="col-md-4"> <!-- Vuelo - Columna de 4 en pantallas medianas y grandes -->
                    <VueloForm v-model="vueloData" />
                </div>
                <div class="col-md-4"> <!-- Pagos - Columna de 4 en pantallas medianas y grandes -->
                    <PagosForm v-model="pagoData" />
                </div>
            </div>

            <div class="mt-3" v-if="formHasErrors">
                <div class="alert alert-danger" role="alert">El formulario tiene errores. Por favor, corrígelos para
                    continuar.</div>
            </div>
            <button type="submit" :disabled="!isFormValid" class="btn btn-primary mt-3">Reservar Vuelo</button>
            <div v-if="!isFormValid" class="alert alert-danger mt-3" role="alert">
                Completa todos los campos correctamente para habilitar el botón.
            </div>
        </form>
    </div>
</template>

<script>
import PasajerosForm from './PasajerosForm.vue';
import VueloForm from './VueloForm.vue';
import PagosForm from './PagosForm.vue';
import { ref, onMounted } from 'vue';

export default {
    components: {
        PasajerosForm,
        VueloForm,
        PagosForm,
    },
    setup() {
        const isDarkMode = ref(false);

        const toggleDarkMode = () => {
            isDarkMode.value = !isDarkMode.value;
            // Guardar la preferencia del usuario en localStorage (opcional)
            localStorage.setItem('darkMode', isDarkMode.value);
        };

        // Cargar la preferencia del usuario al iniciar (opcional)
        onMounted(() => {
            const storedDarkMode = localStorage.getItem('darkMode');
            if (storedDarkMode !== null) {
                isDarkMode.value = JSON.parse(storedDarkMode); // Convertir a booleano
            }
        });

        return {
            isDarkMode,
            toggleDarkMode,
        };
    },
    data() {
        return {
            pasajeroData: {},
            vueloData: {},
            pagoData: {},
        };
    },
    computed: {
        isFormValid() {
            return this.isPasajeroValid && this.isVueloValid && this.isPagoValid;
        },
        formHasErrors() {
            return !this.isFormValid;
        },
        isPasajeroValid() {
            // ... (Lógica de validación de PasajerosForm)
            for (const key in this.pasajeroData) {
                if (this.pasajeroData[key] === '' || this.pasajeroData[key] === null || (key === 'nombre' && this.pasajeroData[key].length < 3)) return false;
            }
            return true;
        },
        isVueloValid() {
            // ... (Lógica de validación de VueloForm)
            for (const key in this.vueloData) {
                if (!this.vueloData[key]) return false;
            }
            return this.vueloData.ciudadOrigen !== this.vueloData.ciudadDestino && this.isFechaSalidaValid && this.isFechaRegresoValid;
        },
        isFechaSalidaValid() {
            if (!this.vueloData.fechaSalida) return false;
            return new Date(this.vueloData.fechaSalida) > new Date();
        },
        isFechaRegresoValid() {
            if (!this.vueloData.fechaRegreso) return true; // Si no hay fecha de regreso, es válido
            return new Date(this.vueloData.fechaRegreso) > new Date(this.vueloData.fechaSalida);
        },
        isPagoValid() {
            // ... (Lógica de validación de PagosForm)
            for (const key in this.pagoData) {
                if (!this.pagoData[key]) return false;
            }
            return true;
        },
    },
    methods: {
        submitForm() {
            if (this.isFormValid) {
                // Aquí iría la lógica para enviar los datos
                console.log('Datos del formulario:', {
                    pasajero: this.pasajeroData,
                    vuelo: this.vueloData,
                    pago: this.pagoData,
                });
            }
        },
    },
};
</script>

<style scoped>
.dark-mode {
    background-color: #343a40;
    /* Color de fondo oscuro */
    color: #f8f9fa;
    /* Color de texto claro */
}

.dark-mode .card {
    background-color: #454d55;
    /* Color de fondo oscuro para las cards */
    color: #f8f9fa;
    /* Color de texto claro en las cards */
}

/* Estilos adicionales para elementos específicos en modo oscuro */
.dark-mode label,
.dark-mode input,
.dark-mode select,
.dark-mode option {
    color: #f8f9fa;
    /* Ajustar el color del texto en inputs, labels, etc. */
    background-color: #454d55;
}

.dark-mode .btn-secondary {
    color: white;
}

.dark-mode option {
    background-color: #454d55;
}
</style>