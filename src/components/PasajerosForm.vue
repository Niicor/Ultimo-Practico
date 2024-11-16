<!-- src/components/PasajerosForm.vue -->
<template>
    <div class="card">
        <div class="card-body">
            <h5 class="card-title">Datos del Pasajero</h5>
            <div class="mb-3">
                <label for="nombre" class="form-label">Nombre Completo:</label>
                <input type="text" id="nombre" v-model.trim="internalData.nombre" @input="validateNombre"
                    class="form-control" placeholder="Nombre Completo" />
                <div v-if="nombreError" class="text-danger">{{ nombreError }}</div>
            </div>

            <div class="mb-3">
                <label for="dni" class="form-label">Número de Pasaporte/DNI (Argentina):</label>
                <input type="text" id="dni" v-model.trim="internalData.dni" @input="validateDni" class="form-control"
                    placeholder="DNI/Pasaporte" />
                <div v-if="dniError" class="text-danger">{{ dniError }}</div>
            </div>

            <div class="mb-3">
                <label for="fechaNacimiento" class="form-label">Fecha de Nacimiento (dd/mm/yyyy):</label>
                <input type="text" id="fechaNacimiento" v-model="internalData.fechaNacimiento"
                    @input="validateFechaNacimiento" class="form-control" placeholder="dd/mm/yyyy" />
                <div v-if="fechaNacimientoError" class="text-danger">{{ fechaNacimientoError }}</div>
            </div>

            <div class="mb-3">
                <label for="nacionalidad" class="form-label">Nacionalidad:</label>
                <select id="nacionalidad" v-model="internalData.nacionalidad" class="form-select">
                    <option value="" disabled>Selecciona tu nacionalidad</option>
                    <option v-for="pais in paises" :key="pais.iso3" :value="pais.nameES"> {{ pais.nameES }} </option>
                </select>
                <div v-if="!internalData.nacionalidad" class="text-danger">Debes seleccionar una nacionalidad.</div>
            </div>
        </div>
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
                const data = await response.json();
                // Asegura que 'paises' sea un array antes de asignarlo
                paises.value = Array.isArray(data) ? data : [];
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