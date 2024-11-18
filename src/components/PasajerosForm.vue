<!-- src/components/PasajerosForm.vue -->
<template>
    <div class="card">
        <div class="card-body">
            <h5 class="card-title">Datos del Pasajero</h5>
            <form class="row g-3 needs-validation" novalidate @submit.prevent="submitForm">
                <div class="mb-3">
                    <label for="nombre" class="form-label">Nombre Completo:</label>
                    <input type="text" id="nombre" v-model.trim="internalData.nombre" @input="validateNombre"
                        class="form-control"
                        :class="{ 'is-invalid': nombreError, 'is-valid': !nombreError && internalData.nombre }"
                        placeholder="Nombre Completo" />
                    <div v-if="nombreError" class="invalid-feedback">{{ nombreError }}</div>
                </div>

                <div class="mb-3">
                    <label for="dni" class="form-label">Número de Pasaporte/DNI (Argentina):</label>
                    <input type="text" id="dni" v-model.trim="internalData.dni" @input="validateDni"
                        class="form-control"
                        :class="{ 'is-invalid': dniError, 'is-valid': !dniError && internalData.dni }"
                        placeholder="DNI/Pasaporte" />
                    <div v-if="dniError" class="invalid-feedback">{{ dniError }}</div>
                </div>

                <div class="mb-3">
                    <label for="fechaNacimiento" class="form-label">Fecha de Nacimiento:</label>
                    <input type="date" id="fechaNacimiento" v-model="internalData.fechaNacimiento"
                        @input="validateFechaNacimiento" class="form-control"
                        :class="{ 'is-invalid': fechaNacimientoError, 'is-valid': !fechaNacimientoError && internalData.fechaNacimiento }"
                        :max="maxDate" />
                    <div v-if="fechaNacimientoError" class="invalid-feedback">{{ fechaNacimientoError }}</div>
                </div>

                <div class="mb-3">
                    <label for="nacionalidad" class="form-label">Nacionalidad:</label>
                    <select id="nacionalidad" v-model="internalData.nacionalidad" class="form-select"
                        :class="{ 'is-invalid': !internalData.nacionalidad, 'is-valid': internalData.nacionalidad }">
                        <option value="" disabled>Selecciona tu nacionalidad</option>
                        <option v-for="pais in paises" :key="pais.iso3" :value="pais.nameES"> {{ pais.nameES }}
                        </option>
                    </select>
                    <div v-if="!internalData.nacionalidad" class="invalid-feedback">Debes seleccionar una nacionalidad.
                    </div>
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
        const internalData = ref({ ...props.modelValue });
        const paises = ref([]);
        const nombreError = ref('');
        const dniError = ref('');
        const fechaNacimientoError = ref('');
        const maxDate = ref('');

        onMounted(async () => {
            try {
                const response = await fetch('/countries.json');
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const data = await response.json();
                // Asegura que 'paises' sea un array antes de asignarlo
                paises.value = Array.isArray(data) ? data : [];
            } catch (error) {
                console.error("Error al cargar países:", error);
                
            }

            // Calcular la fecha máxima permitida (fecha actual)
            const today = new Date();
            maxDate.value = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`;
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
            if (!inputDate) {
                fechaNacimientoError.value = 'Debes seleccionar una fecha de nacimiento.';
                return;
            }

            const date = new Date(inputDate);

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
            } else if (age > 115) {  // Nueva validación
                fechaNacimientoError.value = 'La edad máxima permitida es 115 años.';
            } else {
                fechaNacimientoError.value = '';
            }
        };

        const submitForm = () => {
            // Aquí puedes agregar la lógica para validar todos los campos antes de enviar el formulario
            validateNombre();
            validateDni();
            validateFechaNacimiento();

            // Si no hay errores, puedes enviar el formulario
            if (!nombreError.value && !dniError.value && !fechaNacimientoError.value && internalData.value.nacionalidad) {
                // Aquí puedes agregar la lógica para enviar el formulario
                console.log("Formulario enviado:", internalData.value);
            }
        };

        return {
            paises,
            internalData,
            nombreError,
            dniError,
            fechaNacimientoError,
            maxDate,
            validateNombre,
            validateDni,
            validateFechaNacimiento,
            submitForm
        };
    },
};
</script>

<style scoped>
.is-valid {
    border-color: #198754 !important
}

.error {
    color: red;
}
</style>