import { getHttpRootPath } from "@/paths.js";

export default (resource) => ({
    executeRequest: async (path, method, data) =>
        fetch(`${getHttpRootPath()}/${resource}/${path}`, {
            method,
            headers: {
                "Content-Type": "application/json",
            },
            body: data ? JSON.stringify(data) : "",
        }),
});
