export default {
  getResults: (state, getters, rootState, rootGetters) =>
  state.search.results.map(id => rootGetters["entities/account/get"](id)),
  getCount: state => state.search.count || 0,
  isLoading: state => state.search.loading,
};
