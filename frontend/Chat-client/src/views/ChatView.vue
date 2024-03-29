<template>
    <div class="container whole-page">
        <div class="chat-container shadow-item">
            <div class="sidebar-chat-menu">
                <div class="sidebar-user-info">
                    <div class="user-icon"></div>
                    <div class="user-session-info-container">
                        <span class="user-username">{{
                            sessionInfo.username
                        }}</span>
                        <span class="user-host-alias">{{
                            sessionInfo.hostAlias.split(":")[0]
                        }}</span>
                    </div>
                </div>
                <div class="sidebar-chat-menu-all-agents">
                    <div
                        v-for="(agent, idx) in agents"
                        :key="idx"
                        class="sidebar-chat-menu-agent underline-container transition-ease-in"
                        :class="{
                            'sidebar-chat-menu-active':
                                agent.username === this.newMessage.recipient,
                        }"
                        @click="switchChatAndFocus(agent.username)"
                    >
                        <div
                            class="sidebar-chat-menu-agent-status"
                            :class="
                                agent.isActive
                                    ? 'sidebar-chat-menu-agent-online'
                                    : 'sidebar-chat-menu-agent-offline'
                            "
                        ></div>
                        <div class="user-session-info-container">
                            <span class="user-username">{{
                                agent.username
                            }}</span>
                            <span
                                v-if="agent.hostAlias && agent.isActive"
                                class="user-host-alias"
                                >{{ agent.hostAlias.split(":")[0] }}</span
                            >
                        </div>
                    </div>
                </div>
            </div>
            <div class="current-chat">
                <div class="current-chat-recipient-info">
                    <div class="current-chat-recipient-name">
                        {{ this.newMessage.recipient }}
                    </div>
                </div>
                <div class="current-chat-messages-container">
                    <div
                        v-for="(message, idx) in activeChatMessages"
                        :key="idx"
                        class="message-container-wrapper"
                        :class="
                            message.sender === sessionInfo.username
                                ? 'logged-in-user-message-container-position'
                                : 'other-user-message-container-position'
                        "
                    >
                        <div
                            class="message-container"
                            :class="
                                message.sender === sessionInfo.username
                                    ? 'logged-in-user-message-container'
                                    : 'other-user-message-container'
                            "
                        >
                            <div class="message-container-subject">
                                {{ message.subject }}
                            </div>
                            <div class="message-container-content">
                                {{ message.content }}
                            </div>
                            <div class="message-container-sender-timestamp">
                                <div class="message-container-timestamp">
                                    {{
                                        new Date(
                                            message.created
                                        ).toLocaleString("sr-RS")
                                    }}
                                </div>
                                <div
                                    v-if="message.recipient === 'all'"
                                    class="message-container-sender"
                                >
                                    From: {{ message.sender }}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <form class="current-chat-enter-message-container">
                    <textarea
                        @keydown.enter.exact.prevent="sendMessage"
                        ref="messageSubject"
                        class="current-chat-subject-textarea control"
                        name="chat-message-subject"
                        placeholder="Enter subject"
                        v-model="newMessage.subject"
                    ></textarea>
                    <textarea
                        @keydown.enter.exact.prevent="sendMessage"
                        class="current-chat-content-textarea control"
                        name="chat-message-content"
                        placeholder="Type away..."
                        v-model="newMessage.content"
                    ></textarea>
                    <input
                        class="current-chat-send-message transition-ease-in"
                        type="submit"
                        value=""
                        @click.prevent="sendMessage"
                    />
                </form>
            </div>
        </div>
    </div>
</template>

<script>
import sessionStorageProxy from "@/services/session-storage-proxy.js";
import { initUserWebsocketProxy } from "@/services/socket-proxy";
import userService from "@/services/user-service";
import userWsHandler from "@/services/user-ws-handler.js";
import chatManager from "@/services/chat-manager.js";
import swalToast from "@/mixins/swal-toast.js";
import messageService from "@/services/message-service";

export default {
    name: "ChatView",
    mixins: [userWsHandler, chatManager, swalToast],
    data() {
        return {
            sessionInfo: {
                username: "",
                sessionId: "",
                hostAlias: "",
            },

            newMessage: {
                sender: "",
                recipient: "",
                subject: "",
                content: "",
            },
            registered: [],
            loggedIn: [],
        };
    },

    computed: {
        agents() {
            let agents = this.otherRegistered.concat(this.otherLoggedIn);
            const agentsObj = agents.reduce((prev, curr) => {
                prev[curr.username] = curr;
                return prev;
            }, {});
            const allChatAgent = {
                username: "all",
                hostAlias: "",
                isActive: this.otherLoggedIn.length > 0,
            };
            agents = Object.values(agentsObj);
            return [allChatAgent, ...agents];
        },
        otherLoggedIn() {
            return this.loggedIn
                .filter((a) => a.username !== this.sessionInfo.username)
                .map((a) => ({
                    username: a.username,
                    hostAlias: a.hostAlias,
                    isActive: true,
                }));
        },
        otherRegistered() {
            return this.registered
                .filter((a) => a.username !== this.sessionInfo.username)
                .map((a) => ({
                    username: a.username,
                    hostAlias: a.hostAlias,
                    isActive: false,
                }));
        },
    },
    created() {
        this.sessionInfo = sessionStorageProxy.getSessionInfo();
        initUserWebsocketProxy(
            this.sessionInfo.sessionId,
            this.requestChatOnOpen(),
            this.chatOnMessage()
        );
        this.newMessage = {
            sender: this.sessionInfo.username,
            recipient: "all",
            subject: "",
            content: "",
        };
    },

    mounted() {
        this.focusMessageSubject();
    },

    methods: {
        focusMessageSubject() {
            this.$refs.messageSubject.focus();
        },
        sendMessage() {
            try {
                this.validateMessage();
                this.trySendMessage();
            } catch (error) {
                this.handle(error);
            }
        },

        validateMessage() {
            if (
                !this.newMessage.subject.trim() ||
                !this.newMessage.content.trim()
            ) {
                throw Error("Both subject and content fields are necessary.");
            }
        },

        trySendMessage() {
            this.sendMessageToHost(this.newMessage);
            this.clearInput();
            this.focusMessageSubject();
        },

        clearInput() {
            const recipient = this.newMessage.recipient;
            this.newMessage = {
                sender: this.sessionInfo.username,
                recipient,
                subject: "",
                content: "",
            };
        },

        // sortMessagesByTimestamps() {
        //     this.activeChatMessages.sort((a, b) => a.timestamp - b.timestamp);
        // },

        switchChatAndFocus(username) {
            this.switchChat(username);
            this.focusMessageSubject();
        },

        requestChatOnOpen() {
            return async () => {
                try {
                    await Promise.all([
                        userService.getLoggedInUsers(this.sessionInfo.username),
                        userService.getRegisteredUsers(
                            this.sessionInfo.username
                        ),
                        messageService.getAllMessagesForUser(
                            this.sessionInfo.username
                        ),
                    ]);
                } catch (err) {
                    console.error(err);
                }
            };
        },

        chatOnMessage() {
            return (wsResponse) => {
                console.log(wsResponse);
                this.wsHandler.handle(wsResponse);
            };
        },

        handle(error) {
            this.toast.fire({
                icon: "warning",
                title: error.message,
            });
        },
    },

    filters: {},
};
</script>

<style scoped>
.container {
    background-image: -webkit-linear-gradient(
        -60deg,
        var(--primary-dark) 0%,
        var(--primary-comp-dark) 25%,
        var(--primary-dark) 57%,
        var(--primary-comp-dark) 90%
    );
    background-image: -moz-linear-gradient(
        -60deg,
        var(--primary-dark) 0%,
        var(--primary-comp-dark) 25%,
        var(--primary-dark) 57%,
        var(--primary-comp-dark) 90%
    );
    background-image: -ms-linear-gradient(
        -60deg,
        var(--primary-dark) 0%,
        var(--primary-comp-dark) 25%,
        var(--primary-dark) 57%,
        var(--primary-comp-dark) 90%
    );
    background-image: -o-linear-gradient(
        -60deg,
        var(--primary-dark) 0%,
        var(--primary-comp-dark) 25%,
        var(--primary-dark) 57%,
        var(--primary-comp-dark) 90%
    );
    background-image: linear-gradient(
        -60deg,
        var(--primary-dark) 0%,
        var(--primary-comp-dark) 25%,
        var(--primary-dark) 57%,
        var(--primary-comp-dark) 90%
    );
}

.chat-container {
    height: 90%;
    margin: 2% 10%;
    background: var(--background);
    border-radius: 0.7em;
    border: 3px solid var(--background-lighter);

    display: flex;
}

.sidebar-chat-menu {
    height: 100%;
    height: 100%;
    width: 325px;
    border-right: 3px solid var(--background-lighter);
    display: flex;
    flex-direction: column;
}

.sidebar-user-info {
    min-height: 60px;
    border-bottom: 3px solid var(--background-lighter);

    display: flex;
    align-items: center;
    font-size: 1.1rem;
    padding: 0 1em;
}

.user-session-info-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    color: var(--primary-comp);
}

.user-username {
    margin-right: 10px;
}

.user-host-alias {
    color: #888;
    font-size: 0.87rem;
    padding: 0.2em 0.5em;
    border-radius: 0.7em;
    border: 1px solid var(--primary-comp);
    font-style: italic;
    background: var(--background-lighter);
}

.user-icon {
    background-image: url("@/assets/user-icon.png");
    background-size: cover;
    width: 40px;
    height: 40px;
    margin-right: 0.65em;
}

.sidebar-chat-menu-all-agents {
    overflow-y: auto;
    box-sizing: border-box;
}

.sidebar-chat-menu-agent {
    min-height: 60px;
    border-bottom: 2px solid var(--background-lighter);

    display: flex;
    align-items: center;
    font-size: 0.95rem;
    padding: 0 1em;
    color: var(--control-border-color-focused);
    cursor: pointer;
}

.sidebar-chat-menu-agent:hover {
    background-color: #213349;
}

.underline-container {
    background-image: -webkit-linear-gradient(
            var(--primary-comp),
            var(--primary-comp)
        ),
        -webkit-linear-gradient(transparent, transparent);
    background-image: -moz-linear-gradient(
            var(--primary-comp),
            var(--primary-comp)
        ),
        -moz-linear-gradient(transparent, transparent);
    background-image: -ms-linear-gradient(
            var(--primary-comp),
            var(--primary-comp)
        ),
        -ms-linear-gradient(transparent, transparent);
    background-image: -o-linear-gradient(
            var(--primary-comp),
            var(--primary-comp)
        ),
        -o-linear-gradient(transparent, transparent);
    background-image: linear-gradient(var(--primary-comp), var(--primary-comp)),
        linear-gradient(transparent, transparent);
}

.sidebar-chat-menu-active {
    background-color: #213349;
    background-size: 100% 2px, auto;
    cursor: default;
}

.sidebar-chat-menu-agent-status {
    width: 9px;
    height: 9px;
    border-radius: 50%;
    border: 2px solid var(--background-lighter);
    margin-right: 8px;
}

.sidebar-chat-menu-agent-online {
    background: var(--primary-comp);
}

.sidebar-chat-menu-agent-offline {
    background: transparent;
}

.current-chat {
    height: 100%;
    width: stretch;
    display: flex;
    flex-direction: column;
}

.current-chat-recipient-info {
    min-height: 60px;
    border-bottom: 3px solid var(--background-lighter);

    display: flex;
    align-items: center;
    padding: 0 2em;
}

.current-chat-recipient-name {
    font-size: 1.8rem;
    color: var(--control-border-color-focused);
}

.current-chat-messages-container {
    min-height: 71%;
    border-bottom: 3px solid var(--background-lighter);
    padding: 1rem 2.5rem;
    overflow-y: auto;
}

.message-container-wrapper {
    display: flex;
    align-items: center;
}

.message-container {
    border: 2px solid var(--background-lighter);
    margin-bottom: 10px;
    min-height: 75px;
    min-width: 250px;
    max-width: 550px;
    padding: 0.75em 1em;
    margin-bottom: 1em;
    color: var(--control-border-color);
}

.logged-in-user-message-container-position {
    justify-content: flex-end;
}

.logged-in-user-message-container {
    border-radius: 0.8em 0.8em 0.1em 0.8em;
    align-self: flex-end;

    background-image: -webkit-linear-gradient(
        -60deg,
        var(--primary-dark) 0%,
        var(--primary-comp-dark) 80%
    );
    background-image: -moz-linear-gradient(
        -60deg,
        var(--primary-dark) 0%,
        var(--primary-comp-dark) 80%
    );
    background-image: -ms-linear-gradient(
        -60deg,
        var(--primary-dark) 0%,
        var(--primary-comp-dark) 80%
    );
    background-image: -o-linear-gradient(
        -60deg,
        var(--primary-dark) 0%,
        var(--primary-comp-dark) 80%
    );
    background-image: linear-gradient(
        -60deg,
        var(--primary-dark) 0%,
        var(--primary-comp-dark) 80%
    );
}

.other-user-message-container-position {
    justify-content: flex-start;
}

.other-user-message-container {
    border-radius: 0.1em 0.8em 0.8em 0.8em;
    align-self: flex-start;
    background: var(--background);
}

.message-container-subject {
    font-weight: 550;
    font-size: 0.92rem;
    border-bottom: 2px solid var(--control-border-color-focused);
    padding-bottom: 0.25em;
    margin-bottom: 0.5em;
}

.message-container-content {
    overflow-y: auto;
    font-size: 0.9rem;
    text-align: justify;
    max-height: 450px;
    border-bottom: 1px solid var(--control-border-color-focused);
    padding: 0 0.75em 0.8em 0.2em;
    margin-bottom: 0.5em;
}

.message-container-sender-timestamp {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    font-size: 0.8rem;
}

.current-chat-enter-message-container {
    height: stretch;
    display: flex;
    justify-content: space-around;
    align-items: center;
    padding: 0 1em;
    font-family: Avenir, Helvetica, Arial, sans-serif;
}

.current-chat-subject-textarea {
    min-width: 23%;
    max-width: 23%;
    min-height: 45px;
    max-height: 45px;
    resize: none;
}

.current-chat-content-textarea {
    min-width: 70%;
    max-width: 70%;
    min-height: 45px;
    max-height: 45px;
    resize: none;
}

.control {
    background: transparent;
    font-size: 0.95rem;
    padding: 0.8em;

    color: var(--control-border-color);
}

.control:focus {
    border-color: var(--primary-comp);
}

::-webkit-scrollbar {
    width: 10px;
}

/* Track */
::-webkit-scrollbar-track {
    background: var(--background-lighter);
    border-radius: 0.7em;
}

/* Handle */
::-webkit-scrollbar-thumb {
    background: #888;
    border-radius: 0.7em;
}

/* Handle on hover */
::-webkit-scrollbar-thumb:hover {
    background: #555;
}

.current-chat-send-message {
    background-color: transparent;
    background-image: url("@/assets/paper-plane.png");
    background-size: cover;
    color: transparent;
    width: 43px;
    height: 43px;
    border-radius: 50%;
    border: 2px solid var(--background-lighter);
    cursor: pointer;
    outline: none;
}

.current-chat-send-message:hover,
.current-chat-send-message:focus {
    border: 2px solid var(--primary-comp);
    background-color: #213349;
}

/* For 1024 Resolution */
@media screen and (min-device-width: 1200px) and (max-device-width: 1600px) and (-webkit-min-device-pixel-ratio: 1) {
    .chat-container {
        height: 100%;
        margin: 0;
        border-radius: 0;
        border: 0;
    }

    .current-chat-messages-container {
        min-height: 68%;
    }
}
</style>
