<!-- src/components/ResumenReserva.vue -->
<template>
    <div class="resumen-container mt-4" :class="{ 'dark-mode': isDarkMode }">
        <div class="card resumen-card">
            <div class="card-header resumen-header">
                <h3>Resumen de tu Reserva</h3>
                <p>Verifica los detalles de tu reserva antes de confirmar.</p>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-4">
                        <h5>Datos del Pasajero</h5>
                        <p v-if="pasajero">Nombre: {{ pasajero.nombre }}</p>
                        <p v-if="pasajero">DNI/Pasaporte: {{ pasajero.dni }}</p>
                        <p v-if="pasajero">Fecha de Nacimiento: {{ formatDate(pasajero.fechaNacimiento) }}</p>
                        <p v-if="pasajero">Nacionalidad: {{ pasajero.nacionalidad }}</p>
                    </div>
                    <div class="col-md-4">
                        <h5>Datos del Vuelo</h5>
                        <p v-if="vuelo">Ciudad de Origen: {{ vuelo.ciudadOrigen }}</p>
                        <p v-if="vuelo">Ciudad de Destino: {{ vuelo.ciudadDestino }}</p>
                        <p v-if="vuelo">Tipo de Vuelo: {{ vuelo.tipoVuelo }}</p>
                        <p v-if="vuelo">Fecha de Salida: {{ formatDate(vuelo.fechaSalida) }}</p>
                        <p v-if="vuelo && vuelo.tipoVuelo === 'idaYVuelta'">Fecha de Regreso: {{
                            formatDate(vuelo.fechaRegreso) }}</p>
                        <p v-if="vuelo">Clase: {{ vuelo.claseVuelo }}</p>
                        <p v-if="vuelo">Número de Boletos: {{ vuelo.numeroBoletos }}</p>
                    </div>
                    <div class="col-md-4">
                        <h5>Datos de Pago</h5>
                        <img :src="cardLogo" v-if="cardLogo" alt="Card Type" class="card-logo mb-3">
                        <p v-if="pago">Número de Tarjeta: {{ pago.cardNumber }}</p>
                        <p v-if="pago">Fecha de Vencimiento: {{ pago.expiryDate }}</p>
                        <p v-if="pago">Nombre en la Tarjeta: {{ pago.cardName }}</p>

                    </div>
                </div>
                <div class="row mt-4">
                    <div class="col-md-12">
                        <!-- Resumen del precio total -->
                        <div class="precio-total" v-if="costoTotal">
                            <h4>Precio Total</h4>
                            <p class="precio-valor">USD $ {{ costoTotalFormatted }} </p>
                            <!-- Mostrar el valor formateado -->
                        </div>
                    </div>
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
        costoTotalFormatted() {  // Nueva propiedad computada
            return this.costoTotal ? this.costoTotal.toFixed(2) : 0;
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
.resumen-container {
    margin-top: 20px;
    /* Espacio superior */
}

.resumen-card {
    border: 2px solid #007bff;
    /* Borde azul en modo claro */
    border-radius: 15px;
    /* Bordes redondeados */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    /* Sombra sutil */
}

.dark-mode .resumen-card {
    border-color: #5cb85c;
    /* Borde verde en modo oscuro */
    background-color: #4a5568;
    color: #f7fafc;

}

.resumen-header {
    background-color: #e9ecef;
    /* Gris claro para modo claro */
    color: #343a40;
    /* Texto oscuro para modo claro */
    padding: 15px;
    /* Espaciado interno */
    border-top-left-radius: 15px;
    /* Bordes redondeados en la cabecera */
    border-top-right-radius: 15px;
}

.dark-mode .resumen-header {
    background-color: #2d3748;
    /* Un tono más oscuro para el modo oscuro */
    color: #f7fafc;
}

.dark-mode p {
    color: #f7fafc;
}

.dark-mode h5 {
    color: #f7fafc;
}

.precio-total {
    text-align: center;
    margin-top: 20px;
}

.precio-valor {
    font-size: 24px;
    font-weight: bold;
    color: #28a745;
    /* Verde para el precio */
}

.dark-mode .precio-valor {
    color: #5cb85c;
}

.card-logo {
    max-width: 200px;
    /* Ajusta el tamaño del logo según tus necesidades */
}
</style>