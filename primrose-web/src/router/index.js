import Vue from "vue";
import Router from "vue-router";
import TheDashboard from "@/components/TheDashboard";
import TheAccounts from "@/components/TheAccounts";
import TheAccount from "@/components/TheAccount";

Vue.use(Router);

const router = new Router({
  routes: [{
    path: "/",
    name: "Dashboard",
    component: TheDashboard,
  }, {
    path: "/accounts",
    name: "Accounts",
    component: TheAccounts,
    props: to => ({
      query: to.query,
    }),
    beforeEnter: (to, from, next) => {
      if (!to.query.page || !to.query.size) {
        next({
          name: "Accounts",
          query: {
            page: 1,
            size: 10,
            sort: "displayName",
          },
        });
      } else {
        next();
      }
    },
  }, {
    path: "/accounts/:accountId",
    name: "Account",
    component: TheAccount,
    props: to => ({
      accountId: to.params.accountId,
    }),
  }],
  mode: "history",
});

export default router;
