<!-- src/components/Formulario.vue -->
<template>
    <div class="container" :class="{ 'dark-mode': isDarkMode }">

        <form @submit.prevent="submitForm">
            <div class="row">
                <div class="col-md-4">
                    <PasajerosForm v-model="pasajeroData" />
                </div>
                <div class="col-md-4">
                    <VueloForm v-model="vueloData" />
                </div>
                <div class="col-md-4">
                    <PagosForm v-model="pagoData" />
                </div>
            </div>

            <div v-if="formHasErrors" class="alert alert-danger mt-3" role="alert">
                El formulario tiene errores. Por favor, corrígelos para continuar.
            </div>

            <button type="submit" :disabled="!isFormValid" class="btn btn-primary mt-3">
                Reservar Vuelo
            </button>

            <div v-if="!isFormValid" class="alert alert-danger mt-3" role="alert">
                Completa todos los campos correctamente para habilitar el botón.
            </div>
        </form>

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
import PagosForm from './PagosForm.vue';
import * as bootstrap from 'bootstrap';

export default {
    components: {
        PasajerosForm,
        VueloForm,
        PagosForm,
    },
    setup() {
        return {};
    },
    data() {
        return {
            pasajeroData: {},
            vueloData: {},
            pagoData: {},
            modalTitle: '',
            modalMessage: '',
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
    },
    methods: {
        submitForm() {
            if (this.isFormValid) {
                this.modalTitle = 'Reserva Confirmada';
                this.modalMessage = '¡Tu reserva se ha realizado con éxito!';
                this.showModal();

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
    /* Color de fondo oscuro para el contenido del modal */
    color: #f8f9fa;
    /* Color de texto claro para el contenido del modal */
}

.dark-mode .modal-header,
.dark-mode .modal-footer {
    border-color: #343a40;
    /* Color de borde oscuro para header y footer */
}

.dark-mode .modal-title {
    color: #f8f9fa;
    /* Asegúrate de que el título también sea claro */
}

.dark-mode .btn-secondary {
    /* ajusta los botones de cerrar dentro del modal */
    background-color: #6c757d;
    border-color: #6c757d;
    color: #f8f9fa;
}
</style>