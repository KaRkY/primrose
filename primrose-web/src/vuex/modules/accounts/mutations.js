import Vue from "vue";

export default {
  addResults: (state, results) => {
    Vue.set(state, "results", results);
  },
  count: (state, count) => {
    Vue.set(state, "count", count);
  },
  loading: (state, loading) => {
    Vue.set(state, "loading", loading);
  },
  pagination: (state, pagination) => {
    Vue.set(state, "pagination", pagination);
  },
};
