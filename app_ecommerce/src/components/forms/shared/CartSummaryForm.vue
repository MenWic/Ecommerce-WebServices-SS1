<template>
  <v-card class="mx-auto" max-width="300">
    <v-form>
      <v-text-field v-model="subtotal" label="Subtotal" readonly>
        <template v-slot:append-inner>Q</template>
      </v-text-field>
      <v-text-field v-model="subtotalImpuestos" label="Impuestos" readonly>
        <template v-slot:append-inner>Q</template>
      </v-text-field>
      <h4 class="ml-2">Opcion de Entrega</h4>
      <v-radio-group v-model="opcionEntrega" inline @change="updateDelivery">
        <v-radio label="Retiro en Tienda" value="0"></v-radio>
        <v-radio label="Envio a Domicilio" value="1"></v-radio>
      </v-radio-group>
      <v-text-field
        v-model="direccion"
        v-if="opcionEntrega == '1'"
        label="Direccion de Entrega"
        :required="opcionEntrega == '1'"
      />
      <v-text-field v-model="costoEnvio" label="Costo Envio" readonly>
        <template v-slot:append-inner>Q</template>
      </v-text-field>

      <h4 class="ml-2">Metodo de Pago</h4>
      <v-radio-group v-model="metodoPago" inline @change="updateDelivery">
        <v-radio label="Pago contra Entrega" value="0"></v-radio>
        <v-radio label="Pasarela de Pago" value="pasarela"></v-radio>
        <!-- <v-radio label="Tarjeta de Credito/Debito" value="1"></v-radio> -->
      </v-radio-group>

      <!-- AQUI DEBERIAMOS IMPLEMENTAR EL "ELEGIR LA PASARELA DE PAGO" -->
      <!-- VERIFCAR SI EL EMAIL DE ESTA CUENTA EXISTE EN LA PASARELA ELEGIDA -->
      <!-- SI EXISTE EL EMAIL EN LA PASARELA, PROCEDER A ENVIAR EL MONTO A DEBITAR A DICHO EMAIL -->

      <!-- Selección de Pasarela y Tipo de Transacción -->
      <v-container v-if="metodoPago === 'pasarela'" class="pasarela-options">
        <h4 class="mb-2">Seleccione Pasarela y Tipo de Transacción</h4>

        <!-- Selección de la Pasarela -->
        <v-radio-group v-model="selectedPasarela" row>
          <v-radio label="Chiltepago" value="A"></v-radio>
          <v-radio label="AlexisPagos" value="B"></v-radio>
        </v-radio-group>

        <!-- Selección del Tipo de Transacción -->
        <v-radio-group v-model="selectedTipoTransaccion" row>
          <v-radio label="Tarjeta" value="credit_card"></v-radio>
          <v-radio label="Bancaria" value="bank_account"></v-radio>
        </v-radio-group>
      </v-container>

      <!-- Pago Contra Entrega -->
      <v-text-field v-else v-model="subtotalContraEntrega" label="Cargo Pago Contra Entrega" readonly>
        <template v-slot:append-inner>Q</template>
      </v-text-field>

      <!-- Total -->
      <v-text-field v-model="total" label="Total" readonly>
        <template v-slot:append-inner>Q</template>
      </v-text-field>

      <!-- NIT de Facturación -->
      <h4 class="ml-2">NIT de Facturacion</h4>
      <v-switch
        class="ml-2"
        v-model="consumidorFinal"
        :color="consumidorFinal ? 'green' : 'red'"
        :label="consumidorFinal ? `${user?.nit ?? 'No tienes un NIT asociado'}` : 'Consumidor Final'"
      ></v-switch>

       <!-- Botón Comprar -->
      <v-btn width="100%" prepend-icon="mdi-cart-outline" @click="comprar">Comprar</v-btn>
    </v-form>
  </v-card>
</template>

<script setup lang="ts">
import { useConfigsStore } from '@/stores/config'
import { storeToRefs } from 'pinia'
import { computed, ref } from 'vue'
import { useAuthStore } from '../../../stores/auth'
import { type User } from '../../../stores/regular-auth'

const { user } = storeToRefs(useAuthStore())

const props = defineProps({
  subtotalProp: {
    type: Number,
    default: 0
  },
  taxProp: {
    type: Number,
    default: 0
  },
  codProp: {
    type: Number,
    default: 0
  }
})

const subtotal = computed(() => {
    return props.subtotalProp
})
const subtotalImpuestos = computed(() => {
    return props.taxProp
})
const subtotalContraEntrega = computed(() => {
    return props.codProp
})

const costoEnvio = ref(0)
const opcionEntrega = ref('0')
const direccion = ref('')

const metodoPago = ref('0') // '0' para pago contra entrega, 'pasarela' para pasarela de pago
const selectedPasarela = ref(null) // 'A' para Chiltepago, 'B' para AlexisPagos
const selectedTipoTransaccion = ref(null) // 'credit_card' o 'bank_account'

//const numeroTarjeta = ref('')
//const cvvTarjeta = ref('')
const total = computed(() => {
    const total1 = subtotal.value + subtotalImpuestos.value + costoEnvio.value;
    return metodoPago.value == '0' ? total1 + subtotalContraEntrega.value : total1
})
const consumidorFinal = ref(true)

const emits = defineEmits(['buy'])

function comprar() {
  const attributesSale = {
    consumidorFinal: !consumidorFinal.value,
    retiroEnTienda: opcionEntrega.value == '0' ? true : false,
    pagoContraEntrega: metodoPago.value == '0' ? true : false,
    direccion: direccion.value,
    pasarela: metodoPago.value === 'pasarela' ? selectedPasarela.value : null,
    tipo_transaccion: metodoPago.value === 'pasarela' ? selectedTipoTransaccion.value : null
  }
  console.log('emite')
  emits('buy', attributesSale)
}

function updateDelivery() {
  if (opcionEntrega.value === '0') {
    costoEnvio.value = 0
  } else {
    const { deliveryCost } = storeToRefs(useConfigsStore())
    costoEnvio.value = deliveryCost.value
  }
}

</script>
