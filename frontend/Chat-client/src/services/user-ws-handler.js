import agentMessageType from "@/agent-message-type";

export default {
    data() {
        return {
            wsHandler: {
                handle(wsResponse) {
                    const response = wsResponse.response;
                    if (!wsResponse.success) {
                        console.error(
                            `Server responded with failure on: ${response}`
                        );
                    }
                    console.log(`${response} fired`);
                    return this.handlers[response](wsResponse.payload);
                },

                handlers: {
                    [agentMessageType.getLoggedIn]: this.handleGetLoggedIn,
                    [agentMessageType.getRegistered]: this.handleGetRegistered,
                    [agentMessageType.otherUserLogin]:
                        this.handleOtherUserLogin,
                    [agentMessageType.otherUserLogout]:
                        this.handleOtherUserLogout,
                    [agentMessageType.otherUserRegister]:
                        this.handleOtherUserRegister,
                    [agentMessageType.receiveMessage]:
                        this.handleReceiveMessage,
                },
            },
        };
    },
    methods: {
        handleGetLoggedIn(payload) {
            this.loggedIn = payload;
        },
        handleGetRegistered(payload) {
            this.registered = payload;
        },
        handleOtherUserLogin(payload) {
            this.loggedIn.push(payload);
        },
        handleOtherUserLogout(payload) {
            this.loggedIn = this.loggedIn.filter(
                (u) => u.username !== payload.username
            );
        },
        handleOtherUserRegister(payload) {
            this.registered.push(payload);
        },
        handleReceiveMessage(payload) {
            this.receiveMessages(payload);
        },
    },
};
