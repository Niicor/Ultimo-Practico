<!-- src/components/ResumenReserva.vue -->
<template>
    <div class="card mt-4" :class="{ 'dark-mode': isDarkMode }">
        <div class="card-header">
            Resumen de tu Reserva
        </div>
        <div class="card-body">
            <h5 class="card-title">Datos del Pasajero</h5>
            <p v-if="pasajero">Nombre: {{ pasajero.nombre }}</p>
            <p v-if="pasajero">DNI/Pasaporte: {{ pasajero.dni }}</p>
            <p v-if="pasajero">Fecha de Nacimiento: {{ formatDate(pasajero.fechaNacimiento) }}</p>
            <p v-if="pasajero">Nacionalidad: {{ pasajero.nacionalidad }}</p>

            <h5 class="card-title mt-3">Datos del Vuelo</h5>
            <p v-if="vuelo">Ciudad de Origen: {{ vuelo.ciudadOrigen }}</p>
            <p v-if="vuelo">Ciudad de Destino: {{ vuelo.ciudadDestino }}</p>
            <p v-if="vuelo">Tipo de Vuelo: {{ vuelo.tipoVuelo }}</p>
            <p v-if="vuelo">Fecha de Salida: {{ formatDate(vuelo.fechaSalida) }}</p>
            <p v-if="vuelo && vuelo.tipoVuelo === 'idaYVuelta'">Fecha de Regreso: {{ formatDate(vuelo.fechaRegreso) }}
            </p>
            <p v-if="vuelo">Clase: {{ vuelo.claseVuelo }}</p>
            <p v-if="vuelo">Número de Boletos: {{ vuelo.numeroBoletos }}</p>


            <h5 class="card-title mt-3">Datos de Pago</h5>
            <p v-if="pago">Número de Tarjeta: {{ pago.cardNumber }}</p>
            <p v-if="pago">Fecha de Vencimiento: {{ pago.expiryDate }}</p>
            <p v-if="pago">CVV: ***</p> <!-- No mostramos el CVV -->
            <p v-if="pago">Nombre en la Tarjeta: {{ pago.cardName }}</p>


            <!-- Resumen del precio total (implementación básica, puedes extenderla) -->
            <div v-if="vuelo && pago" class="mt-3">
                <h5>Precio Total</h5>
                <p> ${{ calculateTotalPrice() }} </p>
            </div>

        </div>
    </div>
</template>

<script>
export default {
    props: {
        pasajero: Object,
        vuelo: Object,
        pago: Object,
        isDarkMode: Boolean
    },
    methods: {
        formatDate(dateString) {
            if (!dateString) return '';
            const date = new Date(dateString);
            const options = { year: 'numeric', month: 'long', day: 'numeric' };
            return date.toLocaleDateString(undefined, options);
        },
        calculateTotalPrice() {
            // Implementación básica de cálculo de precio. Ajústala según tus necesidades.
            let basePrice = 100; // Precio base por boleto
            switch (this.vuelo.claseVuelo) {
                case 'Ejecutiva':
                    basePrice *= 1.5;
                    break;
                case 'Primera Clase':
                    basePrice *= 2;
                    break;
            }
            return basePrice * this.vuelo.numeroBoletos;
        }
    },
};
</script>
<style scoped>
.dark-mode .card {
    background-color: #454d55;
    color: #f8f9fa;
}
</style>