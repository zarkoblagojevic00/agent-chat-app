import fetchProxy from "./fetch-proxy.js";

const httpProxy = fetchProxy("users");

export default {
    register: async (newUser) =>
        httpProxy.executeRequest("register", "POST", newUser),
    login: async (user) => httpProxy.executeRequest("login", "POST", user),
    logout: async (username) =>
        httpProxy.executeRequest(`loggedIn/${username}`, "DELETE"),
};
