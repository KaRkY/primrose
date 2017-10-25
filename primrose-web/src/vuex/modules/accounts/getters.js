export default {
  results: (state, getters, rootState, rootGetters) =>
  state.results.map(id => rootGetters["entities/account/get"](id)),
  count: state => state.count || 0,
  loading: state => state.loading,
  pagination: state => state.pagination,
};
