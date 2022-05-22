<template>
    <div class="container">
        <div class="logo-container">
            <img alt="Agent logo" src="../assets/agent-chat-logo-64.svg" />
            <div class="app-name gradient-text">Agent Chat</div>
        </div>
        <nav>
            <div v-if="!userLoggedIn">
                <router-link to="/">Login</router-link>
                <router-link class="register-link gradient-bg" to="/register"
                    >Register</router-link
                >
            </div>
            <div v-else>
                <button class="logout-button clickable danger" @click="logout">
                    Logout
                </button>
            </div>
        </nav>
    </div>
</template>

<script>
import swalToast from "@/mixins/swal-toast.js";
import { initMasterWebsocketProxy } from "@/services/socket-proxy.js";
import agentMessageType from "@/agent-message-type";
import userService from "@/services/user-service";
import sessionStorageProxy from "@/services/session-storage-proxy.js";

export default {
    name: "NavBar",
    mixins: [swalToast],
    data() {
        return {
            sessionInfo: sessionStorageProxy.getSessionInfo(),
        };
    },
    mounted() {
        addEventListener(
            "user-logged-in",
            (event) => (this.sessionInfo = event.detail.sessionInfo)
        );
    },
    computed: {
        userLoggedIn() {
            return this.sessionInfo;
        },
    },
    methods: {
        logout() {
            try {
                this.tryLogout();
            } catch (error) {
                this.handle(error);
            }
        },
        tryLogout() {
            initMasterWebsocketProxy(
                this.requestLogoutOnOpen(),
                this.logoutOnMessage()
            );
        },
        requestLogoutOnOpen() {
            return async () => {
                try {
                    await userService.logout(this.sessionInfo.username);
                } catch (error) {
                    this.handle(
                        new Error(
                            "We're sorry, we've had an issue. Please try again later."
                        )
                    );
                }
            };
        },
        logoutOnMessage() {
            return (wsResponse) => {
                if (wsResponse.response !== agentMessageType.logout) return;
                if (!wsResponse.success) {
                    this.handle(
                        Error("User with given username doesn't exists.")
                    );
                    return;
                }
                this.sessionInfo = null;
                sessionStorageProxy.clearStorage();

                this.toast.fire({
                    icon: "success",
                    title: `Your agent is down. Goodbye!`,
                });
                this.$router.push({ name: "home" });
            };
        },
        handle(error) {
            this.toast.fire({
                icon: "warning",
                title: error.message,
            });
        },
    },
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

.logout-button {
    font-size: 0.9em;
}

@media screen and (min-device-width: 1200px) and (max-device-width: 1600px) and (-webkit-min-device-pixel-ratio: 1) {
    .container {
        padding: 5px 2%;
    }
}
</style>
