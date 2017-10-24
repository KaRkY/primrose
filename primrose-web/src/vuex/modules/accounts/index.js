import actions from "./actions";
import mutations from "./mutations";
import getters from "./getters";

const state = {
  search: {
    results: [],
    count: 0,
    loading: true,
  },
};

export default {
  namespaced: true,
  state,
  actions,
  mutations,
  getters,
};
