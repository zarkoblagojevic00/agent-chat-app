import fetchProxy from "./fetch-proxy.js";

const httpProxy = fetchProxy("users");

export default {
    register: async (newUser) =>
        httpProxy.executeRequest("register", "POST", newUser),
};
