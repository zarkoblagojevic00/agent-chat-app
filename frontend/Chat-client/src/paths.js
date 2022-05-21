const masterAgentId = "master";
const wsRoot = "ws://localhost:8080/Chat-war/ws";
const httpRoot = "http://localhost:8080/Chat-war/api";

export const getMasterWebsocketPath = () => `${wsRoot}/${masterAgentId}`;
export const getUserWebsocketPath = () =>
    `${wsRoot}/${sessionStorage.getItem("sessionId")}`;
export const getHttpRootPath = () => httpRoot;
