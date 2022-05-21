import { createRouter, createWebHistory } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import RegisterView from "@/views/RegisterView.vue";
import ChatView from "@/views/ChatView.vue";

import sessionStorageProxy from "@/services/session-storage-proxy.js";
const routes = [
    {
        path: "/register",
        name: "register",
        component: RegisterView,
        beforeEnter: () => {
            if (sessionStorageProxy.isUserLoggedIn()) {
                return { name: "chat" };
            }
        },
    },
    {
        path: "/chat",
        name: "chat",
        component: ChatView,
        beforeEnter: () => {
            if (!sessionStorageProxy.isUserLoggedIn()) {
                return { name: "login" };
            }
        },
    },
    {
        path: "/:pathMatch(.*)*",
        name: "home",
        component: HomeView,
        beforeEnter: () => {
            if (sessionStorageProxy.isUserLoggedIn()) {
                return { name: "chat" };
            }
        },
    },
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
});

export default router;
