<!-- src/components/Footer.vue -->
<template>
    <footer class="bg-light text-center text-lg-start mt-auto" :class="{ 'bg-dark text-white': isDarkMode }">
        <div class="container p-4">
            <div class="row">
                <div class="col-lg-6 col-md-12 mb-4 mb-md-0">
                    <img src="/vuelos.svg" alt="Logo" width="30" height="24"
                        class="d-inline-block align-text-top me-2 mb-2">
                    <p>
                        Desarrollado por el Grupo Archivo:
                    </p>
                    <ul class="list-unstyled mb-0">
                        <li>Facundo Maldonado</li>
                        <li>Nicolas Rivero</li>
                        <li>Ignacio Leguizamon</li>
                    </ul>
                </div>
                <div class="col-lg-6 col-md-12 mb-4 mb-md-0">
                    <!-- Puedes agregar más información aquí si lo deseas, como enlaces a redes sociales, etc. -->
                </div>
            </div>
        </div>

        <div class="text-center p-3" :class="{ 'bg-dark text-white': isDarkMode }"
            style="background-color: rgba(0, 0, 0, 0.05);">
            <!-- Puedes agregar un copyright u otra información aquí -->
        </div>

    </footer>
</template>

<script>
export default {
    props: {
        isDarkMode: Boolean,
    },
};
</script>