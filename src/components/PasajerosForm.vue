<!-- src/components/PasajerosForm.vue -->
<template>
    <div>
        <h3>Datos del Pasajero</h3>
        <label for="nombre">Nombre Completo:</label>
        <input type="text" id="nombre" v-model.trim="internalData.nombre" @input="validateNombre"
            placeholder="Nombre Completo" />
        <div v-if="nombreError" class="error">{{ nombreError }}</div>

        <label for="dni">Número de Pasaporte/DNI (Argentina):</label>
        <input type="text" id="dni" v-model.trim="internalData.dni" @input="validateDni" placeholder="DNI/Pasaporte" />
        <div v-if="dniError" class="error">{{ dniError }}</div>

        <label for="fechaNacimiento">Fecha de Nacimiento (dd/mm/yyyy):</label>
        <input type="text" id="fechaNacimiento" v-model="internalData.fechaNacimiento" @input="validateFechaNacimiento"
            placeholder="dd/mm/yyyy" />
        <div v-if="fechaNacimientoError" class="error">{{ fechaNacimientoError }}</div>

        <label for="nacionalidad">Nacionalidad:</label>
        <select id="nacionalidad" v-model="internalData.nacionalidad">
            <option value="" disabled>Selecciona tu nacionalidad</option>
            <option v-for="pais in paises" :key="pais.name.common" :value="pais.name.common">{{ pais.name.common }}
            </option>
        </select>
        <div v-if="!internalData.nacionalidad" class="error">Debes seleccionar una nacionalidad.</div>
    </div>
</template>

<script>
import { ref, watch, onMounted } from 'vue';

export default {
    props: ['modelValue'],
    emits: ['update:modelValue'],

    setup(props, { emit }) {
        const internalData = ref({ ...props.modelValue });
        const paises = ref([]);
        const nombreError = ref('');
        const dniError = ref('');
        const fechaNacimientoError = ref('');

        onMounted(async () => {
            try {
                const response = await fetch('/src/data/countries.json');
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                paises.value = await response.json();
            } catch (error) {
                console.error("Error al cargar países:", error);
                alert("No se pudieron cargar las nacionalidades. Por favor, inténtalo de nuevo más tarde.");
            }
        });

        watch(internalData, (newValue) => {
            emit('update:modelValue', newValue);
        }, { deep: true });

        const validateNombre = () => {
            if (!/^[a-zA-Z\s]{3,}$/.test(internalData.value.nombre)) {
                nombreError.value = 'El nombre debe contener al menos 3 letras y espacios.';
            } else {
                nombreError.value = '';
            }
        };

        const validateDni = () => {
            if (!/^[a-zA-Z0-9]{7,8}$/.test(internalData.value.dni)) {
                dniError.value = 'El DNI/Pasaporte Argentino debe tener entre 7 y 8 caracteres alfanuméricos.';
            } else {
                dniError.value = '';
            }
        };

        const validateFechaNacimiento = () => {
            const inputDate = internalData.value.fechaNacimiento;
            if (!/^\d{2}\/\d{2}\/\d{4}$/.test(inputDate)) {
                fechaNacimientoError.value = 'Formato de fecha incorrecto (dd/mm/yyyy).';
                return;
            }

            const parts = inputDate.split('/');
            const day = parseInt(parts[0], 10);
            const month = parseInt(parts[1], 10) - 1; // Los meses en JavaScript son de 0 a 11
            const year = parseInt(parts[2], 10);

            const date = new Date(year, month, day);

            if (isNaN(date.getTime())) { // Verificar si la fecha es válida
                fechaNacimientoError.value = 'Fecha inválida.';
                return;
            }

            const today = new Date();
            let age = today.getFullYear() - date.getFullYear();
            const m = today.getMonth() - date.getMonth();
            if (m < 0 || (m === 0 && today.getDate() < date.getDate())) {
                age--;
            }

            if (age < 18) {
                fechaNacimientoError.value = 'Debes ser mayor de 18 años.';
            } else {
                fechaNacimientoError.value = '';
            }
        };

        return {
            paises,
            internalData,
            nombreError,
            dniError,
            fechaNacimientoError,
            validateNombre,
            validateDni,
            validateFechaNacimiento,
        };
    },
};
</script>

<style scoped>
.error {
    color: red;
}
</style>