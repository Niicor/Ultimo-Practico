<!-- src/components/Formulario.vue -->
<template>
    <form @submit.prevent="submitForm">
        <PasajerosForm v-model="pasajeroData" />
        <VueloForm v-model="vueloData" />
        <PagosForm v-model="pagoData" />
        <div v-if="formHasErrors" class="error">El formulario tiene errores. Por favor, corrígelos para continuar.</div>
        <button type="submit" :disabled="!isFormValid">Reservar Vuelo</button>
    </form>
</template>

<script>
import PasajerosForm from './PasajerosForm.vue';
import VueloForm from './VueloForm.vue';
import PagosForm from './PagosForm.vue';

export default {
    components: {
        PasajerosForm,
        VueloForm,
        PagosForm,
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