<!-- src/components/CostoViaje.vue -->
<template>
    <div class="card mt-3" :class="{ 'dark-mode': isDarkMode }">
        <div class="card-body">
            <h5 class="card-title">Costo del Viaje</h5>

            <table v-if="costoBase" class="table table-bordered">
                <tbody>
                    <tr>
                        <td>Costo base por trayecto:</td>
                        <td>$ {{ costoBase.toFixed(2) }}</td>
                    </tr>
                    <tr v-if="vueloData.tipoVuelo === 'idaYVuelta'">
                        <td>Costo ida y vuelta:</td>
                        <td>$ {{ (costoBase * 2).toFixed(2) }}</td>
                    </tr>
                    <tr>
                        <td>Costo por clase ({{ vueloData.claseVuelo }}):</td> <!-- Mostrar la clase seleccionada -->
                        <td>$ {{ costoClase.toFixed(2) }}</td>
                    </tr>
                    <tr>
                        <td>Costo por boleto:</td>
                        <td>$ {{ costoPorBoleto.toFixed(2) }}</td>
                    </tr>
                    <tr>
                        <td>Costo total ({{ vueloData.numeroBoletos }} boletos):</td>
                        <td>$ {{ costoTotal.toFixed(2) }}</td>
                    </tr>
                </tbody>
            </table>

            <div v-else-if="vueloData.ciudadOrigen && vueloData.ciudadDestino">Calculando costo...</div>
            <div v-else>Selecciona origen y destino para calcular el costo.</div>
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

        const costoClase = computed(() => {
            let multiplicador = 1;
            switch (props.vueloData.claseVuelo) {
                case 'Ejecutiva':
                    multiplicador = 1.5;
                    break;
                case 'Primera Clase':
                    multiplicador = 2;
                    break;
            }
            return costoBase.value * multiplicador;
        });

        const costoPorBoleto = computed(() => costoClase.value);

        const costoTotal = computed(() => costoPorBoleto.value * props.vueloData.numeroBoletos);

        return {
            aeropuertos,
            costoBase,
            costoClase,
            costoPorBoleto,
            costoTotal,
            calcularDistancia,
            calcularCostoBase,
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
</style>