import Vue from "vue";
import Router from "vue-router";
import HelloWorld from "@/components/HelloWorld";
import Accounts from "@/components/Accounts";
import Login from "@/components/Login";
import store from "@/vuex";

Vue.use(Router);

const router = new Router({
  routes: [
    {
      path: "/",
      name: "Hello",
      component: HelloWorld,
    },
    {
      path: "/accounts",
      name: "Accounts",
      component: Accounts,
    },
    {
      path: "/authorize",
      name: "Login",
      component: Login,
    },
  ],
  mode: "history",
});

router.beforeEach((to, from, next) => {
  if (to.name !== "Login" && !store.getters["authorization/isAuthenticated"]) {
    store
      .dispatch("authorization/authorize", { username: "root", password: "root" })
      .then(() => {
        next();
      })
      .catch(() => {
        next({ name: "Login" });
      });
  } else {
    next();
  }
});

export default router;
