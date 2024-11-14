<!-- Practico3/src/components/PagosForm.vue -->
<template>
    <div>
        <h3>Datos de Pago</h3>

        <label for="cardNumber">Número de Tarjeta:</label>
        <input type="text" id="cardNumber" v-model.trim="internalData.cardNumber" @input="detectCardType"
            placeholder="XXXX XXXX XXXX XXXX" />
        <img :src="cardTypeImage" v-if="cardTypeImage" alt="Card Type" class="card-logo">
        <div v-if="cardNumberError" class="error">{{ cardNumberError }}</div>


        <label for="expiryDate">Fecha de Vencimiento (MM/AA):</label>
        <input type="text" id="expiryDate" v-model="internalData.expiryDate" @input="validateExpiryDate"
            placeholder="MM/AA" />
        <div v-if="expiryDateError" class="error">{{ expiryDateError }}</div>


        <label for="cvv">CVV:</label>
        <input type="text" id="cvv" v-model="internalData.cvv" @input="validateCvv" placeholder="CVV" />
        <div v-if="cvvError" class="error">{{ cvvError }}</div>



        <label for="cardName">Nombre en la Tarjeta:</label>
        <input type="text" id="cardName" v-model="internalData.cardName" @input="validateCardName"
            placeholder="Nombre Completo" />
        <div v-if="cardNameError" class="error">{{ cardNameError }}</div>

    </div>
</template>

<script>
import { ref, watch, computed } from 'vue';

export default {
    props: ['modelValue'],
    emits: ['update:modelValue'],

    setup(props, { emit }) {

        const internalData = ref({ ...props.modelValue });


        const cardNumberError = ref('');
        const expiryDateError = ref('');
        const cvvError = ref('');
        const cardNameError = ref('');


        const cardType = ref(null); // Para almacenar el tipo de tarjeta
        const cardTypeImage = ref(null); // Para la URL de la imagen



        const detectCardType = () => {
            let number = internalData.value.cardNumber.replace(/\s/g, ''); // Eliminar espacios
            if (/^4[0-9]{12}(?:[0-9]{3})?$/.test(number)) {
                cardType.value = 'visa';
                cardTypeImage.value = '/src/assets/visa.png'; // Ruta a tu imagen de Visa
            } else if (/^5[1-5][0-9]{14}$/.test(number)) {
                cardType.value = 'mastercard';
                cardTypeImage.value = '/src/assets/mastercard.png'; // Ruta a tu imagen de Mastercard
            } else if (/^3[47][0-9]{13}$/.test(number)) {
                cardType.value = 'amex';
                cardTypeImage.value = '/src/assets/amex.png'; // Ruta a tu imagen de Amex
            } else {
                cardType.value = null;
                cardTypeImage.value = null;
            }

            if (!/^[0-9]{16}$/.test(number)) {
                cardNumberError.value = "El número de tarjeta debe tener 16 dígitos";
            } else {
                cardNumberError.value = "";
            }
        };

        const validateExpiryDate = () => {

            if (!/^(0[1-9]|1[0-2])\/?([0-9]{4}|[0-9]{2})$/.test(internalData.value.expiryDate)) {
                expiryDateError.value = "La fecha de vencimiento debe tener el formato MM/AA o MM/AAAA";

            } else {
                expiryDateError.value = "";
            }
        }



        const validateCvv = () => {
            const cvvValue = internalData.value.cvv;

            if (!/^\d{3,4}$/.test(cvvValue)) {

                cvvError.value = "El CVV debe tener 3 o 4 dígitos";

            } else {

                cvvError.value = "";
            }

        }

        const validateCardName = () => {
            if (!/^[a-zA-Z\s]+$/.test(internalData.value.cardName)) {
                cardNameError.value = "El nombre en la tarjeta solo puede contener letras y espacios."
            } else {
                cardNameError.value = "";
            }
        };


        watch(internalData, (newValue) => {
            emit('update:modelValue', newValue);
        }, { deep: true });




        return {
            internalData,
            cardNumberError,
            expiryDateError,
            cvvError,
            cardNameError,
            detectCardType,
            cardTypeImage,
            validateExpiryDate,
            validateCvv,
            validateCardName
        };
    },
};

</script>