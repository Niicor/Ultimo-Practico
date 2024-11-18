<!-- src/components/ResumenReserva.vue -->
<template>
    <div class="mt-4" :class="{ 'dark-mode': isDarkMode }">
        <div class="row">
            <div class="col-md-4">
                <div class="card">
                    <div class="card-header">Datos del Pasajero</div>
                    <div class="card-body">
                        <p v-if="pasajero">Nombre: {{ pasajero.nombre }}</p>
                        <p v-if="pasajero">DNI/Pasaporte: {{ pasajero.dni }}</p>
                        <p v-if="pasajero">Fecha de Nacimiento: {{ formatDate(pasajero.fechaNacimiento) }}</p>
                        <p v-if="pasajero">Nacionalidad: {{ pasajero.nacionalidad }}</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card">
                    <div class="card-header">Datos del Vuelo</div>
                    <div class="card-body">
                        <p v-if="vuelo">Ciudad de Origen: {{ vuelo.ciudadOrigen }}</p>
                        <p v-if="vuelo">Ciudad de Destino: {{ vuelo.ciudadDestino }}</p>
                        <p v-if="vuelo">Tipo de Vuelo: {{ vuelo.tipoVuelo }}</p>
                        <p v-if="vuelo">Fecha de Salida: {{ formatDate(vuelo.fechaSalida) }}</p>
                        <p v-if="vuelo && vuelo.tipoVuelo === 'idaYVuelta'">Fecha de Regreso: {{
                            formatDate(vuelo.fechaRegreso) }}</p>
                        <p v-if="vuelo">Clase: {{ vuelo.claseVuelo }}</p>
                        <p v-if="vuelo">Número de Boletos: {{ vuelo.numeroBoletos }}</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card">
                    <div class="card-header">Datos de Pago</div>
                    <div class="card-body">
                        <img :src="cardLogo" v-if="cardLogo" alt="Card Type" class="card-logo mb-3">
                        <p v-if="pago">Número de Tarjeta: {{ pago.cardNumber }}</p>
                        <p v-if="pago">Fecha de Vencimiento: {{ pago.expiryDate }}</p>
                        <p v-if="pago">Nombre en la Tarjeta: {{ pago.cardName }}</p>
                    </div>
                </div>
            </div>
        </div>

        <div class="card mt-4" :class="{ 'dark-mode': isDarkMode }">
            <div class="card-body">
                <!-- Resumen del precio total -->
                <div v-if="costoTotal"> <!-- Mostrar solo si costoTotal existe -->
                    <h5>Precio Total</h5>
                    <p> ${{ costoTotal }} </p> <!-- Usar la prop costoTotal -->
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import visaImage from '../assets/visa.png';
import mastercardImage from '../assets/mastercard.png';
import amexImage from '../assets/amex.png';

export default {
    props: {
        pasajero: Object,
        vuelo: Object,
        pago: Object,
        isDarkMode: Boolean,
        costoTotal: Number,  // Añadir la prop costoTotal
    },
    computed: {
        cardLogo() {
            if (this.pago && this.pago.cardNumber) {
                let number = this.pago.cardNumber.replace(/\s/g, '');
                if (/^4/.test(number)) {
                    return visaImage;
                } else if (/^5[1-5]/.test(number)) {
                    return mastercardImage;
                } else if (/^3[47]/.test(number)) {
                    return amexImage;
                }
            }
            return null;
        },
    },
    methods: {
        formatDate(dateString) {
            if (!dateString) return '';
            const date = new Date(dateString);
            const options = { year: 'numeric', month: 'long', day: 'numeric' };
            return date.toLocaleDateString(undefined, options);
        },
    },
};
</script>

<style scoped>
.card-logo {
    max-width: 200px;
    /* Ajusta el tamaño del logo según tus necesidades */
}

.dark-mode .card {
    background-color: #454d55;
    color: #f8f9fa;
}

.dark-mode p {
    color: white
}

.dark-mode h5 {
    color: white
}
</style>