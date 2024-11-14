<!-- Practico3/src/components/VueloForm.vue -->
<template>
    <div>
        <h3>Datos del Vuelo</h3>
        <label for="ciudadOrigen">Ciudad de Origen:</label>
        <select id="ciudadOrigen" v-model="internalData.ciudadOrigen" @change="validateCiudades">
            <option value="" disabled>Selecciona la ciudad de origen</option>
            <option v-for="aeropuerto in aeropuertos" :key="aeropuerto.code" :value="aeropuerto.city">
                {{ aeropuerto.city }} ({{ aeropuerto.code }})
            </option>
        </select>
        <div v-if="ciudadOrigenError" class="error">{{ ciudadOrigenError }}</div>

        <label for="ciudadDestino">Ciudad de Destino:</label>
        <select id="ciudadDestino" v-model="internalData.ciudadDestino" @change="validateCiudades">
            <option value="" disabled>Selecciona la ciudad de destino</option>
            <option v-for="aeropuerto in aeropuertos" :key="aeropuerto.code" :value="aeropuerto.city">
                {{ aeropuerto.city }} ({{ aeropuerto.code }})
            </option>
        </select>
        <div v-if="ciudadDestinoError" class="error">{{ ciudadDestinoError }}</div>


        <label for="fechaSalida">Fecha de Salida (yyyy-mm-dd):</label>
        <input type="date" id="fechaSalida" v-model="internalData.fechaSalida" @change="validateFechaSalida" />
        <div v-if="fechaSalidaError" class="error">{{ fechaSalidaError }}</div>

        <label for="fechaRegreso">Fecha de Regreso (yyyy-mm-dd):</label>
        <input type="date" id="fechaRegreso" v-model="internalData.fechaRegreso" @change="validateFechaRegreso" />
        <div v-if="fechaRegresoError" class="error">{{ fechaRegresoError }}</div>


        <label for="claseVuelo">Clase:</label>
        <select id="claseVuelo" v-model="internalData.claseVuelo">
            <option value="economica">Económica</option>
            <option value="ejecutiva">Ejecutiva</option>
            <option value="primera">Primera Clase</option>
        </select>

        <label for="numBoletos">Número de Boletos:</label>
        <input type="number" id="numBoletos" v-model="internalData.numBoletos" min="1" max="10" />
    </div>
</template>

<script>
import { ref, watch, onMounted } from 'vue';
import airportData from '../data/airport.json'

export default {
    props: ['modelValue'],
    emits: ['update:modelValue'],
    setup(props, { emit }) {
        const aeropuertos = ref(airportData);
        const internalData = ref({ ...props.modelValue });

        const ciudadOrigenError = ref('');
        const ciudadDestinoError = ref('');
        const fechaSalidaError = ref('');
        const fechaRegresoError = ref('');


        watch(internalData, (newValue) => {
            emit('update:modelValue', newValue);
        }, { deep: true });


        const validateCiudades = () => {
            if (internalData.value.ciudadOrigen && internalData.value.ciudadDestino && internalData.value.ciudadOrigen === internalData.value.ciudadDestino) {
                ciudadOrigenError.value = 'Las ciudades de origen y destino deben ser diferentes.';
                ciudadDestinoError.value = 'Las ciudades de origen y destino deben ser diferentes.';
            } else {
                ciudadOrigenError.value = '';
                ciudadDestinoError.value = '';
            }
        };


        const validateFechaSalida = () => {

            if (internalData.value.fechaSalida && new Date(internalData.value.fechaSalida) < new Date()) {
                fechaSalidaError.value = "La fecha de salida debe ser posterior a la fecha actual"
            } else {
                fechaSalidaError.value = ""
            }
            validateFechaRegreso()

        };

        const validateFechaRegreso = () => {

            if (internalData.value.fechaRegreso && new Date(internalData.value.fechaRegreso) < new Date(internalData.value.fechaSalida)) {
                fechaRegresoError.value = "La fecha de regreso debe ser posterior a la fecha de salida"
            } else {
                fechaRegresoError.value = ""
            }

        };


        return {
            aeropuertos,
            internalData,
            ciudadOrigenError,
            ciudadDestinoError,
            fechaSalidaError,
            fechaRegresoError,
            validateCiudades,
            validateFechaSalida,
            validateFechaRegreso
        };
    },
};
</script>