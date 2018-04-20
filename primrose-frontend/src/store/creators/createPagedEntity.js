import { handleActions } from "redux-actions";
import { createSelector } from "reselect";

export default ({
  loadingAction,
  fetchedAction,
  errorAction,
  rootSelector,
}) => {
  const data = handleActions({
    [fetchedAction]: (state, action) => action.payload.data,
    [errorAction]: () => null,
  }, null);

  const count = handleActions({
    [fetchedAction]: (state, action) => action.payload.count,
    [errorAction]: () => null,
  }, 0);

  const loading = handleActions({
    [loadingAction]: (state, action) => !!!action.payload,
    [fetchedAction]: () => false,
    [errorAction]: () => false,
  }, false);

  const loadingSearch = handleActions({
    [loadingAction]: payload => payload,
    [fetchedAction]: () => false,
    [errorAction]: () => false,
  }, false);

  const error = handleActions({
    [loadingAction]: () => null,
    [fetchedAction]: () => null,
    [errorAction]: (state, action) => action.payload,
  }, null);

  const reducers = {
    data,
    count,
    loading,
    loadingSearch,
    error,
  };

  const selectors = {
    getCount: createSelector(rootSelector, root => root.count),
    getData: createSelector(rootSelector, root => root.data),
    getError: createSelector(rootSelector, root => root.error),
    isLoading: createSelector(rootSelector, root => root.loading),
    isLoadingSearch: createSelector(rootSelector, root => root.loadingSearch),
  };

  return {
    reducers,
    selectors,
  };
};