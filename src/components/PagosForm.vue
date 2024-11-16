<!-- Practico3/src/components/PagosForm.vue -->
<template>
    <div class="card">
        <div class="card-body">
            <div class="container">
                <h5>Datos de Pago</h5>
                <div class="row">
                    <div class="col-md-12">
                        <label for="cardNumber" class="form-label">Número de Tarjeta:</label>
                        <input type="text" class="form-control" id="cardNumber" v-model.trim="internalData.cardNumber"
                            @input="detectCardType" placeholder="XXXX XXXX XXXX XXXX"
                            :class="{ 'is-valid': cardNumberValid, 'is-invalid': cardNumberError }" />
                        <img :src="cardTypeImage" v-if="cardTypeImage" alt="Card Type" class="card-logo mt-2">
                        <div v-if="cardNumberError" class="invalid-feedback">{{ cardNumberError }}</div>
                        <!--<div v-if="cardNumberValid" class="valid-feedback">Correcto!</div>-->

                    </div>
                </div>
                <div class="row mt-3">
                    <div class="col-md-6">
                        <label for="expiryDate" class="form-label">Fecha de Vencimiento:</label>
                        <input type="text" class="form-control" id="expiryDate" v-model="internalData.expiryDate"
                            @input="validateExpiryDate" placeholder="MM/AA"
                            :class="{ 'is-valid': expiryDateValid, 'is-invalid': expiryDateError }" />
                        <div v-if="expiryDateError" class="invalid-feedback">{{ expiryDateError }}</div>
                       <!-- <div v-if="expiryDateValid" class="valid-feedback">Correcto!</div>-->

                    </div>
                    <div class="col-md-6">
                        <label for="cvv" class="form-label">CVV:</label>
                        <input type="text" class="form-control" id="cvv" v-model="internalData.cvv" @input="validateCvv"
                            placeholder="CVV" :class="{ 'is-valid': cvvValid, 'is-invalid': cvvError }" />
                        <div v-if="cvvError" class="invalid-feedback">{{ cvvError }}</div>
                       <!-- <div v-if="cvvValid" class="valid-feedback">Correcto!</div>-->
                    </div>
                </div>
                <div class="row mt-3">
                    <div class="col-md-12">
                        <label for="cardName" class="form-label">Nombre en la Tarjeta:</label>
                        <input type="text" class="form-control" id="cardName" v-model="internalData.cardName"
                            @input="validateCardName" placeholder="Nombre Completo"
                            :class="{ 'is-valid': cardNameValid, 'is-invalid': cardNameError }" />
                        <div v-if="cardNameError" class="invalid-feedback">{{ cardNameError }}</div>
                        <!-- <div v-if="cardNameValid" class="valid-feedback">Correcto!</div>--> 
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { ref, watch, computed } from 'vue';
import visaImage from '../assets/visa.png'; // Importa las imágenes
import mastercardImage from '../assets/mastercard.png';
import amexImage from '../assets/amex.png';

export default {
    props: ['modelValue'],
    emits: ['update:modelValue'],

    setup(props, { emit }) {

        const internalData = ref({ ...props.modelValue });

        const cardNumberError = ref('');
        const expiryDateError = ref('');
        const cvvError = ref('');
        const cardNameError = ref('');

        const cardNumberValid = ref(false);
        const expiryDateValid = ref(false);
        const cvvValid = ref(false);
        const cardNameValid = ref(false);


        const cardType = ref(null);
        const cardTypeImage = ref(null);



        const detectCardType = () => {
            let number = internalData.value.cardNumber.replace(/\s/g, '');
            if (/^4[0-9]{12}(?:[0-9]{3})?$/.test(number)) {
                cardType.value = 'visa';
                cardTypeImage.value = visaImage;  // Asigna la imagen importada
            } else if (/^5[1-5][0-9]{14}$/.test(number)) {
                cardType.value = 'mastercard';
                cardTypeImage.value = mastercardImage; // Asigna la imagen importada
            } else if (/^3[47][0-9]{13}$/.test(number)) {
                cardType.value = 'amex';
                cardTypeImage.value = amexImage; // Asigna la imagen importada
            } else {
                cardType.value = null;
                cardTypeImage.value = null;
            }

            if (!/^[0-9]{16}$/.test(number)) {
                cardNumberError.value = "El número de tarjeta debe tener 16 dígitos";
                cardNumberValid.value = false;
            } else {
                cardNumberError.value = "";
                cardNumberValid.value = true;
            }
        };

        const validateExpiryDate = () => {

            if (!/^(0[1-9]|1[0-2])\/?([0-9]{4}|[0-9]{2})$/.test(internalData.value.expiryDate)) {
                expiryDateError.value = "La fecha de vencimiento debe tener el formato MM/AA o MM/AAAA";
                expiryDateValid.value = false;
            } else {
                expiryDateError.value = "";
                expiryDateValid.value = true;
            }
        }



        const validateCvv = () => {
            const cvvValue = internalData.value.cvv;

            if (!/^\d{3,4}$/.test(cvvValue)) {

                cvvError.value = "El CVV debe tener 3 o 4 dígitos";
                cvvValid.value = false;

            } else {

                cvvError.value = "";
                cvvValid.value = true;
            }

        }

        const validateCardName = () => {
            if (!/^[a-zA-Z\s]+$/.test(internalData.value.cardName)) {
                cardNameError.value = "El nombre en la tarjeta solo puede contener letras y espacios."
                cardNameValid.value = false;
            } else {
                cardNameError.value = "";
                cardNameValid.value = true;
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
            cardNumberValid,
            expiryDateValid,
            cvvValid,
            cardNameValid,
            detectCardType,
            cardTypeImage,
            validateExpiryDate,
            validateCvv,
            validateCardName
        };
    },
};

</script>
<style scoped>
.card-logo {
    max-width: 50px;
}
</style>