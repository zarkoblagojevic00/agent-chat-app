<template>
    <div class="container">
        <div class="logo-container">
            <img alt="Agent logo" src="../assets/agent-chat-logo-64.svg" />
            <div class="app-name gradient-text">Agent Chat</div>
        </div>
        <nav>
            <router-link to="/">Login</router-link>
            <router-link
                class="register-link gradient-bg ease-in"
                to="/register"
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
    margin: 0 10px;
    font-size: 1.05rem;
    color: var(--white);
}

nav a.router-link-exact-active {
    color: var(--blue);
}

.register-link {
    padding: 0.5em 0.8em;
    border-radius: 0.2em 0.7em;
    color: var(--white);
}

.register-link:hover {
    background-image: linear-gradient(
        45deg,
        var(--darker-blue),
        var(--darker-purple)
    );
    background-size: 100%;
}

a.register-link.router-link-exact-active {
    color: var(--white);
    background-image: linear-gradient(
        45deg,
        var(--darker-blue),
        var(--darker-purple)
    );
}
</style>
