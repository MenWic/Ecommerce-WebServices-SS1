<template>
    <h1>Carrito</h1>
    <v-row>
        <v-col cols="12" xs="6" sm="6">
            <h2>Productos</h2>
            <CartProductList :products="products" @remove-product="removerProducto"/>
        </v-col>
        <v-col v-if="products.length > 0" cols="12" xs="6" sm="6">
            <h2>Resumen</h2>
            <CartSummaryForm :subtotal-prop="totalProducts" :tax-prop="totalTax" :cod-prop="cod" @buy="buy"></CartSummaryForm>
        </v-col>
    </v-row>
</template>
<script setup lang="ts">
import CartSummaryForm from '@/components/forms/shared/CartSummaryForm.vue';
import CartProductList from '@/components/partials/CartProductList.vue';
import router from '@/router';
import { useCartStore } from '@/stores/cart';
import { useConfigsStore } from '@/stores/config';
import { useRegularAuthStore } from '@/stores/regular-auth';
import { storeToRefs } from 'pinia';

const { fetchProductsCart, buyProducts, removeProduct } = useCartStore()
const { cod } = useConfigsStore()
const { products, totalProducts, totalTax } = storeToRefs(useCartStore())

//Func principal para comprar
async function buy(params: {
    consumidorFinal: boolean,
    retiroEnTienda: boolean,
    pagoContraEntrega: boolean,
    direccion: string,
    pasarela?: string, //Para pasarla de pago
    tipo_transaccion?: string //Para tipo de transaccion en pasarela
}) {
    const attributesSale = {
        idComprador: useRegularAuthStore().user?.id!,
        consumidorFinal: params.consumidorFinal,
        retiroEnTienda: params.retiroEnTienda,
        pagoContraEntrega: params.pagoContraEntrega,
        direccion: params.direccion,
        pasarela: params.pasarela, //Pasarela
        tipo_transaccion: params.tipo_transaccion //Tipo transaccionn de pasarela

    }
    //Se envian productos de la compra a la func. en "stores/cart.ts"
    const {data, error} = await buyProducts(attributesSale);
    if (error === false) {
        router.go(0)
    }
}

async function removerProducto(product_id: number) {
    await removeProduct(product_id)
    fetchProductsCart()
}

fetchProductsCart()
</script>