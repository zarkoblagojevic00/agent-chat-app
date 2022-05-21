<template>
    <div class="container">
        <div class="logo-container">
            <img alt="Agent logo" src="../assets/agent-chat-logo-64.svg" />
            <div class="app-name gradient-text">Agent Chat</div>
        </div>
        <nav>
            <router-link to="/">Login</router-link>
            <router-link to="/chat">Chat</router-link>
            <router-link class="register-link gradient-bg" to="/register"
                >Register</router-link
            >
        </nav>
    </div>
</template>

<script>
export default {
    name: "NavBar",
};
</script>

<style scoped>
.container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 5px 10%;
    background: var(--background);
    border-bottom: 2px var(--primary-dark) solid;
}

.logo-container {
    display: flex;
    justify-content: flex-start;
    align-items: center;
}

.app-name {
    font-size: 1.8rem;
}

nav a {
    text-decoration: none;
    margin-left: 25px;
    font-size: 1.05rem;
    color: var(--control-border-color);
}

nav a.router-link-exact-active {
    color: #fc96e7;
    cursor: default;
}

.register-link {
    padding: 0.5em 0.8em;
    border-radius: 0.2em 0.7em;
}

.register-link:hover {
    background-image: linear-gradient(
        135deg,
        var(--primary-dark),
        var(--primary-comp-dark)
    );
    background-size: 100%;
}

a.register-link.router-link-exact-active {
    background-image: linear-gradient(
        135deg,
        var(--primary-dark),
        var(--primary-comp-dark)
    );
}

@media screen and (min-device-width: 1200px) and (max-device-width: 1600px) and (-webkit-min-device-pixel-ratio: 1) {
    .container {
        padding: 5px 2%;
    }
}
</style>
