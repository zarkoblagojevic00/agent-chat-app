const USERNAME = "username";
const SESSION_ID = "sessionId";

export default {
    storeSessionInfo(sessionInfo) {
        sessionStorage.setItem(USERNAME, sessionInfo.username);
        sessionStorage.setItem(SESSION_ID, sessionInfo.sessionId);
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
    clearStorage: () => sessionStorage.clear(),
    isUserLoggedIn() {
        return (
            this.getUsername() != undefined && this.getSessionId() != undefined
        );
    },
};
