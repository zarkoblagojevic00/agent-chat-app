import fetchProxy from "./fetch-proxy.js";

const httpProxy = fetchProxy("messages");

export default {
    sendMessageToAll: async (newMessage) =>
        httpProxy.executeRequest("all", "POST", newMessage),
    sendMessageToUser: async (newMessage) =>
        httpProxy.executeRequest("user", "POST", newMessage),
    getAllMessagesForUser: async (username) =>
        httpProxy.executeRequest(username, "GET"),
};
