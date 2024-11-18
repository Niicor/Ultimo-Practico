<!-- src/components/Formulario.vue -->
<template>
    <div class="container" :class="{ 'dark-mode': isDarkMode }">
        <form @submit.prevent="submitForm">
            <div class="row">
                <!-- Pasajeros -->
                <div class="col-md-4">
                    <PasajerosForm v-model="pasajeroData" />
                </div>

                <!-- Vuelo -->
                <div class="col-md-4">
                    <VueloForm v-model="vueloData" />
                </div>

                <!-- Costo -->
                <div class="col-md-4">
                    <CostoViaje v-if="vueloData.ciudadOrigen && vueloData.ciudadDestino" :vueloData="vueloData"
                        :is-dark-mode="isDarkMode" ref="costoViaje" />
                </div>

                <!-- Pagos -->
                <div class="col-md-4">
                    <PagosForm v-model="pagoData" />
                </div>
            </div>

            <div v-if="formHasErrors" class="alert alert-danger mt-3" role="alert">
                * El formulario tiene errores. Por favor, corrígelos para continuar.
            </div>

            <div v-if="!isFormValid" class="alert alert-danger mt-3" role="alert">
                * Completa todos los campos correctamente para habilitar el botón.
            </div>

            <button type="submit" :disabled="!isFormValid" class="btn btn-primary mt-3">
                Reservar Vuelo
            </button>
        </form>

        <!-- Resumen de la Reserva -->
        <ResumenReserva v-if="showModalResumen" :pasajero="pasajeroData" :vuelo="vueloData" :pago="pagoData"
            :is-dark-mode="isDarkMode" :costoTotal="costoTotal" />

        <!-- Modal de Bootstrap -->
        <div class="modal fade" id="confirmationModal" tabindex="-1" aria-labelledby="confirmationModalLabel"
            aria-hidden="true" ref="confirmationModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="confirmationModalLabel">
                            {{ modalTitle }}
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">{{ modalMessage }}</div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            Cerrar
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import PasajerosForm from './PasajerosForm.vue';
import VueloForm from './VueloForm.vue';
import CostoViaje from './CostoViaje.vue';
import PagosForm from './PagosForm.vue';
import ResumenReserva from './ResumenReserva.vue'; // Importar el componente ResumenReserva
import * as bootstrap from 'bootstrap';
import { nextTick } from 'vue';

export default {
    components: {
        PasajerosForm,
        VueloForm,
        CostoViaje,
        PagosForm,
        ResumenReserva,
    },
    props: {
        isDarkMode: Boolean, // Recibe isDarkMode como prop
    },
    data() {
        return {
            pasajeroData: {},
            vueloData: {},
            pagoData: {},
            modalTitle: '',
            modalMessage: '',
            showModalResumen: false, // Nueva variable para controlar la visibilidad del resumen
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
            for (const key in this.pasajeroData) {
                if (this.pasajeroData[key] === '' || this.pasajeroData[key] === null || (key === 'nombre' && this.pasajeroData[key].length < 3)) return false;
            }
            return true;
        },
        isVueloValid() {
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
            if (!this.vueloData.fechaRegreso) return true;
            return new Date(this.vueloData.fechaRegreso) > new Date(this.vueloData.fechaSalida);
        },
        isPagoValid() {
            for (const key in this.pagoData) {
                if (!this.pagoData[key]) return false;
            }
            return true;
        },
        costoTotal() {
            return this.$refs.costoViaje ? this.$refs.costoViaje.costoTotal : 0;
        },
    },
    methods: {
        submitForm() {
            if (this.isFormValid) {
                this.modalTitle = 'Reserva Confirmada';
                this.modalMessage = '¡Tu reserva se ha realizado con éxito!';
                this.showModal();
                this.showModalResumen = true; // Mostrar el resumen de la reserva

                console.log('Datos del formulario:', {
                    pasajero: this.pasajeroData,
                    vuelo: this.vueloData,
                    pago: this.pagoData,
                });
            } else {
                this.modalTitle = 'Error en el Formulario';
                this.modalMessage = 'Por favor, completa todos los campos correctamente antes de enviar el formulario.';
                this.showModal();
            }
        },
        showModal() {
            nextTick(() => {
                const modalElement = this.$refs.confirmationModal;
                const modal = new bootstrap.Modal(modalElement);
                modal.show();
            });
        },
    },
};
</script>

<style scoped>
.dark-mode {
    background-color: #343a40;
    color: #f8f9fa;
}

.dark-mode .card {
    background-color: #454d55;
    color: #f8f9fa;
}

.dark-mode label,
.dark-mode input,
.dark-mode select,
.dark-mode option {
    color: #f8f9fa;
    background-color: #454d55;
}

.dark-mode .btn-secondary {
    color: white;
}

.dark-mode option {
    background-color: #454d55;
}

.dark-mode .modal-content {
    background-color: #454d55;
    color: #f8f9fa;
}

.dark-mode .modal-header,
.dark-mode .modal-footer {
    border-color: #343a40;
}

.dark-mode .modal-title {
    color: #f8f9fa;
}

.dark-mode .btn-secondary {
    background-color: #6c757d;
    border-color: #6c757d;
    color: #f8f9fa;
}
</style>