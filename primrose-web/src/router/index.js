import Vue from "vue";
import Router from "vue-router";
import HelloWorld from "@/components/HelloWorld";
import Accounts from "@/components/Accounts";

Vue.use(Router);

export default new Router({
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
  ],
  mode: "history",
});
