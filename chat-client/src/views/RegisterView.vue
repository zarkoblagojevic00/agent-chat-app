<template>
    <div class="container whole-page">
        <!-- <img alt="Agent logo" src="../assets/agent-chat-logo-256.svg" /> -->
        <vue-register class="register-position"></vue-register>
        <vue-register-promo
            class="register-promo-position"
        ></vue-register-promo>
    </div>
</template>

<script>
import VueRegister from "@/components/VueRegister.vue";
import VueRegisterPromo from "@/components/VueRegisterPromo.vue";

export default {
    name: "HomeView",
    components: {
        VueRegister,
        VueRegisterPromo,
    },
};
</script>

<style scoped>
.container {
    background-color: #013a6b;
    background-image: -webkit-linear-gradient(
        120deg,
        var(--background) 50%,
        var(--primary-dark) 0%,
        var(--darker-purple) 85%
    );
    background-image: -moz-linear-gradient(
        120deg,
        var(--background) 50%,
        var(--primary-dark) 0%,
        var(--darker-purple) 85%
    );
    background-image: -ms-linear-gradient(
        120deg,
        var(--background) 50%,
        var(--primary-dark) 0%,
        var(--darker-purple) 85%
    );
    background-image: -o-linear-gradient(
        120deg,
        var(--background) 50%,
        var(--primary-dark) 0%,
        var(--darker-purple) 85%
    );
    background-image: linear-gradient(
        120deg,
        var(--background) 50%,
        var(--primary-dark) 0%,
        var(--primary-comp-dark) 85%
    );
}

.register-position {
    position: absolute;
    top: 8%;
    right: 10%;
}

.register-promo-position {
    position: absolute;
    top: 10%;
    left: 10%;
}
</style>
