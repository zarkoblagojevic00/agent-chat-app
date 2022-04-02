<template>
    <nav-bar></nav-bar>
    <router-view />
</template>

<script>
import NavBar from "@/components/NavBar.vue";

export default {
    components: {
        NavBar,
    },
};
</script>

<style src="@/styles/root.css"></style>
<style>
body {
    margin: 0;
    padding: 0;
}

#app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    color: #2c3e50;
}

nav a {
    font-weight: bold;
    color: #2c3e50;
}
</style>
