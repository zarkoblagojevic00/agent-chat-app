import messageService from "./message-service.js";

export default {
    data() {
        // private fields and functions
        const self = this;
        console.log(self);
        return {
            chats: {},

            async sendMessageToHost(newMessage) {
                try {
                    if (newMessage.recipient === "all") {
                        await messageService.sendMessageToAll(newMessage);
                    } else {
                        await messageService.sendMessageToUser(newMessage);
                    }
                } catch (error) {
                    console.error("FAILED on sending a message.");
                    throw Error(
                        "We're sorry, we've had an issue. Please try again later."
                    );
                }
            },

            receiveMessages(messages) {
                const addMessageToChat = (username, message) => {
                    if (this.chats[username] === undefined) {
                        this.chats[username] = [message];
                    } else {
                        this.chats[username].push(message);
                    }
                };
                messages.forEach((message) => {
                    if (message.sender === self.sessionInfo.username) {
                        addMessageToChat(message.recipient, message);
                    } else {
                        if (message.recipient !== "all") {
                            addMessageToChat(message.sender, message);
                        } else {
                            addMessageToChat("all", message);
                        }
                    }
                });
            },

            switchChat(username) {
                self.newMessage.recipient = username;
            },
        };
    },
    computed: {
        activeChatMessages() {
            return this.chats[this.newMessage.recipient];
        },
    },
};
