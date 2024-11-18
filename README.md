# Reserva de Vuelos - Proyecto Vue 3 con Vite

Este proyecto implementa un formulario de reserva de vuelos con validación completa utilizando Vue 3, Vite, y Bootstrap. Ofrece una experiencia de usuario interactiva con validación en tiempo real y retroalimentación visual.

## Instrucciones para principiantes con Node y npm

Si eres nuevo en el desarrollo web con Node.js y npm, sigue estos pasos para configurar y ejecutar el proyecto:

1. **Instalar Node.js y npm:** Descarga e instala la última versión de Node.js desde [https://nodejs.org/](https://nodejs.org/). npm se instalará automáticamente junto con Node.js.

2. **Clonar el repositorio:** Abre una terminal o línea de comandos y navega hasta el directorio donde deseas guardar el proyecto. Luego, clona el repositorio con el siguiente comando:

   ```bash
   git clone [text](https://github.com/Niicor/Ultimo-Practico.git)
   ```

3. **Navegar al directorio del proyecto:**

   ```bash
   cd ULTIMO-PRACTICO  (o el nombre de la carpeta de tu proyecto)
   ```

4. **Instalar las dependencias:** Ejecuta el siguiente comando para instalar las bibliotecas necesarias:

   ```bash
   npm install
   ```

5. **Iniciar el servidor de desarrollo:** Ejecuta el siguiente comando para iniciar el servidor de desarrollo de Vite:

   ```bash
   npm run dev
   ```

   Esto abrirá automáticamente el proyecto en tu navegador web. Si no es así, abre manualmente  `http://localhost:5173/` (o el puerto que indique la terminal).

## Ventajas de usar Vite

Vite es una herramienta de construcción de frontend que ofrece un flujo de trabajo de desarrollo mucho más rápido y eficiente en comparación con otras herramientas como Webpack. Algunas de sus ventajas clave incluyen:

* **Servido de módulos nativo (ESM):** Vite aprovecha el soporte nativo de ESM en los navegadores modernos, lo que elimina la necesidad de agrupar el código antes de servirlo. Esto resulta en un inicio instantáneo del servidor y una recarga de módulos en caliente (HMR) increíblemente rápida.

* **Optimización de la producción basada en Rollup:** Vite utiliza Rollup para la construcción de la producción, lo que garantiza un tamaño de paquete pequeño y un rendimiento optimizado.

* **Configuración mínima:** Vite requiere una configuración mínima, lo que te permite concentrarte en el desarrollo de tu aplicación en lugar de configurar herramientas complejas.

* **Rica en funciones:**  Vite ofrece una gran cantidad de funciones útiles, como soporte para TypeScript, JSX, CSS preprocesadores, y más.

## Estructura del Proyecto

La estructura del proyecto es la siguiente:

```
ULTIMO-PRACTICO/
├── .vite
├── .vscode
├── node_modules
├── public/
│   └── vuelos.svg
├── src/
│   ├── assets
│   │    ├── amex.png
│   │    ├── mastercard.png
│   │    └── visa.png
│   ├── components/
│   │    ├── CostoViaje.vue
│   │    ├── Footer.vue
│   │    ├── Formulario.vue
│   │    ├── Navbar.vue
│   │    ├── PagosForm.vue
│   │    ├── PasajerosForm.vue
│   │    ├── ResumenReserva.vue
│   │    └── VueloForm.vue
│   ├── data/
│   │    ├── airport.json
│   │    └── countries.json
│   ├── App.vue
│   ├── main.js
│   └── styles.css
├── .eslintrc.js
├── .gitignore
├── .prettierrc
├── index.html
├── jsconfig.json
├── package-lock.json
├── package.json
├── README.md
└── vite.config.js
```

## Requisitos del Proyecto

El formulario de reserva de vuelos debe cumplir con los siguientes requisitos:

* **Validación completa de todos los campos:** Incluyendo nombre, pasaporte/DNI, fecha de nacimiento, ciudad de origen, ciudad de destino, fechas de vuelo, clase de vuelo, número de boletos, número de tarjeta de crédito, fecha de vencimiento, CVV, y nombre en la tarjeta.
* **Validación en tiempo real con retroalimentación visual:** Los errores se muestran en rojo y los campos válidos en verde.
* **Deshabilitar el botón de envío hasta que todos los campos sean válidos.**
* **Mostrar un resumen de la reserva antes de confirmar el pago.**

## Puntos Extra Implementados

* **Detección automática del tipo de tarjeta de crédito.**
* **Cálculo del precio total en tiempo real.**
