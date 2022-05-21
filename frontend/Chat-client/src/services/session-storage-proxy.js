const USERNAME = "username";
const SESSION_ID = "sessionId";
const HOST_ALIAS = "hostAlias";
const SESSION_INFO = "sessionInfo";

export default {
    storeSessionInfo(sessionInfo) {
        sessionStorage.setItem(USERNAME, sessionInfo.username);
        sessionStorage.setItem(SESSION_ID, sessionInfo.sessionId);
        sessionStorage.setItem(HOST_ALIAS, sessionInfo.hostAlias);
        sessionStorage.setItem(SESSION_INFO, JSON.stringify(sessionInfo));
        dispatchEvent(
            new CustomEvent("user-logged-in", {
                detail: {
                    sessionInfo,
                },
            })
        );
    },

    getUsername: () => sessionStorage.getItem(USERNAME),
    getSessionId: () => sessionStorage.getItem(SESSION_ID),
    getHostAlias: () => sessionStorage.getItem(HOST_ALIAS),
    getSessionInfo: () => JSON.parse(sessionStorage.getItem(SESSION_INFO)),

    clearStorage: () => sessionStorage.clear(),

    isUserLoggedIn() {
        return this.getSessionId() != undefined;
    },
};
