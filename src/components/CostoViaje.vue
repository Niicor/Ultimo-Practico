<!-- src/components/CostoViaje.vue -->
<template>
    <div class="card mt-3" :class="{ 'dark-mode': isDarkMode }">
        <div class="card-body">
            <h5 class="card-title">Costo del Viaje (USD)</h5>
            <h6 class="card-subtitle mb-2 text-info">(Precios en dólares estadounidenses)</h6>

            <table v-if="mostrarCostos" class="table table-bordered table-striped">
                <tbody>
                    <tr v-for="(item, index) in costos" :key="index"
                        :class="{ 'table-active': index % 2 === 1, 'total-row': item.name === 'Costo total' }">
                        <td>{{ item.name }}<span v-if="item.showBoletos"> ({{ item.boletos }} boletos)</span>:</td>
                        <td v-if="item.costo !== null">USD $ {{ item.costo.toFixed(2) }}</td>
                        <td v-else> - </td>
                    </tr>
                </tbody>
            </table>
            <div v-else class="text-center">
                Selecciona origen y destino para calcular el costo.
            </div>
        </div>
    </div>
</template>

<script>
import { computed, onMounted, ref, watch, onBeforeUnmount } from 'vue';

export default {
    props: {
        vueloData: Object,
        isDarkMode: Boolean,
    },
    setup(props) {
        const aeropuertos = ref([]);
        const costoBase = ref(null);
        let watcher;

        const calcularDistancia = (lat1, lon1, lat2, lon2) => {
            const R = 6371;
            const dLat = (lat2 - lat1) * Math.PI / 180;
            const dLon = (lon2 - lon1) * Math.PI / 180;
            const a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2);
            const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return R * c;
        };

        const calcularCostoBase = (origen, destino) => {
            const tarifaPorKm = 0.15;
            const aeropuertoOrigen = aeropuertos.value.find(a => a.city === origen);
            const aeropuertoDestino = aeropuertos.value.find(a => a.city === destino);

            if (aeropuertoOrigen && aeropuertoDestino) {
                const distancia = calcularDistancia(
                    parseFloat(aeropuertoOrigen.lat),
                    parseFloat(aeropuertoOrigen.lon),
                    parseFloat(aeropuertoDestino.lat),
                    parseFloat(aeropuertoDestino.lon)
                );
                return distancia * tarifaPorKm;
            }
            return null;
        };

        onMounted(async () => {
            try {
                const response = await fetch('/src/data/airport.json');
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                aeropuertos.value = await response.json();
            } catch (error) {
                console.error("Error al cargar aeropuertos:", error);
                alert("No se pudieron cargar los aeropuertos. Por favor, inténtalo de nuevo más tarde.");
            }
        });

        watcher = watch([
            () => props.vueloData.ciudadOrigen,
            () => props.vueloData.ciudadDestino,
            () => props.vueloData.claseVuelo,
            () => props.vueloData.numeroBoletos
        ], ([ciudadOrigen, ciudadDestino]) => {
            if (ciudadOrigen && ciudadDestino) {
                costoBase.value = calcularCostoBase(ciudadOrigen, ciudadDestino);
            } else {
                costoBase.value = null;
            }
        });

        onBeforeUnmount(() => {
            watcher(); // Detener el watch al destruir el componente
        });

        const costoExtraClase = computed(() => {
            let multiplicador = 0; // Inicializa en 0 para solo el extra
            switch (props.vueloData.claseVuelo) {
                case 'Ejecutiva':
                    multiplicador = 0.37; // 1.5 - 1 = 0.5
                    break;
                case 'Primera Clase':
                    multiplicador = 1.13; // 2 - 1 = 1
                    break;
            }
            return costoBase.value * multiplicador;
        });

        const costoPorBoleto = computed(() => costoBase.value + costoExtraClase.value);

        const costos = computed(() => {
            const costoIdaYVuelta = props.vueloData.tipoVuelo === 'idaYVuelta' ? costoPorBoleto.value * 2 : 0;

            // Inicializa los costos con valores null si no se han calculado aún.
            let costosArray = [
                { name: 'Costo base por trayecto', costo: costoBase.value || null, boletos: '', showBoletos: false },
                { name: 'Costo ida y vuelta', costo: costoIdaYVuelta || null, boletos: '', showBoletos: false },
                { name: `Costo extra por clase (${props.vueloData.claseVuelo || ''})`, costo: costoExtraClase.value || null, boletos: '', showBoletos: false },
                { name: 'Costo por boleto', costo: costoPorBoleto.value || null, boletos: '', showBoletos: false },
                { name: 'Costo total', costo: costoTotal.value || null, boletos: props.vueloData.numeroBoletos || '', showBoletos: true },
            ];

            if (props.vueloData.tipoVuelo === 'ida') {
                costosArray = costosArray.filter(item => item.name !== 'Costo ida y vuelta');
            }

            return costosArray;
        });

        const costoTotal = computed(() => {
            if (props.vueloData.tipoVuelo === 'idaYVuelta') {
                return costoPorBoleto.value * props.vueloData.numeroBoletos * 2; // Multiplicar por 2 para ida y vuelta
            } else {
                return costoPorBoleto.value * props.vueloData.numeroBoletos;
            }
        });

        const mostrarCostos = computed(() => props.vueloData.ciudadOrigen && props.vueloData.ciudadDestino);

        return {
            aeropuertos,
            costoBase,
            costoExtraClase,
            costoPorBoleto,
            costoTotal,
            calcularDistancia,
            calcularCostoBase,
            costos, // Retorna el nuevo computed property
            mostrarCostos, // Agregar mostrarCostos al objeto retornado
        };
    },
};
</script>

<style scoped>
/* Estilos para el modo oscuro */
.dark-mode {
    background-color: #343a40;
    color: #f8f9fa;
}

.dark-mode .card {
    background-color: #454d55;
    color: #f8f9fa;
}

.dark-mode table {
    color: #f8f9fa;
}

.dark-mode th,
.dark-mode td {
    border-color: #6c757d;
    /* Color de borde de la tabla en modo oscuro */
}

.dark-mode p {
    color: white;
}

.dark-mode h5 {
    color: white;
}

/* Resaltado de la fila del costo total */
.total-row {
    font-weight: bold;
    background-color: #d1e7dd;
    /* Verde claro para modo claro */
    color: #198754;
}

.dark-mode .total-row {
    background-color: #212f3c;
    /* Un tono más oscuro para el modo oscuro */
    color: #5cb85c;
    /* Cambia el texto a verde para el modo oscuro*/
}

/* Estilo cebra para filas impares */
.table-striped>tbody>tr:nth-of-type(odd)>* {
    --bs-table-accent-bg: rgba(0, 0, 0, 0.05);
    color: var(--bs-table-striped-color);
}

.dark-mode .table-striped>tbody>tr:nth-of-type(odd)>* {
    --bs-table-accent-bg: rgba(255, 255, 255, 0.05);
    /* Para modo oscuro */
}

.dark-mode .table>:not(caption)>*>* {
    border-color: #454d55;
    /* Ajusta el color de borde en modo oscuro si es necesario */
}
</style>