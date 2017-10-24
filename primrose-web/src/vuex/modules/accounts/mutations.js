import Vue from "vue";

export default {
  addResults: (state, results) => {
    Vue.set(state.search, "results", results);
  },
  setCount: (state, count) => {
    Vue.set(state.search, "count", count);
  },
  setLoading: (state, loading) => {
    Vue.set(state.search, "loading", loading);
  },
};
