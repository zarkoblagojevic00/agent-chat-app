<template>
    <div class="container whole-page">
        <div class="chat-container shadow-item">
            <div class="sidebar-chat-menu">
                <div class="sidebar-user-info">
                    <div class="user-icon"></div>
                    <div>{{ loggedInUser }}</div>
                </div>
                <div class="sidebar-chat-menu-all-agents">
                    <div
                        v-for="(agent, idx) in agents"
                        :key="agent"
                        class="sidebar-chat-menu-agent underline-container transition-ease-in"
                        :class="{
                            'sidebar-chat-menu-active': idx === activeChatIdx,
                        }"
                        @click="switchChat(idx)"
                    >
                        <div
                            class="sidebar-chat-menu-agent-status"
                            :class="
                                agents[idx].isActive
                                    ? 'sidebar-chat-menu-agent-online'
                                    : 'sidebar-chat-menu-agent-offline'
                            "
                        ></div>
                        {{ agent.name }}
                    </div>
                </div>
            </div>
            <div class="current-chat">
                <div class="current-chat-receiver-info">
                    <div class="current-chat-receiver-name">
                        {{ agents[activeChatIdx].name }}
                    </div>
                </div>
                <div class="current-chat-messages-container">
                    <div
                        v-for="(message, idx) in activeChatMessages"
                        :key="idx"
                        class="message-container-wrapper"
                        :class="
                            message.sender === loggedInUser
                                ? 'logged-in-user-message-container-position'
                                : 'other-user-message-container-position'
                        "
                    >
                        <div
                            class="message-container"
                            :class="
                                message.sender === loggedInUser
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
                            <div class="message-container-timestamp">
                                {{ message.timestamp.toLocaleString("sr-RS") }}
                            </div>
                        </div>
                    </div>
                </div>
                <form class="current-chat-enter-message-container">
                    <textarea
                        @keydown.enter.exact.prevent="sendMessage"
                        ref="messageInput"
                        class="current-chat-textarea control"
                        name="chat-message"
                        placeholder="Type a message"
                        v-model="message.content"
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
export default {
    name: "ChatView",
    data() {
        return {
            loggedInUser: "John Doe",
            message: {
                sender: "",
                receiver: "",
                timestamp: null,
                subject: "",
                content: "",
            },
            agents: [],
            activeChatIdx: -1,
            activeChatMessages: [
                {
                    sender: "John Doe",
                    receiver: "Mark Markman",
                    timestamp: new Date("2022-04-01T12:24:00"),
                    subject: "Longer subject test for me",
                    content:
                        "Lorem ipsum dolor sit amet consectetur adipisicing elit. Vitae quidem debitis, beatae reprehenderit exercitationem nostrum deleniti explicabo cupiditate cum obcaecati. Facilis aperiam ratione voluptatum ",
                },
                {
                    sender: "Mark Markman",
                    receiver: "John Doe",
                    timestamp: new Date("2022-04-01T12:25:00"),
                    subject: "Longer subject test for me",
                    content:
                        "Lorem ipsum dolor sit amet consectetur adipisicing elit. Vitae quidem debitis, beatae reprehenderit exercitationem nostrum deleniti explicabo cupiditate cum obcaecati. Facilis aperiam ratione voluptatum ",
                },
                {
                    sender: "Mark Markman",
                    receiver: "John Doe",
                    timestamp: new Date("2022-04-01T12:26:00"),
                    subject: "Longer subject test for me",
                    content:
                        "Lorem ipsum dolor sit amet consectetur adipisicing elit. Vitae quidem debitis, beatae reprehenderit exercitationem nostrum deleniti explicabo cupiditate cum obcaecati. Facilis aperiam ratione voluptatum ",
                },
                {
                    sender: "Mark Markman",
                    receiver: "John Doe",
                    timestamp: new Date("2022-04-01T12:35:00"),
                    subject: "Longer subject test for me",
                    content:
                        "Lorem ipsum dolor sit amet consectetur adipisicing elit. Vitae quidem debitis, beatae reprehenderit exercitationem nostrum deleniti explicabo cupiditate cum obcaecati. Facilis aperiam ratione voluptatum ",
                },
                {
                    sender: "John Doe",
                    receiver: "Mark Markman",
                    timestamp: new Date("2022-04-01T12:30:00"),
                    subject: "Longer subject test for me",
                    content:
                        "Lorem ipsum dolor sit amet consectetur adipisicing elit. Vitae quidem debitis, beatae reprehenderit exercitationem nostrum deleniti explicabo cupiditate cum obcaecati. Facilis aperiam ratione voluptatum ",
                },

                {
                    sender: "John Doe",
                    receiver: "Mark Markman",
                    timestamp: new Date("2022-04-01T12:36:00"),
                    subject: "Longer subject test for me",
                    content:
                        "Lorem ipsum dolor sit amet consectetur adipisicing elit. Vitae quidem debitis, beatae reprehenderit exercitationem nostrum deleniti explicabo cupiditate cum obcaecati. Facilis aperiam ratione voluptatum ",
                },
                {
                    sender: "John Doe",
                    receiver: "Mark Markman",
                    timestamp: new Date("2022-04-01T12:40:00"),
                    subject: "Longer subject test for me",
                    content:
                        "Lorem ipsum dolor sit amet consectetur adipisicing elit. Vitae quidem debitis, beatae reprehenderit exercitationem nostrum deleniti explicabo cupiditate cum obcaecati. Facilis aperiam ratione voluptatum Lorem ipsum dolor sit amet consectetur adipisicing elit. Vitae quidem debitis, beatae reprehenderit exercitationem nostrum deleniti explicabo cupiditate cum obcaecati. Facilis aperiam ratione voluptatum Lorem ipsum dolor sit amet consectetur adipisicing elit. Vitae quidem debitis, beatae reprehenderit exercitationem nostrum deleniti explicabo cupiditate cum obcaecati. Facilis aperiam ratione voluptatum Lorem ipsum dolor sit amet consectetur adipisicing elit. Vitae quidem debitis, beatae reprehenderit exercitationem nostrum deleniti explicabo cupiditate cum obcaecati. Facilis aperiam ratione voluptatum Lorem ipsum dolor sit amet consectetur adipisicing elit. Vitae quidem debitis, beatae reprehenderit exercitationem nostrum deleniti explicabo cupiditate cum obcaecati. Facilis aperiam ratione voluptatum Lorem ipsum dolor sit amet consectetur adipisicing elit. Vitae quidem debitis, beatae reprehenderit exercitationem nostrum deleniti explicabo cupiditate cum obcaecati. Facilis aperiam ratione voluptatum Lorem ipsum dolor sit amet consectetur adipisicing elit. Vitae quidem debitis, beatae reprehenderit exercitationem nostrum deleniti explicabo cupiditate cum obcaecati. Facilis aperiam ratione voluptatum Lorem ipsum dolor sit amet consectetur adipisicing elit. Vitae quidem debitis, beatae reprehenderit exercitationem nostrum deleniti explicabo cupiditate cum obcaecati. Facilis aperiam ratione voluptatum Lorem ipsum dolor sit amet consectetur adipisicing elit. Vitae quidem debitis, beatae reprehenderit exercitationem nostrum deleniti explicabo cupiditate cum obcaecati. Facilis aperiam ratione voluptatum Lorem ipsum dolor sit amet consectetur adipisicing elit. Vitae quidem debitis, beatae reprehenderit exercitationem nostrum deleniti explicabo cupiditate cum obcaecati. Facilis aperiam ratione voluptatum Lorem ipsum dolor sit amet consectetur adipisicing elit. Vitae quidem debitis, beatae reprehenderit exercitationem nostrum deleniti explicabo cupiditate cum obcaecati. Facilis aperiam ratione voluptatum Lorem ipsum dolor sit amet consectetur adipisicing elit. Vitae quidem debitis, beatae reprehenderit exercitationem nostrum deleniti explicabo cupiditate cum obcaecati. Facilis aperiam ratione voluptatum ",
                },
            ],
        };
    },
    created() {
        this.activeChatIdx = 0;
        this.agents = [
            { name: "Mark Markman", isActive: true },
            { name: "Steve Steveman", isActive: false },
            { name: "Rene Renven", isActive: false },
            { name: "Rita Goodall", isActive: true },
        ];
        this.message = {
            sender: this.loggedInUser,
            receiver: this.agents[this.activeChatIdx].name,
            timestamp: null,
            subject: "",
            content: "",
        };
        this.sortMessagesByTimestamps();
    },

    mounted() {
        this.focusMessageInput();
    },
    methods: {
        focusMessageInput() {
            this.$refs.messageInput.focus();
        },
        sendMessage() {
            // check valid message
            if (!this.message.content.trim()) return;

            this.message.timestamp = new Date();

            // send message
            console.log(this.message);

            //clear input
            this.message = {
                sender: this.loggedInUser,
                receiver: this.agents[this.activeChatIdx].name,
                timestamp: null,
                subject: "",
                content: "",
            };
        },
        switchChat(idx) {
            this.activeChatIdx = idx;
            this.message.receiver = this.agents[this.activeChatIdx].name;
            this.focusMessageInput();
        },
        sortMessagesByTimestamps() {
            this.activeChatMessages.sort((a, b) => a.timestamp - b.timestamp);
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
    min-height: 75px;
    border-bottom: 3px solid var(--background-lighter);

    display: flex;
    align-items: center;
    font-size: 1.2rem;
    padding: 0 1em;
    color: var(--primary-comp);
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
    font-size: 1.025rem;
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
    background: #a97f32;
}

.current-chat {
    height: 100%;
    width: stretch;
    display: flex;
    flex-direction: column;
}

.current-chat-receiver-info {
    min-height: 75px;
    border-bottom: 3px solid var(--background-lighter);

    display: flex;
    align-items: center;
    padding: 0 2em;
}

.current-chat-receiver-name {
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
    min-height: 80px;
    max-width: 350px;
    padding: 1em;
    color: var(--control-border-color);
}

.logged-in-user-message-container-position {
    justify-content: flex-end;
}

.logged-in-user-message-container {
    border-radius: 1.5em 1.5em 0.1em 1.5em;
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
    border-radius: 0.1em 1.5em 1.5em 1.5em;
    align-self: flex-start;
    background: var(--background);
}

.message-container-subject {
    font-weight: 550;
    border-bottom: 2px solid var(--control-border-color-focused);
    padding-bottom: 0.25em;
    margin-bottom: 0.5em;
}

.message-container-content {
    overflow-y: auto;
    max-height: 600px;
    border-bottom: 1px solid var(--control-border-color-focused);
    padding-bottom: 0.9em;
    margin-bottom: 0.5em;
}

.message-container-timestamp {
    font-size: 0.9rem;
    display: flex;
    justify-content: flex-end;
}

.current-chat-enter-message-container {
    height: stretch;
    display: flex;
    justify-content: space-around;
    align-items: center;
    padding: 0 1em;
}

.current-chat-textarea {
    min-width: 92%;
    max-width: 92%;
    min-height: 45px;
    max-height: 45px;
    resize: none;

    font-family: Avenir, Helvetica, Arial, sans-serif;
}

.control {
    background: transparent;
    font-size: 1.2rem;
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
</style>
