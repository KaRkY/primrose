import Vue from "vue";

export default {
  account: (state, account) => {
    Vue.set(state, "account", account);
  },
  loading: (state, loading) => {
    Vue.set(state, "loading", loading);
  },
};
