<template>
    <div class="login-container">
        <div class="login-title-container">Login</div>
        <form class="form-wrapper">
            <div class="control-wrapper">
                <span class="input-label">Username* </span>
                <input
                    ref="username"
                    class="control transition-ease"
                    v-model="credentials.username"
                    type="text"
                    placeholder="Enter your username"
                />
            </div>
            <div class="control-wrapper">
                <span class="input-label">Password* </span>
                <input
                    class="control transition-ease"
                    v-model="credentials.password"
                    type="password"
                    placeholder="Enter your password"
                />
            </div>
            <div class="submit-container">
                <input
                    class="submit-button clickable primary-comp transition-ease-in"
                    type="submit"
                    @click.prevent="login"
                />
            </div>
            <div class="not-registered">
                Not yet registered? Register
                <router-link class="not-registered-link" to="/register">
                    here.</router-link
                >
            </div>
        </form>
    </div>
</template>

<script>
import SwalToast from "@/mixins/swal-toast.js";
import { initMasterWebsocketProxy } from "@/services/socket-proxy";
import userService from "@/services/user-service";
import agentMessageType from "@/agent-message-type";
import sessionStorageProxy from "@/services/session-storage-proxy.js";

export default {
    name: "VueLogin",
    mixins: [SwalToast],
    data() {
        return {
            credentials: {
                username: "",
                password: "",
            },
        };
    },
    mounted() {
        this.$refs.username.focus();
    },
    methods: {
        login() {
            try {
                this.validateInput();
                this.tryLogin();
            } catch (error) {
                this.handle(error);
            }
        },
        validateInput() {
            this.validateFieldsEmpty();
        },
        validateFieldsEmpty() {
            if (this.credentials.username && this.credentials.password) return;
            throw Error("You must provide all the required fields.");
        },
        tryLogin() {
            initMasterWebsocketProxy(
                this.requestLoginOnOpen(),
                this.loginOnMessage()
            );
        },
        handle(error) {
            this.toast.fire({
                icon: "warning",
                title: error.message,
            });
        },
        requestLoginOnOpen() {
            return async () => {
                try {
                    await userService.login(this.credentials);
                } catch (error) {
                    this.handle(
                        new Error(
                            "We're sorry, we've had an issue. Please try again later."
                        )
                    );
                }
            };
        },
        loginOnMessage() {
            return (wsResponse) => {
                if (wsResponse.response !== agentMessageType.login) return;
                if (!wsResponse.success) {
                    this.handle(
                        Error("User with given credentials doesn't exists.")
                    );
                    return;
                }
                sessionStorageProxy.storeSessionInfo(wsResponse.payload);
                this.toast.fire({
                    icon: "success",
                    title: `Welcome 🎉🎉🎉. Your agent is warming up.`,
                });
                this.$router.push({ name: "chat" });
            };
        },
    },
};
</script>

<style scoped>
.login-container {
    width: 400px;
    border-radius: 0.7em;
    background: var(--background);
    color: var(--primary-comp);
}

.login-title-container {
    display: flex;
    justify-content: center;
    align-items: center;

    font-size: 1.8rem;
    font-weight: bold;
    margin-top: 0.8em;
    color: var(--primary-comp);
}

.form-wrapper {
    margin: 2em 1.5em;
}

.input-label {
    margin-top: 1em;
    font-size: 1.12rem;
}

.control {
    background: transparent;
    font-size: 1.2rem;
    color: var(--control-border-color);
}

.control:focus {
    border-color: var(--primary-comp);
}

.submit-container {
    margin-top: 3.5em;
    display: flex;
    justify-content: center;
    align-items: center;
}

.submit-button {
    width: 50%;
    font-size: 1.2rem;
    padding: 0.5em;
}

.not-registered {
    margin-top: 2em;
    display: flex;
    justify-content: center;
    align-items: center;
    color: var(--control-border-color);
}

.not-registered-link {
    color: var(--primary-comp);
    margin-left: 0.3em;
}
</style>
