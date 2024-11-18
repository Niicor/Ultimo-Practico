<!-- src/components/Formulario.vue -->
<template>
    <div class="container" :class="{ 'dark-mode': isDarkMode }">
        <form @submit.prevent="submitForm">
            <div class="row">
                <div class="col-md-6">
                    <div class="card mb-3">
                        <div class="card-body">
                            <PasajerosForm v-model="pasajeroData" @update:modelValue="updateFormValidity" />
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="card mb-3">
                        <div class="card-body">
                            <VueloForm v-model="vueloData" @update:modelValue="updateFormValidity" />
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="card mb-3">
                        <div class="card-body">
                            <CostoViaje v-if="vueloData.ciudadOrigen && vueloData.ciudadDestino" :vueloData="vueloData"
                                :is-dark-mode="isDarkMode" ref="costoViajeRef" />
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="card mb-3">
                        <div class="card-body">
                            <PagosForm v-model="pagoData" @update:modelValue="updateFormValidity" />
                        </div>
                    </div>
                </div>
            </div>
            <ResumenReserva :pasajero="pasajeroData" :vuelo="vueloData" :pago="pagoData" :is-dark-mode="isDarkMode"
                :costo-total="costoTotal" />
            <div v-if="formHasErrors" class="alert alert-danger mt-3" role="alert">
                * El formulario tiene errores. Por favor, corrígelos para continuar.
            </div>
            <div v-if="!isFormValid && !formHasErrors" class="alert alert-danger mt-3" role="alert">
                * Completa todos los campos correctamente para habilitar el botón.
            </div>
            <button type="submit" :disabled="!isFormValid || formHasErrors" class="btn btn-primary mt-3">
                Reservar Vuelo
            </button>
        </form>
        <div class="modal fade" id="confirmationModal" tabindex="-1" aria-labelledby="confirmationModalLabel"
            aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="confirmationModalLabel">{{ modalTitle }}</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">{{ modalMessage }}</div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
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
import ResumenReserva from './ResumenReserva.vue';
import * as bootstrap from 'bootstrap';
import { ref, nextTick, computed, onMounted, watch } from 'vue';

export default {
    components: {
        PasajerosForm,
        VueloForm,
        CostoViaje,
        PagosForm,
        ResumenReserva,
    },
    props: {
        isDarkMode: Boolean,
    },
    setup(props) {
        const pasajeroData = ref({});
        const vueloData = ref({});
        const pagoData = ref({});
        const modalTitle = ref('');
        const modalMessage = ref('');
        const costoViajeRef = ref(null);

        const costoTotal = computed(() => {
            return costoViajeRef.value ? costoViajeRef.value.costoTotal : 0;
        });

        const isFormValid = ref(false);
        const formHasErrors = ref(false);

        const updateFormValidity = () => {
            formHasErrors.value = Object.values({ ...pasajeroData.value, ...vueloData.value, ...pagoData.value }).some(
                value => value === "" || value === null || value === undefined
            );
            isFormValid.value = !formHasErrors.value && isPasajeroValid.value && isVueloValid.value && isPagoValid.value;
        };

        watch([pasajeroData, vueloData, pagoData], () => {
            updateFormValidity();
        });

        const isPasajeroValid = computed(() => {
            for (const key in pasajeroData.value) {
                if (pasajeroData.value[key] === '' || pasajeroData.value[key] === null || (key === 'nombre' && pasajeroData.value[key].length < 3)) return false;
            }
            return true;
        });

        const isVueloValid = computed(() => {
            for (const key in vueloData.value) {
                if (!vueloData.value[key]) return false;
            }
            return vueloData.value.ciudadOrigen !== vueloData.value.ciudadDestino && isFechaSalidaValid.value && isFechaRegresoValid.value;
        });

        const isFechaSalidaValid = computed(() => {
            if (!vueloData.value.fechaSalida) return false;
            return new Date(vueloData.value.fechaSalida) > new Date();
        });

        const isFechaRegresoValid = computed(() => {
            if (!vueloData.value.fechaRegreso) return true;
            return new Date(vueloData.value.fechaRegreso) > new Date(vueloData.value.fechaSalida);
        });

        const isPagoValid = computed(() => {
            for (const key in pagoData.value) {
                if (!pagoData.value[key]) return false;
            }
            return true;
        });

        const submitForm = () => {
            if (isFormValid.value) {
                modalTitle.value = 'Reserva Confirmada';
                modalMessage.value = '¡Tu reserva se ha realizado con éxito!';
                showModal();
            } else {
                modalTitle.value = 'Error en el Formulario';
                modalMessage.value = 'Por favor, completa todos los campos correctamente antes de enviar el formulario.';
                showModal();
            }
        };

        const showModal = () => {
            nextTick(() => {
                const modalElement = document.getElementById('confirmationModal');
                const modal = new bootstrap.Modal(modalElement);
                modal.show();
            });
        };

        onMounted(() => {
            console.log("CostoViaje ref en onMounted:", costoViajeRef.value);
        });

        return {
            pasajeroData,
            vueloData,
            pagoData,
            modalTitle,
            modalMessage,
            costoTotal,
            isFormValid,
            formHasErrors,
            isPasajeroValid,
            isVueloValid,
            isFechaSalidaValid,
            isFechaRegresoValid,
            isPagoValid,
            submitForm,
            showModal,
            costoViajeRef,
            updateFormValidity,
        };
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