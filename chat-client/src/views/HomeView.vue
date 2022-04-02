<template>
    <div class="container whole-page">
        <!-- <img alt="Agent logo" src="../assets/agent-chat-logo-256.svg" /> -->
        <vue-login></vue-login>
        <vue-login-promo></vue-login-promo>
    </div>
</template>

<script>
import VueLogin from "@/components/VueLogin.vue";
import VueLoginPromo from "@/components/VueLoginPromo.vue";

export default {
    name: "HomeView",
    components: {
        VueLogin,
        VueLoginPromo,
    },
};
</script>

<style scoped>
.container {
    background-color: #013a6b;
    background-image: -webkit-linear-gradient(
        -60deg,
        var(--background) 50%,
        var(--primary-dark) 0%,
        var(--darker-purple) 85%
    );
    background-image: -moz-linear-gradient(
        -60deg,
        var(--background) 50%,
        var(--primary-dark) 0%,
        var(--darker-purple) 85%
    );
    background-image: -ms-linear-gradient(
        -60deg,
        var(--background) 50%,
        var(--primary-dark) 0%,
        var(--darker-purple) 85%
    );
    background-image: -o-linear-gradient(
        -60deg,
        var(--background) 50%,
        var(--primary-dark) 0%,
        var(--darker-purple) 85%
    );
    background-image: linear-gradient(
        -60deg,
        var(--background) 50%,
        var(--primary-dark) 0%,
        var(--primary-comp-dark) 85%
    );
}
</style>
