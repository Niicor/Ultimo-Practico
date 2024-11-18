<!-- src/components/VueloForm.vue -->
<template>
    <div class="card">
        <div class="card-body">
            <h5 class="card-title">Datos del Vuelo</h5>

            <form class="row g-3 needs-validation" novalidate @submit.prevent="submitForm">
                <div class="col-md-6">
                    <label for="ciudadOrigen" class="form-label">Ciudad de Origen:</label>
                    <select id="ciudadOrigen" v-model="internalData.ciudadOrigen" class="form-select"
                        :class="{ 'is-invalid': ciudadOrigenError, 'is-valid': !ciudadOrigenError && internalData.ciudadOrigen }"
                        @change="validateOrigenDestino">
                        <option value="" disabled>Selecciona una ciudad de origen</option>
                        <option v-for="aeropuerto in aeropuertos" :key="aeropuerto.code" :value="aeropuerto.city">
                            {{ aeropuerto.city }} ({{ aeropuerto.code }})
                        </option>
                    </select>
                    <div v-if="ciudadOrigenError" class="invalid-feedback">{{ ciudadOrigenError }}</div>
                </div>

                <div class="col-md-6">
                    <label for="ciudadDestino" class="form-label">Ciudad de Destino:</label>
                    <select id="ciudadDestino" v-model="internalData.ciudadDestino" class="form-select"
                        :class="{ 'is-invalid': ciudadDestinoError, 'is-valid': !ciudadDestinoError && internalData.ciudadDestino }"
                        @change="validateOrigenDestino">
                        <option value="" disabled>Selecciona una ciudad de destino</option>
                        <option v-for="aeropuerto in aeropuertos" :key="aeropuerto.code" :value="aeropuerto.city">
                            {{ aeropuerto.city }} ({{ aeropuerto.code }})
                        </option>
                    </select>
                    <div v-if="ciudadDestinoError" class="invalid-feedback">{{ ciudadDestinoError }}</div>
                </div>

                <div class="col-md-12">
                    <label for="tipoVuelo">Tipo de Vuelo:</label>
                    <select id="tipoVuelo" v-model="internalData.tipoVuelo" class="form-select"
                        @change="validateFechas">
                        <option value="ida">Solo Ida</option>
                        <option value="idaYVuelta">Ida y Vuelta</option>
                    </select>
                </div>

                <div class="col-md-6">
                    <label for="fechaSalida" class="form-label">Fecha de Salida:</label>
                    <input type="date" id="fechaSalida" v-model="internalData.fechaSalida" class="form-control"
                        :class="{ 'is-invalid': fechaSalidaError, 'is-valid': !fechaSalidaError && internalData.fechaSalida }"
                        @change="validateFechas" />
                    <div v-if="fechaSalidaError" class="invalid-feedback">{{ fechaSalidaError }}</div>
                </div>

                <div class="col-md-6" v-show="internalData.tipoVuelo === 'idaYVuelta'">
                    <label for="fechaRegreso" class="form-label">Fecha de Regreso:</label>
                    <input type="date" id="fechaRegreso" v-model="internalData.fechaRegreso" class="form-control"
                        :class="{ 'is-invalid': fechaRegresoError, 'is-valid': !fechaRegresoError && internalData.fechaRegreso }"
                        @change="validateFechas" />
                    <div v-if="fechaRegresoError" class="invalid-feedback">{{ fechaRegresoError }}</div>
                </div>

                <div class="col-md-6">
                    <label for="claseVuelo" class="form-label">Clase de Vuelo:</label>
                    <select id="claseVuelo" v-model="internalData.claseVuelo" class="form-select"
                        :class="{ 'is-invalid': claseVueloError, 'is-valid': !claseVueloError && internalData.claseVuelo }">
                        <option value="" disabled>Selecciona una clase</option>
                        <option value="Economica">Económica</option>
                        <option value="Ejecutiva">Ejecutiva</option>
                        <option value="Primera Clase">Primera Clase</option>
                    </select>
                    <div v-if="claseVueloError" class="invalid-feedback">{{ claseVueloError }}</div>
                </div>

                <div class="col-md-6">
                    <label for="numeroBoletos" class="form-label">Número de Boletos:</label>
                    <input type="number" id="numeroBoletos" v-model="internalData.numeroBoletos" class="form-control"
                        :class="{ 'is-invalid': numeroBoletosError, 'is-valid': !numeroBoletosError && internalData.numeroBoletos }"
                        min="1" max="10" @input="validateNumeroBoletos" />
                    <div v-if="numeroBoletosError" class="invalid-feedback">{{ numeroBoletosError }}</div>
                </div>

            </form>
        </div>
    </div>
</template>

<script>
import { ref, watch, onMounted } from 'vue';

export default {
    props: ['modelValue'],
    emits: ['update:modelValue'],

    setup(props, { emit }) {
        const internalData = ref({
            ...props.modelValue,
            tipoVuelo: 'ida' // Valor por defecto
        });
        const aeropuertos = ref([]);

        const ciudadOrigenError = ref('');
        const ciudadDestinoError = ref('');
        const fechaSalidaError = ref('');
        const fechaRegresoError = ref('');
        const claseVueloError = ref('');
        const numeroBoletosError = ref('');

        onMounted(async () => {
            try {
                const response = await fetch('/airport.json');
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                aeropuertos.value = await response.json();
            } catch (error) {
                console.error("Error al cargar aeropuertos:", error);
                
            }
        });

        const validateOrigenDestino = () => {
            if (internalData.value.ciudadOrigen === internalData.value.ciudadDestino) {
                ciudadDestinoError.value = "La ciudad de destino debe ser diferente a la de origen.";
            } else {
                ciudadDestinoError.value = "";
            }
        };

        const validateFechas = () => {
            const fechaSalida = new Date(internalData.value.fechaSalida);
            const fechaRegreso = new Date(internalData.value.fechaRegreso);
            const hoy = new Date();

            if (fechaSalida < hoy) {
                fechaSalidaError.value = "La fecha de salida debe ser posterior a la fecha actual.";
            } else {
                fechaSalidaError.value = "";
            }

            if (internalData.value.tipoVuelo === 'idaYVuelta') {
                if (fechaRegreso && fechaRegreso <= fechaSalida) {
                    fechaRegresoError.value = "La fecha de regreso debe ser posterior a la fecha de salida.";
                } else {
                    fechaRegresoError.value = "";
                }
            } else {
                fechaRegresoError.value = "";
            }
        };

        const validateNumeroBoletos = () => {
            const num = parseInt(internalData.value.numeroBoletos, 10);
            if (isNaN(num) || num < 1 || num > 10) {
                numeroBoletosError.value = "El número de boletos debe ser entre 1 y 10.";
            } else {
                numeroBoletosError.value = "";
            }
        };

        const submitForm = () => {
            validateOrigenDestino();
            validateFechas();
            validateNumeroBoletos();

            if (!ciudadOrigenError.value && !ciudadDestinoError.value && !fechaSalidaError.value && !fechaRegresoError.value && !numeroBoletosError.value) {
                console.log("Formulario enviado:", internalData.value);
            }
        };

        watch(internalData, (newValue) => {
            emit('update:modelValue', newValue);
        }, { deep: true });

        return {
            aeropuertos,
            internalData,
            ciudadOrigenError,
            ciudadDestinoError,
            fechaSalidaError,
            fechaRegresoError,
            claseVueloError,
            numeroBoletosError,
            validateOrigenDestino,
            validateFechas,
            validateNumeroBoletos,
            submitForm
        };
    },
};
</script>