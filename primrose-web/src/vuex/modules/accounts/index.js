import actions from "./actions";
import mutations from "./mutations";
import getters from "./getters";

const state = {
  results: [],
  count: 0,
  loading: true,
  pagination: {},
};

export default {
  namespaced: true,
  state,
  actions,
  mutations,
  getters,
};
