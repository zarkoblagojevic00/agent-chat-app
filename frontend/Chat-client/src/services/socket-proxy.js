import { getMasterWebsocketPath, getUserWebsocketPath } from "@/paths";

export const initMasterWebsocketProxy = (onOpen, onMessage) => {
    let socket = new WebSocket(getMasterWebsocketPath());
    socket.onopen = () => {
        console.log("Master Socket is open");
        onOpen();
    };
    socket.onclose = () => {
        socket = null;
        console.log("Master Socket is closed");
    };
    socket.onmessage = getSelfClosingOnMessage(onMessage, socket);
};

function getSelfClosingOnMessage(onMessage, socket) {
    return function (message) {
        socket.close();
        const wsResponse = JSON.parse(message.data);
        onMessage(wsResponse);
    };
}

export const initUserWebsocketProxy = (onMessage, username) => {
    let socket = new WebSocket(getUserWebsocketPath(username));
    socket.onopen = () => {
        console.log(`Socket is open for user: ${username}`);
    };
    socket.onclose = () => {
        socket = null;
        console.log(`Socket is closed for user: ${username}`);
    };
    socket.onmessage = onMessage;
};
